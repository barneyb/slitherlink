package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.ClueOnly
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
class TwoInCorner implements Strategy, ClueOnly {

    Move nextMove(Puzzle p) {
        def corners = [
            (new CellCoord(0, 0))                  : [
                new EdgeCoord(0, 1, Dir.NORTH),
                new EdgeCoord(1, 0, Dir.WEST)
            ],
            (new CellCoord(0, p.cols - 1))         : [
                new EdgeCoord(0, p.cols - 2, Dir.NORTH),
                new EdgeCoord(1, p.cols - 1, Dir.EAST)
            ],
            (new CellCoord(p.rows - 1, p.cols - 1)): [
                new EdgeCoord(p.rows - 1, p.cols - 2, Dir.SOUTH),
                new EdgeCoord(p.rows - 2, p.cols - 1, Dir.EAST)
            ],
            (new CellCoord(p.rows - 1, 0))         : [
                new EdgeCoord(p.rows - 1, 1, Dir.SOUTH),
                new EdgeCoord(p.rows - 2, 0, Dir.WEST)
            ]
        ]
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 2) {
                for (EdgeCoord ec : corners[cc]) {
                    if (p.edge(ec) != EdgeState.ON) {
                        return new MoveImpl(ec, EdgeState.ON)
                    }
                }
            }
        }
        return null
    }

}
