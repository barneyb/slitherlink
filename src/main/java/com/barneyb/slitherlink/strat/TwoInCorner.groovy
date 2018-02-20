package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StaticStrategy
import com.barneyb.slitherlink.SingleMoveStrategy

import static com.barneyb.slitherlink.Dir.*
/**
 *
 *
 * @author barneyb
 */
class TwoInCorner implements SingleMoveStrategy, StaticStrategy {

    Move nextMove(Puzzle p) {
        def corners = [
            (p.humanCellCoord(0, 0)): [
                p.humanEdgeCoord(0, 1, NORTH),
                p.humanEdgeCoord(1, 0, WEST)
            ],
            (p.humanCellCoord(0, p.cols - 1)): [
                p.humanEdgeCoord(0, p.cols - 2, NORTH),
                p.humanEdgeCoord(1, p.cols - 1, EAST)
            ],
            (p.humanCellCoord(p.rows - 1, p.cols - 1)): [
                p.humanEdgeCoord(p.rows - 1, p.cols - 2, SOUTH),
                p.humanEdgeCoord(p.rows - 2, p.cols - 1, EAST)
            ],
            (p.humanCellCoord(p.rows - 1, 0)): [
                p.humanEdgeCoord(p.rows - 1, 1, SOUTH),
                p.humanEdgeCoord(p.rows - 2, 0, WEST)
            ]
        ]
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
