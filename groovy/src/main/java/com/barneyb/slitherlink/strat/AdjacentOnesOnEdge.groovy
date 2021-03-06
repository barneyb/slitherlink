package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SingleMoveStrategy
import com.barneyb.slitherlink.StaticStrategy

import static com.barneyb.slitherlink.Dir.*

/**
 *
 *
 * @author barneyb
 */
class AdjacentOnesOnEdge implements SingleMoveStrategy, StaticStrategy {

    @Override
    Move nextMove(Puzzle p) {
        def ones = p.clueCells(1)
        def edges = (ones.findAll {
            (it.topRow || it.bottomRow) && !it.rightCol && it.cell(EAST).clue == 1
        }.collect {
            it.edge(EAST)
        } + ones.findAll {
            (it.leftCol || it.rightCol) && !it.bottomRow && it.cell(SOUTH).clue == 1
        }.collect {
            it.edge(SOUTH)
        })
            .findAll {
            it.state != Puzzle.OFF
        }
        for (ec in edges) {
            return new MoveImpl(ec, Puzzle.OFF)
        }
        null
    }

}
