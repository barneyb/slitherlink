package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.StaticStrategy
import com.barneyb.slitherlink.Dir
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
class TwoInCorner implements StaticStrategy {

    Move nextMove(Puzzle p) {
        def corners = [
            (p.cellCoord(0, 0))                  : [
                p.edgeCoord(0, 1, Dir.NORTH),
                p.edgeCoord(1, 0, Dir.WEST)
            ],
            (p.cellCoord(0, p.cols - 1))         : [
                p.edgeCoord(0, p.cols - 2, Dir.NORTH),
                p.edgeCoord(1, p.cols - 1, Dir.EAST)
            ],
            (p.cellCoord(p.rows - 1, p.cols - 1)): [
                p.edgeCoord(p.rows - 1, p.cols - 2, Dir.SOUTH),
                p.edgeCoord(p.rows - 2, p.cols - 1, Dir.EAST)
            ],
            (p.cellCoord(p.rows - 1, 0))         : [
                p.edgeCoord(p.rows - 1, 1, Dir.SOUTH),
                p.edgeCoord(p.rows - 2, 0, Dir.WEST)
            ]
        ]
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 2) {
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
