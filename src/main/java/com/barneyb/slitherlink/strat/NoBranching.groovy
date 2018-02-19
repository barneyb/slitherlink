package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.DotCoord
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
class NoBranching implements SingleMoveStrategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def edges = dc.edges()
            if (edges.count { it.state == EdgeState.ON } == 2) {
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
