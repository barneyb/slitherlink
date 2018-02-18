package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy
/**
 *
 *
 * @author barneyb
 */
class ThreeWithEdgePair implements Strategy {

    @Override
    Move nextMove(Puzzle p) {
        return nextMove(p, 3, EdgeState.ON)
    }

    protected Move nextMove(Puzzle p, int clue, EdgeState pairState) {
        for (cc in p.clueCells(clue)) {
            for (dc in cc.dots()) {
                if (dc.externalEdges(cc).every { it.state() == EdgeState.OFF }) {
                    for (e in dc.internalEdges(cc)) {
                        if (e.state() != pairState) {
                            return new MoveImpl(this, e, pairState)
                        }
                    }
                }
            }
        }
        null
    }

}