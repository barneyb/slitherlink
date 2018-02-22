package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Moves
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import com.barneyb.slitherlink.edges

fun noBranching (p: Puzzle): Moves {
    var moves: Moves = null
    for (d in p.dots()) {
        if (d.edges(ON).size == 2) {
            moves = moves.edges(d.edges(UNKNOWN), OFF)
        }
    }
    return moves
}

fun singleUnknownEdge(p: Puzzle): Moves {
    var moves: Moves = null
    for (d in p.dots()) {
        val unknownEdges = d.edges(UNKNOWN)
        val onEdges = d.edges(ON)
        if (unknownEdges.size == 1) {
            moves = moves.edges(unknownEdges, if (onEdges.size == 1) ON else OFF)
        }
    }
    return moves
}
