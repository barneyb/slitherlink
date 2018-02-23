package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle

/**
 *
 *
 * @author barneyb
 */
class ThreeWithEdgePair extends AbstractEdgePairStrategy implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        return nextMoves(p, 3, Puzzle.ON)
    }

}
