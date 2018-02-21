package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy
import com.barneyb.slitherlink.UNKNOWN

class ClueSatisfied : Strategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        var moves: MutableList<Move>? = null
        for (c in p.clueCells()) {
            val edges = c.edges
            if (edges.count { it.state == ON } == c.clue) {
                moves = edgesIf(moves, edges, OFF, UNKNOWN)
            }
        }
        return moves
    }
}

class NeedAllRemaining : Strategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        var moves: MutableList<Move>? = null
        for (c in p.clueCells()) {
            val unknown = c.edges(UNKNOWN)
            if (unknown.isEmpty()) {
                continue
            }
            val onCount = c.edges(ON).size
            if (onCount + unknown.size == c.clue) {
                moves = edges(moves, unknown, ON)
            }
        }
        return moves
    }

}