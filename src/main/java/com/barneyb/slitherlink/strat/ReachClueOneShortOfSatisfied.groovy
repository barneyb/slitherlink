package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class ReachClueOneShortOfSatisfied implements MultiMoveStrategy {

    List<Move> nextMoves(Puzzle p) {
        for (cc in p.clueCells()) {
            def edgeMap = cc.edges().countBy {
                it.state
            }
            if (edgeMap.getOrDefault(EdgeState.OFF, 0) == (3 - cc.clue)) {
                for (dc in cc.dots()) {
                    def externalEdges = dc.externalEdges(cc)
                    if (externalEdges.count { it.state == EdgeState.ON } == 1) {
                        // one edge to it
                        def internalEdges = dc.internalEdges(cc)
                        if (internalEdges.every { it.state == EdgeState.UNKNOWN }) {
                            // not yet connected
                            def ms = Utils.edgesIf(externalEdges, EdgeState.OFF, EdgeState.UNKNOWN)
                            ms = Utils.edgesIf(ms, cc.edges().minus(internalEdges), EdgeState.ON, EdgeState.UNKNOWN)
                            if (ms) return ms
                        }
                    }
                }
            }
        }
        null
    }

}
