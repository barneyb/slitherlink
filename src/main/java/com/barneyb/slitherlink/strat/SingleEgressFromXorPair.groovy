package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
class SingleEgressFromXorPair extends AbstractXorPairStrategy implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        def mc = null
        for (fc in getXorEdgePairs(p)) {
            def edges = fc.dot
                .externalEdges(fc.cell)
            def edgeMap = edges
                .countBy {
                    it.state
                }
            if (edgeMap.getOrDefault(Puzzle.UNKNOWN, 0) == 1) {
                if (edgeMap.containsKey(Puzzle.ON)) {
                    mc = Utils.edgesIf(mc, edges, Puzzle.OFF, Puzzle.UNKNOWN)
                } else {
                    mc = Utils.edgesIf(mc, edges, Puzzle.ON, Puzzle.UNKNOWN)
                }
            }
        }
        mc
    }

}
