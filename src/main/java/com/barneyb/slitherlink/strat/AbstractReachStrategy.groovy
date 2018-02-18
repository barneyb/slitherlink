package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy

/**
 *
 *
 * @author barneyb
 */
abstract class AbstractReachStrategy implements Strategy {

    protected MoveImpl nextMove(Puzzle p, int clue, EdgeState otherState) {
        def cells = p.clueCells(clue)
        for (cc in cells) {
            for (dc in cc.dots()) {
                if (dc.externalEdges(cc).count { it.state() == EdgeState.ON } == 1) {
                    // one edge to it
                    def internalEdges = dc.internalEdges(cc)
                    if (internalEdges.every { it.state() == EdgeState.UNKNOWN }) {
                        // not yet connected
                        for (ec in cc.edges().minus(internalEdges)) {
                            if (ec.state() != otherState) {
                                return new MoveImpl(this, ec, otherState)
                            }
                        }
                    }
                }
            }
        }
        null
    }

}
