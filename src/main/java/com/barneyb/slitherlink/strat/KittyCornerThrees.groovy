package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StaticStrategy

import static com.barneyb.slitherlink.Dir.*
/**
 *
 * @author bboisvert
 */
class KittyCornerThrees implements StaticStrategy {

    Move nextMove(Puzzle p) {
        def threes = p.clueCells(3)
        for (CellCoord a : threes) {
            def edges = []
            if (! a.topRow && ! a.rightCol) {
                def b = a.cell(NORTH).cell(EAST)
                if (threes.contains(b)) {
                    edges << a.edge(WEST)
                    edges << a.edge(SOUTH)
                    edges << b.edge(NORTH)
                    edges << b.edge(EAST)
                }
            }
            if (! a.bottomRow && ! a.rightCol) {
                def b = a.cell(SOUTH).cell(EAST)
                if (threes.contains(b)) {
                    edges << a.edge(NORTH)
                    edges << a.edge(WEST)
                    edges << b.edge(EAST)
                    edges << b.edge(SOUTH)
                }
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
