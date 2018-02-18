package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy
/**
 *
 * @author bboisvert
 */
class ReachThree implements Strategy {

    Move nextMove(Puzzle p) {
        def threes = p.clueCells(3)
        for (cc in threes) {
            for (dc in cc.dots()) {
                if (dc.externalEdges(cc).count { it.state() == EdgeState.ON } == 1) {
                    // one edge to it
                    def internalEdges = dc.internalEdges(cc)
                    if (internalEdges.every { it.state() == EdgeState.UNKNOWN }) {
                        // not yet connected
                        for (ec in cc.edges().minus(internalEdges)) {
                            if (ec.state() != EdgeState.ON) {
                                return new MoveImpl(this, ec, EdgeState.ON)
                            }
                        }
                    }
                }
            }
        }
        null
    }

}
