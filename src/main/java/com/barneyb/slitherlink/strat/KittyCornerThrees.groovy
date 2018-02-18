package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.ClueOnly
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
class KittyCornerThrees implements Strategy, ClueOnly {

    Move nextMove(Puzzle p) {
        def threes = p.clues()
        .findAll { cc, c ->
            c == 3
        }.keySet()
        for (CellCoord a : threes) {
            def edges = []
            def b = p.cellCoord(a.r - 1, a.c + 1) // NE
            if (threes.contains(b)) {
                edges << p.edgeCoord(a.r, a.c, Dir.WEST)
                edges << p.edgeCoord(a.r, a.c, Dir.SOUTH)
                edges << p.edgeCoord(b.r, b.c, Dir.NORTH)
                edges << p.edgeCoord(b.r, b.c, Dir.EAST)
            }
            b = p.cellCoord(a.r + 1, a.c + 1) // SE
            if (threes.contains(b)) {
                edges << p.edgeCoord(a.r, a.c, Dir.NORTH)
                edges << p.edgeCoord(a.r, a.c, Dir.WEST)
                edges << p.edgeCoord(b.r, b.c, Dir.EAST)
                edges << p.edgeCoord(b.r, b.c, Dir.SOUTH)
            }
            for (EdgeCoord ec : edges) {
                if (ec.state() != EdgeState.ON) {
                    return new MoveImpl(this, ec, EdgeState.ON)
                }
            }
        }
        null
    }

}
