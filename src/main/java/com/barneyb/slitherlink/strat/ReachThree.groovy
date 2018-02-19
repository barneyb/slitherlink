package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SingleMoveStrategy

/**
 *
 * @author bboisvert
 */
class ReachThree implements SingleMoveStrategy {

    Move nextMove(Puzzle p) {
        def cells = p.clueCells(3)
        for (cc in cells) {
            for (dc in cc.dots()) {
                def externalEdges = dc.externalEdges(cc)
                if (externalEdges.count { it.state == EdgeState.ON } == 1) {
                    for (ec in externalEdges.findAll { it.state == EdgeState.UNKNOWN }) {
                        return new MoveImpl(this, ec, EdgeState.OFF)
                    }
                    // one edge to it
                    def internalEdges = dc.internalEdges(cc)
                    if (internalEdges.every { it.state == EdgeState.UNKNOWN }) {
                        // not yet connected
                        for (ec in cc.edges().minus(internalEdges)) {
                            if (ec.state != EdgeState.ON) {
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
