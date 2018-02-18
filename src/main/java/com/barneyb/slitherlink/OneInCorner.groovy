package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
class OneInCorner implements Strategy {

    Move nextMove(Puzzle p) {
        def corners = p.cornerEdgeMap()
        for (CellCoord cc : corners.keySet()) {
            def c = p.cell(cc)
            if (c == 1) {
                for (EdgeCoord ec : corners[cc]) {
                    if (p.edge(ec) == EdgeState.UNKNOWN) {
                        return new MoveImpl(ec, EdgeState.OFF)
                    }
                }
            }
        }
        return null
    }

}
