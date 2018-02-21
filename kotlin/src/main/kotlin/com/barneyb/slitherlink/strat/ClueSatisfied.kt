package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StatelessStrategy
import com.barneyb.slitherlink.UNKNOWN

/**
 *
 *
 * @author barneyb
 */

class ClueSatisfied : StatelessStrategy {
    override val name = "ClueSatisfied"

    override fun nextMoves(p: Puzzle): Collection<Move>? {
        var moves: MutableList<Move>? = null
        for (cc in p.clueCells()) {
            val edges = cc.edges
            if (edges.count { it.state == ON } == cc.clue) {
                moves = edgesIf(moves, edges, OFF, UNKNOWN)
            }
        }
        return moves
    }

}
