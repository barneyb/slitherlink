package com.barneyb.slitherlink.strat

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
            if (edges.collect { it.state }.count { it == Puzzle.ON || it == Puzzle.UNKNOWN } == cc.clue) {
                def mc = Utils.edgesIf(edges, Puzzle.ON, Puzzle.UNKNOWN)
                if (mc) return mc
            }
        }
        null
    }
}
