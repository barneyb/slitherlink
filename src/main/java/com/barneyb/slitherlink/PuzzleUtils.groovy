package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
final class PuzzleUtils {

    private PuzzleUtils() {}

    static List<CellCoord> corners(Puzzle p) {
        [
            new CellCoord(0, 0),
            new CellCoord(0, p.cols - 1),
            new CellCoord(p.rows - 1, p.cols - 1),
            new CellCoord(p.rows - 1, 0),
        ]
    }

    static Map<CellCoord, List<EdgeCoord>> cornerEdgeMap(Puzzle p) {
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
