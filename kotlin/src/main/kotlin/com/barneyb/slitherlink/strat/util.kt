package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Cell
import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Evidence
import com.barneyb.slitherlink.IllegalMoveException
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.PuzzleItem
import com.barneyb.slitherlink.TWO
import com.barneyb.slitherlink.ruleStrategies
import kotlin.coroutines.experimental.SequenceBuilder
import kotlin.coroutines.experimental.buildSequence

suspend fun SequenceBuilder<Move>.setUnknownTo(
    edges: Collection<Edge>,
    state: EdgeState
) {
    for (e in edges) {
        if (e.unknown) {
            yield(Move(e, state))
        }
    }
}

suspend fun SequenceBuilder<Move>.setTo(
    edges: Collection<Edge>,
    state: EdgeState
) {
    for (e in edges) {
        yield(Move(e, state))
    }
}

suspend fun SequenceBuilder<Move>.setUnknownTo(edge: Edge, state: EdgeState) {
    if (edge.unknown) {
        yield(Move(edge, state))
    }
}

suspend fun SequenceBuilder<Move>.setTo(edge: Edge, state: EdgeState) {
    yield(Move(edge, state))
}

data class Segment(
    val start: Dot,
    val end: Dot,
    val length: Int
) {
    val reverse get() = Segment(end, start, length)
}

fun allSegments(p: Puzzle): Collection<Segment> {
    val segments = mutableMapOf<Dot, Segment>()
    for (dot in p.dots()) {
        if (segments.containsKey(dot)) {
            // already found it in the other direction
            continue
        }
        val edges = dot.edges(ON)
        if (edges.size == 1) {
            val s = getSegment(dot, edges.first())
            segments[s.end] = s
        }
    }
    return segments.values
}

fun allSegmentsDirected(p: Puzzle): Collection<Segment> {
    val forward = allSegments(p)
    return forward + forward.map {
        it.reverse
    }
}

fun getSegment(
    start: Dot,
    edge: Edge? = null
): Segment {
    val initialEdge = when (edge) {
        null -> {
            val es = start.edges(ON)
            if (es.size != 1) {
                throw IllegalArgumentException("$start isn't the end of a segment")
            }
            es.first()
        }
        else -> edge
    }
    var curr = start.otherEnd(initialEdge)
    var prev = start
    var i = 1
    while (true) {
        val outbound = curr.edges(ON)
        if (outbound.size == 1) {
            return Segment(start, curr, i)
        }
        val nexts = outbound
            .map { it.dots }
            .flatten()
            .filter {
                it != curr && it != prev
            }
        prev = curr
        curr = nexts.first()
        i += 1
    }
}

data class EdgePair(
    val cell: Cell,
    val dot: Dot
) {

    val hasOpposedCell get() = dot.hasOpposedCell(cell)

    val opposedCell get() = dot.opposedCell(cell)

    val edges get() = cell.internalEdges(dot)

    val externalEdges get() = cell.externalEdges(dot)

    val opposedEdges get() = cell.opposedEdges(dot)

    val ends get() = edges.map { it.otherDot(dot) }

    val opposedDot get() = cell.opposedDot(dot)

}

internal suspend fun SequenceBuilder<EdgePair>.maybeYieldEdgePair(
    cell: Cell,
    dot: Dot
) {
    if (cell.internalEdges(dot).all { it.unknown }) {
        yield(EdgePair(cell, dot))
        if (cell.clue == TWO) {
            yield(EdgePair(cell, cell.opposedDot(dot)))
        }
    }
}

internal fun propagateAlongTwos(pairs: Sequence<EdgePair>) = buildSequence {
    for (p in pairs) {
        yield(p)
        var (cell, dot) = p
        while (dot.hasOpposedCell(cell)) {
            cell = dot.opposedCell(cell)
            maybeYieldEdgePair(cell, dot)
            if (cell.clue != TWO) {
                break
            }
            dot = cell.opposedDot(dot)
            maybeYieldEdgePair(cell, dot)
        }
    }
}

/**
 * I am the number of steps to look ahead to see if an illegal move can be
 * forced by a given candidate move.
 */
const val MAX_LOOKAHEAD = 5

fun createsContradiction(puzzle: Puzzle, vararg moves: Move) =
    createsContradiction(puzzle, moves.asList())

fun createsContradiction(puzzle: Puzzle, moves: Collection<Move>): Pair<Boolean, Evidence> {
    val p = puzzle.scratch()
    for (m in moves) {
        p.move(m)
    }
    val path = mutableSetOf<PuzzleItem>()
    try {
        var i = 0
        do {
            var moved = false
            ruleStrategies.forEach { strat ->
                strat(p).forEach { m ->
                    p.move(m)
                    path.add(m.edge)
                    moved = true
                }
            }
        } while (moved && ++i < MAX_LOOKAHEAD)
    } catch (ime: IllegalMoveException) {
        return Pair(true, mapOf(
            "path" to path,
            "illegal" to setOf(ime.move.edge)
        ))
    }
    return Pair(false, emptyMap())
}
