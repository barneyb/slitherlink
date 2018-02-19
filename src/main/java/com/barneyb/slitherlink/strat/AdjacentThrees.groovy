package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StaticStrategy

import static com.barneyb.slitherlink.Dir.*
/**
 *
 * @author bboisvert
 */
class AdjacentThrees implements MultiMoveStrategy, StaticStrategy {

    List<Move> nextMoves(Puzzle p) {
        def threes = p.clueCells(3)
        for (CellCoord a : threes) {
            def edges = [] as List<EdgeCoord>
            def ticks = [] as List<EdgeCoord>
            if  (! a.topRow) {
                def b = a.cell(NORTH)
                if (threes.contains(b)) {
                    edges << a.edge(SOUTH)
                    edges << a.edge(NORTH)
                    edges << b.edge(NORTH)

                    if (! a.leftCol) ticks << a.cell(WEST).edge(NORTH)
                    if (! a.rightCol) ticks << a.cell(EAST).edge(NORTH)
                }
            }
            if (! a.rightCol) {
                def b = a.cell(EAST)
                if (threes.contains(b)) {
                    edges << a.edge(WEST)
                    edges << a.edge(EAST)
                    edges << b.edge(EAST)

                    if (! a.topRow) ticks << a.cell(NORTH).edge(EAST)
                    if (! a.bottomRow) ticks << a.cell(SOUTH).edge(EAST)
                }
            }
            def ms = []
            for (EdgeCoord ec : edges) {
                if (ec.state != EdgeState.ON) {
                    ms << new MoveImpl(this, ec, EdgeState.ON)
                }
            }
            for (EdgeCoord ec : ticks) {
                if (ec.state != EdgeState.OFF) {
                    ms << new MoveImpl(this, ec, EdgeState.OFF)
                }
            }
            if (! ms.empty) {
                return ms
            }
        }
        null
    }

}
