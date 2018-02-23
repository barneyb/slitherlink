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
class SingleIngress implements SingleMoveStrategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def edgeMap = dc.edges().groupBy { it.state }
            def unknownEdges = edgeMap[Puzzle.UNKNOWN]
            if (unknownEdges != null && unknownEdges.size() == 1 && !edgeMap.containsKey(Puzzle.ON)) {
                return new MoveImpl(unknownEdges.first(), Puzzle.OFF)
            }
        }
        return null
    }

}
