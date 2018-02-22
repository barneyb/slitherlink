package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Clue
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

fun threeWithEdgePair(p: Puzzle) = edgePairs(p, THREE, ON)

fun oneWithEdgePair(p: Puzzle) = edgePairs(p, ON, OFF)

private fun edgePairs(p: Puzzle, clue: Clue, state: EdgeState) = buildSequence {
    for (pair in allAndPairs(p, clue)) {
        setTo(pair.edges, state)
    }
}

fun twoWithEdgePairHasWhiskers(p: Puzzle) = buildSequence {
    for (pair in allAndPairs(p, TWO)) {
        for (d in pair.ends) {
            val edges = pair.cell.externalEdges(d)
            if (edges.count { it.unknown } == 1) {
                setUnknownTo(edges, if (edges.any { it.on }) OFF else ON)
            }
        }
    }
}

private fun allAndPairs(p: Puzzle) = lastTwoUnknownEdgesOfDot(p)

private fun allAndPairs(p: Puzzle, clue: EdgeState) = allAndPairs(p).filter {
    it.cell.clue == clue
}

private fun lastTwoUnknownEdgesOfDot(p: Puzzle) = buildSequence {
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
