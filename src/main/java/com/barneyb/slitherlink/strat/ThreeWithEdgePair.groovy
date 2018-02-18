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
                def externalEdges = dc.edges() - cc.edges()
                if (externalEdges.size() == 2 && externalEdges.every {
                    it.state() == EdgeState.OFF
                }) {
                    def internalEdges = dc.edges() - externalEdges
                    for (e in (internalEdges)) {
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
