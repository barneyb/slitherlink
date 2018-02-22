package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Moves
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.edges

fun edges(edges: Collection<Edge>, state: EdgeState) = (null as Moves).edges(edges, state)

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
