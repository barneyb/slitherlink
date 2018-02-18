package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.DotCoord
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
class SingleEgress implements Strategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def unknownEdges = dc.edges(EdgeState.UNKNOWN)
            def onEdges = dc.edges(EdgeState.ON)
            if (unknownEdges.size() == 1 && onEdges.size() == 1) {
                return new MoveImpl(this, unknownEdges.first(), EdgeState.ON)
            }
        }
        return null
    }

}
