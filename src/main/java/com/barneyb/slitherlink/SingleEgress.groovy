package com.barneyb.slitherlink

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
                return new MoveImpl(map.find { ec, s ->
                    s == EdgeState.UNKNOWN
                }.key as EdgeCoord, EdgeState.ON)
            }
        }
        return null;
    }

}
