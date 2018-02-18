package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class ReachThree extends AbstractReachStrategy {

    Move nextMove(Puzzle p) {
        return nextMove(p, 3, EdgeState.ON)
    }

}
