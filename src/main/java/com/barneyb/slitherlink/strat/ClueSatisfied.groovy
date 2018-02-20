package com.barneyb.slitherlink.strat

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
            if (edges.count { it.state == Puzzle.ON } == cc.clue) {
                def mc = Utils.edgesIf(edges, Puzzle.OFF, Puzzle.UNKNOWN)
                if (mc) return mc
            }
        }
        null
    }
}
