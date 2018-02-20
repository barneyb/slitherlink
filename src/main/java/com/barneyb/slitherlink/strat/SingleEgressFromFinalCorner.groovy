package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SingleMoveStrategy
/**
 *
 *
 * @author barneyb
 */
class SingleEgressFromFinalCorner implements SingleMoveStrategy {

    @Override
    Move nextMove(Puzzle p) {
        for (it in p.clueCells()) {
            def unknown = it.edges(EdgeState.UNKNOWN)
            def on = it.edges(EdgeState.ON)
            if (unknown.size() == 2 && on.size() == it.clue - 1) {
                // one away from satisfied, and two unknowns
                def dots = unknown.first().dots().intersect(unknown.last().dots())
                if (dots.size() == 1) {
                    // unknowns make a corner!
                    def edges = dots.first()
                        .externalEdges(it)
                        .findAll {
                            it.state == EdgeState.UNKNOWN
                        }
                    if (edges.size() == 1) {
                        // single egress
                        return new MoveImpl(this, edges.first(), EdgeState.ON)
                    }
                }
            }
        }
        null
    }

}
