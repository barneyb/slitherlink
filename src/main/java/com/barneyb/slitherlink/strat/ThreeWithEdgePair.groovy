package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Puzzle
/**
 *
 *
 * @author barneyb
 */
class ThreeWithEdgePair extends AbstractEdgePairStrategy {

    @Override
    Move nextMove(Puzzle p) {
        return nextMove(p, 3, EdgeState.ON)
    }

}
