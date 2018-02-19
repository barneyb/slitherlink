package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
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
                    def ms = []
                    for (e in dc.internalEdges(cc)) {
                        if (e.state != pairState) {
                            ms << new MoveImpl(this, e, pairState)
                        }
                    }
                    if (! ms.empty) return ms
                }
            }
        }
        null
    }

}
