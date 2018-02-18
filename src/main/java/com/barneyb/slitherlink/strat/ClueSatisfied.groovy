package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy

/**
 *
 * @author bboisvert
 */
class ClueSatisfied implements Strategy {

    Move nextMove(Puzzle p) {
        for (entry in p.clues()) {
            def cc = entry.key
            def clue = entry.value
            def edges = p.edges(cc)
            if (edges.count { it.state() == EdgeState.ON } == clue) {
                for (EdgeCoord ec : edges) {
                    if (ec.state() == EdgeState.UNKNOWN) {
                        return new MoveImpl(this, ec, EdgeState.OFF)
                    }
                }
            }
        }
        null
    }

}
