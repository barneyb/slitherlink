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
            def b = new CellCoord(a.r - 1, a.c + 1) // NE
            if (threes.contains(b)) {
                edges << new EdgeCoord(a.r, a.c, Dir.WEST)
                edges << new EdgeCoord(a.r, a.c, Dir.SOUTH)
                edges << new EdgeCoord(b.r, b.c, Dir.NORTH)
                edges << new EdgeCoord(b.r, b.c, Dir.EAST)
            }
            b = new CellCoord(a.r + 1, a.c + 1) // SE
            if (threes.contains(b)) {
                edges << new EdgeCoord(a.r, a.c, Dir.NORTH)
                edges << new EdgeCoord(a.r, a.c, Dir.WEST)
                edges << new EdgeCoord(b.r, b.c, Dir.EAST)
                edges << new EdgeCoord(b.r, b.c, Dir.SOUTH)
            }
            for (EdgeCoord ec : edges) {
                switch (p.edge(ec)) {
                    case EdgeState.UNKNOWN:
                        return new MoveImpl(ec, EdgeState.ON)
                    case EdgeState.ON:
                        break // perfect!
                    case EdgeState.OFF:
                        throw new IllegalStateException("Edge $ec should be on, but it's off")
                }
            }
        }
        null
    }

}
