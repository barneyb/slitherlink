package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SingleMoveStrategy

/**
 *
 * @author bboisvert
 */
class ClueSatisfied implements SingleMoveStrategy {

    Move nextMove(Puzzle p) {
        for (cc in p.clueCells()) {
            def edges = cc.edges()
            if (edges.count { it.state == EdgeState.ON } == cc.clue) {
                for (EdgeCoord ec : edges) {
                    if (ec.state == EdgeState.UNKNOWN) {
                        return new MoveImpl(this, ec, EdgeState.OFF)
                    }
                }
            }
        }
        null
    }

}
