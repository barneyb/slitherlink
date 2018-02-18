package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StaticStrategy

/**
 *
 * @author bboisvert
 */
class AdjacentThrees implements StaticStrategy {

    Move nextMove(Puzzle p) {
        def threes = p.clues()
        .findAll { cc, c ->
            c == 3
        }.keySet()
        for (CellCoord a : threes) {
            def edges = []
            def ticks = []
            def b = p.cellCoord(a.r - 1, a.c) // N
            if (threes.contains(b)) {
                edges << p.edgeCoord(a.r, a.c, Dir.SOUTH)
                edges << p.edgeCoord(a.r, a.c, Dir.NORTH)
                edges << p.edgeCoord(b.r, b.c, Dir.NORTH)

                if (a.c > 0) ticks << p.edgeCoord(a.r, a.c - 1, Dir.NORTH)
                if (a.c < p.cols - 1) ticks << p.edgeCoord(a.r, a.c + 1, Dir.NORTH)
            }
            b = p.cellCoord(a.r, a.c + 1) // E
            if (threes.contains(b)) {
                edges << p.edgeCoord(a.r, a.c, Dir.WEST)
                edges << p.edgeCoord(a.r, a.c, Dir.EAST)
                edges << p.edgeCoord(b.r, b.c, Dir.EAST)

                if (a.r > 0 ) ticks << p.edgeCoord(a.r - 1, a.c, Dir.EAST)
                if (a.r < p.rows - 1) ticks << p.edgeCoord(a.r + 1, a.c, Dir.EAST)
            }
            for (EdgeCoord ec : edges) {
                if (ec.state() != EdgeState.ON) {
                    return new MoveImpl(this, ec, EdgeState.ON)
                }
            }
            for (EdgeCoord ec : ticks) {
                if (ec.state() != EdgeState.OFF) {
                    return new MoveImpl(this, ec, EdgeState.OFF)
                }
            }
        }
        null
    }

}
