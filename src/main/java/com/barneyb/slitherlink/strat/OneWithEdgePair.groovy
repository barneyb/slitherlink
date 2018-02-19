package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
class OneWithEdgePair extends AbstractEdgePairStrategy {

    @Override
    Move nextMove(Puzzle p) {
        return nextMove(p, 1, EdgeState.OFF)
    }

}
