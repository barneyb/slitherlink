package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Cell
import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.ON
import kotlin.coroutines.experimental.SequenceBuilder

suspend fun SequenceBuilder<Move>.setUnknownTo(edges: Collection<Edge>, state: EdgeState) {
    for (e in edges) {
        if (e.unknown) {
            yield(Move(e, state))
        }
    }
}

suspend fun SequenceBuilder<Move>.setTo(edges: Collection<Edge>, state: EdgeState) {
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

data class FindOtherEndStats(
        val otherEnd: Dot,
        val edgeCount: Int
)

fun findOtherEndHelper(start: Dot, prior: Dot, initial: Dot? = null): FindOtherEndStats {
    var curr = start
    var prev = prior
    var i = 0
    while (true) {
        val outbound = curr.edges(ON)
        if (outbound.size == 1 || curr == initial) {
            return FindOtherEndStats(curr, i)
        }
        if (outbound.size != 2) {
            throw IllegalArgumentException("branch at $curr")
        }
        val itr = outbound.iterator()
        val ds = itr.next().dots + itr.next().dots
        val nexts = ds.filter {
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

}
