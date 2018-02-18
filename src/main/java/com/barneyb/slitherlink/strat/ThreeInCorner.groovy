package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.StaticStrategy
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy

/**
 *
 *
 * @author barneyb
 */
class ThreeInCorner implements StaticStrategy {

    Move nextMove(Puzzle p) {
        def corners = p.cornerEdgeMap()
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 3) {
                for (EdgeCoord ec : corners[cc]) {
                    if (ec.state() != EdgeState.ON) {
                        return new MoveImpl(this, ec, EdgeState.ON)
                    }
                }
            }
        }
        return null
    }

}
