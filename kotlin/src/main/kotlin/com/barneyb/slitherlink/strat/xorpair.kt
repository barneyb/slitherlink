package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Cell
import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.SequenceBuilder
import kotlin.coroutines.experimental.buildSequence

fun forcedToOne(p: Puzzle) = buildSequence {
    for (pair in allXorPairs(p)) {
        if (pair.cell.clue == 1) {
            setUnknownTo(pair.cell.opposedEdges(pair.dot), OFF)
        }
    }
}

fun singleXorPairEgress(p: Puzzle) = buildSequence {
    for (pair in allXorPairs(p)) {
        val externalEdges = pair.cell.externalEdges(pair.dot)
        if (externalEdges.count { it.unknown } == 1
                && externalEdges.count { it.on } == 0
        ) {
            setUnknownTo(externalEdges, ON)
        }
    }
}

fun allXorPairs(p: Puzzle) = propagateAlongTwos(
        buildSequence {
            yieldAll(lastTwoUnknownEdgesOfCell(p))
            yieldAll(forcedTo(p))
        })

fun propagateAlongTwos(pairs: Sequence<EdgePair>) = buildSequence {
    for (p in pairs) {
        yield(p)
        var (cell, dot) = p
        while (cell.clue == 2) {
            dot = cell.opposedDot(dot)
            maybeYieldXorPair(cell, dot)
            if (dot.hasOpposedCell(cell)) {
                cell = dot.opposedCell(cell)
                maybeYieldXorPair(cell, dot)
            } else {
                break
            }
        }
    }
}

private suspend fun SequenceBuilder<EdgePair>.maybeYieldXorPair(cell: Cell, dot: Dot) {
    if (cell.internalEdges(dot).all { it.unknown }) {
        yield(EdgePair(cell, dot))
    }
}

fun lastTwoUnknownEdgesOfCell(p: Puzzle) = buildSequence {
    for (cc in p.clueCells()) {
        val unknown = cc.edges(UNKNOWN)
        val on = cc.edges(ON)
        if (unknown.size == 2 && on.size == cc.clue - 1) {
            // one away from satisfied, and two unknowns
            val dots = unknown.first().dots.intersect(unknown.last().dots)
            if (dots.size == 1) {
                // unknowns make a corner!
                val cell = cc
                val dot = dots.first()
                maybeYieldXorPair(cell, dot)
            }
        }
    }
}

fun forcedTo(p: Puzzle) = buildSequence {
    for (c in p.cells) {
        for (d in c.dots) {
            val externalEdges = c.externalEdges(d)
            if (externalEdges.count { it.on } == 1
                    && externalEdges.count { it.unknown } == 0
                    && c.internalEdges(d).none { it.on }
            ) {
                yield(EdgePair(c, d))
            }
        }
    }
}