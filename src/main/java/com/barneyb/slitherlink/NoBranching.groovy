package com.barneyb.slitherlink

/**
 *
 * @author bboisvert
 */
class NoBranching implements Strategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def edges = p.edges(dc)
            if (edges.count { p.edge(it) == EdgeState.ON } == 2) {
                for (EdgeCoord ec : edges.findAll { p.edge(it) == EdgeState.UNKNOWN }) {
                    return new MoveImpl(ec, EdgeState.OFF)
                }
            }
        }
        null
    }

}
