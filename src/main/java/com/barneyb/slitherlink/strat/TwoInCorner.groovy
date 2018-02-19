package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StaticStrategy

import static com.barneyb.slitherlink.Dir.*
/**
 *
 *
 * @author barneyb
 */
class TwoInCorner implements StaticStrategy {

    Move nextMove(Puzzle p) {
        def corners = [
            (p.cellCoord(0, 0)): [
                p.edgeCoord(0, 1, NORTH),
                p.edgeCoord(1, 0, WEST)
            ],
            (p.cellCoord(0, p.cols - 1)): [
                p.edgeCoord(0, p.cols - 2, NORTH),
                p.edgeCoord(1, p.cols - 1, EAST)
            ],
            (p.cellCoord(p.rows - 1, p.cols - 1)): [
                p.edgeCoord(p.rows - 1, p.cols - 2, SOUTH),
                p.edgeCoord(p.rows - 2, p.cols - 1, EAST)
            ],
            (p.cellCoord(p.rows - 1, 0)): [
                p.edgeCoord(p.rows - 1, 1, SOUTH),
                p.edgeCoord(p.rows - 2, 0, WEST)
            ]
        ]
        for (CellCoord cc : corners.keySet()) {
            if (cc.clue == 2) {
                for (EdgeCoord ec : corners[cc]) {
                    if (ec.state != EdgeState.ON) {
                        return new MoveImpl(this, ec, EdgeState.ON)
                    }
                }
            }
        }
        return null
    }

}
