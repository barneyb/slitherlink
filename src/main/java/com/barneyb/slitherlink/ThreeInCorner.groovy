package com.barneyb.slitherlink
/**
 *
 *
 * @author barneyb
 */
class ThreeInCorner implements Strategy {

    Move nextMove(Puzzle p) {
        def corners = getCornerMap(p)
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 3) {
                for (EdgeCoord ec : corners[cc]) {
                    if (p.edge(ec) == EdgeState.UNKNOWN) {
                        return new MoveImpl(ec, EdgeState.ON)
                    }
                }
            }
        }
        return null
    }

    private Map<CellCoord, List<EdgeCoord>> getCornerMap(Puzzle p) {
        [
            (new CellCoord(0, 0)): [
                new EdgeCoord(0, 0, Dir.NORTH),
                new EdgeCoord(0, 0, Dir.WEST)
            ],
            (new CellCoord(0, p.cols - 1)): [
                new EdgeCoord(0, p.cols - 1, Dir.NORTH),
                new EdgeCoord(0, p.cols - 1, Dir.EAST)
            ],
            (new CellCoord(p.rows - 1, p.cols - 1)): [
                new EdgeCoord(p.rows - 1, p.cols - 1, Dir.SOUTH),
                new EdgeCoord(p.rows - 1, p.cols - 1, Dir.EAST)
            ],
            (new CellCoord(p.rows - 1, 0)): [
                new EdgeCoord(p.rows - 1, 0, Dir.SOUTH),
                new EdgeCoord(p.rows - 1, 0, Dir.WEST)
            ]
        ]
    }

}
