package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SingleMoveStrategy
import com.barneyb.slitherlink.StaticStrategy
/**
 *
 *
 * @author barneyb
 */
class TwoInCorner implements SingleMoveStrategy, StaticStrategy {

    Move nextMove(Puzzle p) {
        def corners = p.cornerEgressMap()
        for (CellCoord cc : corners.keySet()) {
            if (cc.clue == 2) {
                for (EdgeCoord ec : corners[cc]) {
                    if (ec.state != EdgeState.ON) {
                        return new MoveImpl(ec, EdgeState.ON)
                    }
                }
            }
        }
        return null
    }

}
