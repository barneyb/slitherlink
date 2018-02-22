package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Moves
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy
import com.barneyb.slitherlink.UNKNOWN
import com.barneyb.slitherlink.edges
import com.barneyb.slitherlink.edgesIf

class ClueSatisfied : Strategy {
    override fun nextMoves(p: Puzzle): Moves {
        var moves: MutableList<Move>? = null
        for (c in p.clueCells()) {
            val edges = c.edges
            if (edges.count { it.state == ON } == c.clue) {
                moves = moves.edgesIf(edges, OFF, UNKNOWN)
            }
        }
        return moves
    }
}

class NeedAllRemaining : Strategy {
    override fun nextMoves(p: Puzzle): Moves {
        var moves: MutableList<Move>? = null
        for (c in p.clueCells()) {
            val unknown = c.edges(UNKNOWN)
            if (unknown.isEmpty()) {
                continue
            }
            val onCount = c.edges(ON).size
            if (onCount + unknown.size == c.clue) {
                moves = moves.edges(unknown, ON)
            }
        }
        return moves
    }

}
