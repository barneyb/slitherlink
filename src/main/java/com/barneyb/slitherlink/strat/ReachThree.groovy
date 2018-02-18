package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.Dir
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
class ReachThree implements Strategy {

    Move nextMove(Puzzle p) {
        def threes = p.clueCells(3)
        for (CellCoord cc : threes) {
            def edges = cc.edges()
            def dots = cc.dots()
            def ends = dots
            .findAll {
                // exactly one edge to the dot
                def singleEdged = it.edges(EdgeState.ON)
                if (singleEdged.size() != 1) return false
                // the edge is from the outside
                return ! edges.contains(singleEdged.first())

            }
            for (dc in ends) {
                def ecs = [] as List<EdgeCoord>
                // this dependency on ordering is SUPER janky
                switch (dots.indexOf(dc)) {
                    case 0:
                        ecs << cc.edge(Dir.EAST)
                        ecs << cc.edge(Dir.SOUTH)
                        break
                    case 1:
                        ecs << cc.edge(Dir.SOUTH)
                        ecs << cc.edge(Dir.WEST)
                        break
                    case 2:
                        ecs << cc.edge(Dir.NORTH)
                        ecs << cc.edge(Dir.WEST)
                        break
                    case 3:
                        ecs << cc.edge(Dir.NORTH)
                        ecs << cc.edge(Dir.EAST)
                        break
                    default:
                        throw new IllegalStateException("$cc has janky dots: $dots")
                }
                for (ec in ecs) {
                    if (ec.state() != EdgeState.ON) {
                        return new MoveImpl(this, ec, EdgeState.ON)
                    }
                }
            }
        }
        null
    }

}
