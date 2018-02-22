package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

fun threeWithEdgePair(p: Puzzle) = buildSequence {
    for (pair in allAndPairs(p)) {
        if (pair.cell.clue == THREE) {
            setTo(pair.edges, ON)
        }
    }
}

fun allAndPairs(p: Puzzle) = lastTwoUnknownEdgesOfDot(p)

fun lastTwoUnknownEdgesOfDot(p: Puzzle) = buildSequence {
    for (d in p.dots()) {
        val unknown = d.edges(UNKNOWN)
        val on = d.edges(ON)
        if (unknown.size == 2 && on.isEmpty()) {
            // one away from satisfied, and two unknowns
            val cells = unknown.first().cells.intersect(unknown.last().cells)
            if (cells.size == 1) {
                // unknowns make a corner!
                yield(EdgePair(cells.first(), d))
            }
        }
    }
}
