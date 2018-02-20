package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
/**
 *
 * @author bboisvert
 */
class TouchOppositeCornersOfTwo implements MultiMoveStrategy {

    @Override
    List<Move> nextMoves(Puzzle p) {
        for (cc in p.clueCells(2)) {
            def dots = cc.dots().findAll {
                it.externalEdges(cc)*.state.sort() in [
                    [EdgeState.ON],
                    [EdgeState.UNKNOWN, EdgeState.ON],
                    [EdgeState.ON, EdgeState.OFF],
                ]
            }
            if (dots.size() >= 2) {
                def ms = null
                dots.permutations().findAll {
                    it.size() == 2
                }.findAll {
                    def a = it.first()
                    def b = it.last()
                    a.r != b.r && a.c != b.c
                }.flatten()
                .unique(true)
                .each {
                    ms = Utils.edgesIf(ms, it.externalEdges(cc), EdgeState.OFF, EdgeState.UNKNOWN)
                }
                if (ms) return ms
            }
        }
        null
    }

}
