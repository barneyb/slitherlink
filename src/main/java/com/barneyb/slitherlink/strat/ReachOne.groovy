package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class ReachOne extends AbstractReachStrategy {

    Move nextMove(Puzzle p) {
        return nextMove(p, 1, EdgeState.OFF)
    }

}
