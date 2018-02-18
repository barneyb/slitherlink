package com.barneyb.slitherlink.strat

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
class NeedAllRemaining implements Strategy {

    Move nextMove(Puzzle p) {
        for (entry in p.clues()) {
            def cc = entry.key
            def clue = entry.value
            def edges = p.edges(cc)
            if (edges.collect { p.edge(it) }.count { it == EdgeState.ON || it == EdgeState.UNKNOWN } == clue) {
                for (EdgeCoord ec : edges) {
                    if (ec.state(p) != EdgeState.ON) {
                        return new MoveImpl(this, ec, EdgeState.ON)
                    }
                }
            }
        }
        null
    }

}
