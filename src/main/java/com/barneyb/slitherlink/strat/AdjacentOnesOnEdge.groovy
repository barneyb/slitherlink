package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.Strategy

import static com.barneyb.slitherlink.Dir.*
/**
 *
 *
 * @author barneyb
 */
class AdjacentOnesOnEdge implements Strategy {

    @Override
    Move nextMove(Puzzle p) {
        def ones = p.clueCells(1)
        def edges = (ones.findAll {
            (it.topRow || it.bottomRow) && ! it.rightCol && it.cell(EAST).clue == 1
        }.collect {
            it.edge(EAST)
        } + ones.findAll {
            (it.leftCol || it.rightCol) && ! it.bottomRow && it.cell(SOUTH).clue == 1
        }.collect {
            it.edge(SOUTH)
        })
        .findAll {
            it.state != EdgeState.OFF
        }
        for (ec in edges) {
            return new MoveImpl(this, ec, EdgeState.OFF)
        }
        null
    }

}
