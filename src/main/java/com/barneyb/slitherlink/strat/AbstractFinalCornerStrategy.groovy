package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.DotCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Puzzle
import groovy.transform.ToString
import groovy.transform.TupleConstructor
/**
 *
 *
 * @author barneyb
 */
abstract class AbstractFinalCornerStrategy {

    @TupleConstructor
    @ToString(includePackage = false)
    protected static class FinalCorner {
        CellCoord cell
        DotCoord dot
    }

    protected List<FinalCorner> getFinalCorners(Puzzle p) {
        def fcs = []
        for (cc in p.clueCells()) {
            def unknown = cc.edges(EdgeState.UNKNOWN)
            def on = cc.edges(EdgeState.ON)
            if (unknown.size() == 2 && on.size() == cc.clue - 1) {
                // one away from satisfied, and two unknowns
                def dots = unknown.first().dots().intersect(unknown.last().dots())
                if (dots.size() == 1) {
                    // unknowns make a corner!
                    fcs << new FinalCorner(cc, dots.first())
                }
            }
        }
        fcs
    }

}
