package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
abstract class AbstractEdgePairStrategy {

    protected List<Move> nextMoves(Puzzle p, int clue, EdgeState pairState) {
        for (cc in p.clueCells(clue)) {
            for (dc in cc.dots()) {
                if (dc.externalEdges(cc).every { it.state == EdgeState.OFF }) {
                    def ms = Utils.edges(dc.internalEdges(cc), pairState)
                    if (ms) return ms
                }
            }
        }
        null
    }

}
