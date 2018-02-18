package com.barneyb.slitherlink

/**
 *
 * @author bboisvert
 */
class ClueSatisfied implements Strategy {

    Move nextMove(Puzzle p) {
        for (entry in p.clues()) {
            def cc = entry.key
            def clue = entry.value
            def edges = p.edges(cc)
            if (edges.count { p.edge(it) == EdgeState.ON } == clue) {
                for (EdgeCoord ec : edges.findAll { p.edge(it) == EdgeState.UNKNOWN }) {
                    return new MoveImpl(ec, EdgeState.OFF)
                }
            }
        }
        null
    }

}
