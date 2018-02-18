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
class NeedAllRemaining implements Strategy {

    Move nextMove(Puzzle p) {
        for (cc in p.clueCells()) {
            def edges = cc.edges()
            if (edges.collect { it.state() }.count { it == EdgeState.ON || it == EdgeState.UNKNOWN } == cc.clue()) {
                for (EdgeCoord ec : edges) {
                    if (ec.state() == EdgeState.UNKNOWN) {
                        return new MoveImpl(this, ec, EdgeState.ON)
                    }
                }
            }
        }
        null
    }

}
