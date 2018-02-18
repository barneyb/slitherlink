package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.ClueOnly
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
class OneInCorner implements Strategy, ClueOnly {

    Move nextMove(Puzzle p) {
        def corners = p.cornerEdgeMap()
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 1) {
                for (EdgeCoord ec : corners[cc]) {
                    if (p.edge(ec) != EdgeState.OFF) {
                        return new MoveImpl(ec, EdgeState.OFF)
                    }
                }
            }
        }
        return null
    }

}
