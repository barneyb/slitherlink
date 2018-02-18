package com.barneyb.slitherlink
/**
 *
 *
 * @author barneyb
 */
class TwoInCorner implements Strategy, ClueOnly {

    Move nextMove(Puzzle p) {
        def corners = [
            (new CellCoord(0, 0)): [
                new EdgeCoord(0, 1, Dir.NORTH),
                new EdgeCoord(1, 0, Dir.WEST)
            ],
            (new CellCoord(0, p.cols - 1)): [
                new EdgeCoord(0, p.cols - 2, Dir.NORTH),
                new EdgeCoord(1, p.cols - 1, Dir.EAST)
            ],
            (new CellCoord(p.rows - 1, p.cols - 1)): [
                new EdgeCoord(p.rows - 1, p.cols - 2, Dir.SOUTH),
                new EdgeCoord(p.rows - 2, p.cols - 1, Dir.EAST)
            ],
            (new CellCoord(p.rows - 1, 0)): [
                new EdgeCoord(p.rows - 1, 1, Dir.SOUTH),
                new EdgeCoord(p.rows - 2, 0, Dir.WEST)
            ]
        ]
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 2) {
                for (EdgeCoord ec : corners[cc]) {
                    if (p.edge(ec) == EdgeState.UNKNOWN) {
                        return new MoveImpl(ec, EdgeState.ON)
                    }
                }
            }
        }
        return null
    }

}
