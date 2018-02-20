package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
class OneWithEdgePair extends AbstractEdgePairStrategy implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        nextMoves(p, 1, Puzzle.OFF)
    }

}
