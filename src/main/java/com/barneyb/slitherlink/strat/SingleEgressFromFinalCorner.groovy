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
                .findAll {
                    it.state == EdgeState.UNKNOWN
                }
            if (edges.size() == 1) {
                // single egress
                mc = Utils.edges(edges, EdgeState.ON)
            }
        }
        mc
    }

}
