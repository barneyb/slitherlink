package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class ClueSatisfied implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        for (cc in p.clueCells()) {
            def edges = cc.edges()
            if (edges.count { it.state == EdgeState.ON } == cc.clue) {
                def mc = Utils.edgesIf(edges, EdgeState.OFF, EdgeState.UNKNOWN)
                if (mc) return mc
            }
        }
        null
    }
}
