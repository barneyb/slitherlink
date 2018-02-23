package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.EdgeCoord
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
class OneInCorner implements SingleMoveStrategy, StaticStrategy {

    Move nextMove(Puzzle p) {
        def corners = p.cornerEdgeMap()
        for (CellCoord cc : corners.keySet()) {
            def c = cc.clue
            if (c == 1) {
                for (EdgeCoord ec : corners[cc]) {
                    if (ec.state != Puzzle.OFF) {
                        return new MoveImpl(ec, Puzzle.OFF)
                    }
                }
            }
        }
        return null
    }

}
