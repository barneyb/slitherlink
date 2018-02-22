package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

fun noBranching(p: Puzzle) = toMoves(noBranchingSeq(p))

fun noBranchingSeq(p: Puzzle) = buildSequence {
    for (d in p.dots()) {
        if (d.edges.count { it.on } == 2) {
            setUnknownTo(d.edges, OFF)
        }
    }
}

fun singleUnknownEdge(p: Puzzle) = toMoves(singleUnknownEdgeSeq(p))

fun singleUnknownEdgeSeq(p: Puzzle) = buildSequence {
    for (d in p.dots()) {
        val unknownEdges = d.edges(UNKNOWN)
        val onEdges = d.edges(ON)
        if (unknownEdges.size == 1) {
            setTo(unknownEdges, if (onEdges.size == 1) ON else OFF)
        }
    }
}
