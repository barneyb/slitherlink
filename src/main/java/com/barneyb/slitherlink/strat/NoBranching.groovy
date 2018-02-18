package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.DotCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy

/**
 *
 * @author bboisvert
 */
class NoBranching implements Strategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def edges = p.edges(dc)
            if (edges.count { p.edge(it) == EdgeState.ON } == 2) {
                for (EdgeCoord ec : edges) {
                    if (p.edge(ec) != EdgeState.OFF) {
                        return new MoveImpl(this, ec, EdgeState.OFF)
                    }
                }
            }
        }
        null
    }

}
