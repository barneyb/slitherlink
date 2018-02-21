package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy
import com.barneyb.slitherlink.UNKNOWN

class NoBranching : Strategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        var moves: MutableList<Move>? = null
        for (d in p.dots()) {
            if (d.edges(ON).size == 2) {
                moves = edges(moves, d.edges(UNKNOWN), OFF)
            }
        }
        return moves
    }
}

class SingleUnknownEdge : Strategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        var moves: MutableList<Move>? = null
        for (d in p.dots()) {
            val unknownEdges = d.edges(UNKNOWN)
            val onEdges = d.edges(ON)
            if (unknownEdges.size == 1) {
                moves = edges(moves, unknownEdges, if (onEdges.size == 1) ON else OFF)
            }
        }
        return moves
    }
}