package com.barneyb.slitherlink

/**
 *
 * @author bboisvert
 */
class NeedAllRemaining implements Strategy {

    Move nextMove(Puzzle p) {
        for (entry in p.clues()) {
            def cc = entry.key
            def clue = entry.value
            def edges = p.edges(cc)
            if (edges.collect { p.edge(it) }.count { it == EdgeState.ON || it == EdgeState.UNKNOWN } == clue) {
                for (EdgeCoord ec : edges.findAll { p.edge(it) == EdgeState.UNKNOWN }) {
                    return new MoveImpl(ec, EdgeState.ON)
                }
            }
        }
        null
    }

}
