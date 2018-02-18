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
        def threes = p.clues()
        .findAll { cc, c ->
            c == 3
        }.keySet()
        for (CellCoord cc : threes) {
            def dots = p.dots(cc)
            def edges = p.edges(cc)
            def ends = dots
            .findAll {
                // exactly one edge to the dot
                def singleEdged = p.edges(it)
                    .findAll {
                    p.edge(it) == EdgeState.ON
                }
                if (singleEdged.size() != 1) return false
                // the edge is from the outside
                return ! edges.contains(singleEdged.first())

            }
            for (dc in ends) {
                def ecs = [] as List<EdgeCoord>
                // this dependency on ordering is SUPER janky
                switch (dots.indexOf(dc)) {
                    case 0:
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.EAST)
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.SOUTH)
                        break
                    case 1:
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.SOUTH)
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.WEST)
                        break
                    case 2:
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.NORTH)
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.WEST)
                        break
                    case 3:
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.NORTH)
                        ecs << p.edgeCoord(cc.r, cc.c, Dir.EAST)
                        break
                    default:
                        throw new IllegalStateException("$cc has janky dots: $dots")
                }
                for (ec in ecs) {
                    if (ec.state(p) != EdgeState.ON) {
                        return new MoveImpl(this, ec, EdgeState.ON)
                    }
                }
            }
        }
        null
    }

}
