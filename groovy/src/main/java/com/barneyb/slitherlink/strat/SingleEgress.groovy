package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.DotCoord
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SingleMoveStrategy

/**
 *
 *
 * @author barneyb
 */
class SingleEgress implements SingleMoveStrategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def edgeMap = dc.edges().groupBy { it.state }
            def unknownEdges = edgeMap[Puzzle.UNKNOWN]
            def onEdges = edgeMap[Puzzle.ON]
            if (unknownEdges != null && unknownEdges.size() == 1 && onEdges != null && onEdges.size() == 1) {
                return new MoveImpl(unknownEdges.first(), Puzzle.ON)
            }
        }
        return null
    }

}
