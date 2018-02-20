package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
class SingleEgressFromFinalCorner extends AbstractFinalCornerStrategy implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        def mc = null
        for (fc in getFinalCorners(p)) {
            def edges = fc.dot
                .externalEdges(fc.cell)
            def edgeMap = edges
                .countBy {
                    it.state
                }
            if (edgeMap.getOrDefault(EdgeState.UNKNOWN, 0) == 1) {
                if (edgeMap.containsKey(EdgeState.ON)) {
                    mc = Utils.edgesIf(mc, edges, EdgeState.OFF, EdgeState.UNKNOWN)
                } else {
                    mc = Utils.edgesIf(mc, edges, EdgeState.ON, EdgeState.UNKNOWN)
                }
            }
        }
        mc
    }

}
