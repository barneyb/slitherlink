package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.DotCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle

/**
 *
 * @author bboisvert
 */
class NoBranching implements MultiMoveStrategy {

    List<Move> nextMoves(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def edges = dc.edges()
            if (edges.count { it.state == EdgeState.ON } == 2) {
                def ms = []
                for (EdgeCoord ec : edges) {
                    if (ec.state == EdgeState.UNKNOWN) {
                        ms << new MoveImpl(this, ec, EdgeState.OFF)
                    }
                }
                if (! ms.empty) return ms
            }
        }
        null
    }

}
