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
 *
 * @author barneyb
 */
class SingleEgress implements Strategy {

    Move nextMove(Puzzle p) {
        for (DotCoord dc : p.dots()) {
            def map = p
                .edges(dc)
                .collectEntries({
                    [it, p.edge(it)]
                })
            def unknownCount = map.count { ec, s ->
                s == EdgeState.UNKNOWN
            }
            def onCount = map .count { ec, s ->
                s == EdgeState.ON
            }
            if (unknownCount == 1 && onCount == 1) {
                return new MoveImpl(this, map.find { ec, s ->
                    s == EdgeState.UNKNOWN
                }.key as EdgeCoord, EdgeState.ON)
            }
        }
        return null;
    }

}
