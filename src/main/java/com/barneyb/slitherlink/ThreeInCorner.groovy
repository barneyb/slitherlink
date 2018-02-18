package com.barneyb.slitherlink
/**
 *
 *
 * @author barneyb
 */
class ThreeInCorner implements Strategy, Idempotent {

    Move nextMove(Puzzle p) {
        def corners = p.cornerEdgeMap()
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

}
