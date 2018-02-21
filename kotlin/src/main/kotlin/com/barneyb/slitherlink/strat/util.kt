package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.ON

/**
 *
 *
 * @author barneyb
 */

fun edges(edges: Collection<Edge>, state: EdgeState)
        = edges(null, edges, state)

fun edges(moves: MutableList<Move>?, edges: Collection<Edge>, state: EdgeState)
        = edgesUnless(moves, edges, state, state)

fun edgesIf(edges: Collection<Edge>, state: EdgeState, check: EdgeState)
        = edgesIf(null, edges, state, check)

fun edgesIf(moves: MutableList<Move>?, edges: Collection<Edge>, state: EdgeState, check: EdgeState)
        = edgesIf(moves, edges, state, { it -> it == check })

fun edgesUnless(edges: Collection<Edge>, state: EdgeState, check: EdgeState)
        = edgesUnless(null, edges, state, check)

fun edgesUnless(moves: MutableList<Move>?, edges: Collection<Edge>, state: EdgeState, check: EdgeState)
        = edgesIf(moves, edges, state, { it -> it != check })

private fun edgesIf(moves: MutableList<Move>?, edges: Collection<Edge>, state: EdgeState, test: (EdgeState) -> Boolean): MutableList<Move>? {
    var ms = moves
    for (e in edges) {
        if (test(e.state)) {
            if (ms == null) {
                ms = mutableListOf()
            }
            ms.add(Move(e, state))
        }
    }
    return ms
}


data class FindOtherEndStats(
    val otherEnd: Dot,
    val edgeCount: Int
)

fun findOtherEndHelper(start: Dot, prior: Dot, initial: Dot? = null): FindOtherEndStats {
    var curr = start
    var prev = prior
    var i = 0;
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
