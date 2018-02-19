package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class NeedAllRemaining implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        for (cc in p.clueCells()) {
            def edges = cc.edges()
            if (edges.collect { it.state }.count { it == EdgeState.ON || it == EdgeState.UNKNOWN } == cc.clue) {
                def mc = Utils.edgesOnIf(edges, EdgeState.UNKNOWN)
                if (mc) return mc
            }
        }
        null
    }
}
