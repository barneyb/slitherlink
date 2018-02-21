package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.DotCoord
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MultiMoveStrategy
import com.barneyb.slitherlink.Puzzle
import groovy.transform.Canonical
import groovy.transform.ToString
/**
 *
 * @author bboisvert
 */
class ForcedToOne implements MultiMoveStrategy {

    List<Move> nextMoves(Puzzle p) {
        def ms = null
        for (f in getForcedToList(p, 1)) {
            def dc = f.dot
            def cc = f.cell
            def internalEdges = dc.internalEdges(cc)
            if (internalEdges.every { it.state == Puzzle.UNKNOWN }) {
                // not yet connected
                ms = Utils.edges(ms, cc.edges().minus(internalEdges), Puzzle.OFF)
            }
        }
        ms
    }

    static List<ForcedTo> getForcedToList(Puzzle p, int clue) {
        def forces = []
        for (cc in p.clueCells(clue)) {
            for (dc in cc.dots()) {
                def externalEdges = dc.externalEdges(cc)
                if (externalEdges.count { it.state == Puzzle.ON } == 1
                    && externalEdges.count {
                    it.state == Puzzle.UNKNOWN
                } == 0
                ) {
                    // one edge to it
                    forces << new ForcedTo(dc, cc)
                }
            }
        }
        forces
    }

    @Canonical
    @ToString(includePackage = false)
    static class ForcedTo {
        /** the dot that was forced at */
        DotCoord dot
        /** the cell being forced to */
        CellCoord cell
    }

}
