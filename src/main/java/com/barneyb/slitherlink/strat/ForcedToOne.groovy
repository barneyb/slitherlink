package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle

/**
 *
 * @author bboisvert
 */
class ForcedToOne implements MultiMoveStrategy {

    List<Move> nextMoves(Puzzle p) {
        def cells = p.clueCells(1)
        for (cc in cells) {
            for (dc in cc.dots()) {
                def externalEdges = dc.externalEdges(cc)
                if (externalEdges.count { it.state == EdgeState.ON } == 1
                    && externalEdges.count { it.state == EdgeState.UNKNOWN } == 0
                ) {
                    // one edge to it
                    def internalEdges = dc.internalEdges(cc)
                    if (internalEdges.every { it.state == EdgeState.UNKNOWN }) {
                        // not yet connected
                        def ms = Utils.edges(cc.edges().minus(internalEdges), EdgeState.OFF)
                        if (ms) return ms
                    }
                }
            }
        }
        null
    }

}
