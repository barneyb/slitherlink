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
class ReachOne implements Strategy {

    Move nextMove(Puzzle p) {
        def cells = p.clueCells(1)
        for (cc in cells) {
            for (dc in cc.dots()) {
                def externalEdges = dc.externalEdges(cc)
                if (externalEdges.count { it.state() == EdgeState.ON } == 1
                    && externalEdges.count { it.state() == EdgeState.UNKNOWN } == 0
                ) {
                    // one edge to it
                    def internalEdges = dc.internalEdges(cc)
                    if (internalEdges.every { it.state() == EdgeState.UNKNOWN }) {
                        // not yet connected
                        for (ec in cc.edges().minus(internalEdges)) {
                            if (ec.state() != EdgeState.OFF) {
                                return new MoveImpl(this, ec, EdgeState.OFF)
                            }
                        }
                    }
                }
            }
        }
        null
    }

}
