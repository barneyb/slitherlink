package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class ReachThree implements MultiMoveStrategy {

    List<Move> nextMoves(Puzzle p) {
        for (cc in p.clueCells(3)) {
            for (dc in cc.dots()) {
                def externalEdges = dc.externalEdges(cc)
                if (externalEdges.count { it.state == EdgeState.ON } == 1) {
                    def ms = Utils.edgesIf(externalEdges, EdgeState.OFF, EdgeState.UNKNOWN)
                    // one edge to it
                    def internalEdges = dc.internalEdges(cc)
                    if (internalEdges.every { it.state == EdgeState.UNKNOWN }) {
                        // not yet connected
                        ms = Utils.edges(ms, cc.edges().minus(internalEdges), EdgeState.ON)
                    }
                    if (ms) return ms
                }
            }
        }
        null
    }

}
