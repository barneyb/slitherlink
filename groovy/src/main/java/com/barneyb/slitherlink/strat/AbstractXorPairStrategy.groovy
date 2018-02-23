package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.CellCoord
import com.barneyb.slitherlink.DotCoord
import com.barneyb.slitherlink.Puzzle
import groovy.transform.Canonical
import groovy.transform.ToString

/**
 *
 *
 * @author barneyb
 */
abstract class AbstractXorPairStrategy {

    @Canonical
    @ToString(includePackage = false)
    protected static class XorEdgePair {
        /** the cell who owns the pair */
        CellCoord cell
        /** the dot at the apex of the corner */
        DotCoord dot
    }

    protected List<XorEdgePair> getXorEdgePairs(Puzzle p) {
        def fcs = new HashSet<XorEdgePair>()
        // clue cells one short of clue satisfaction with two unknown edges
        // that form a corner (share a dot).
        for (cc in p.clueCells()) {
            def unknown = cc.edges(Puzzle.UNKNOWN)
            def on = cc.edges(Puzzle.ON)
            if (unknown.size() == 2 && on.size() == cc.clue - 1) {
                // one away from satisfied, and two unknowns
                def dots = unknown.first().dots().intersect(unknown.last().dots())
                if (dots.size() == 1) {
                    // unknowns make a corner!
                    def cell = cc
                    def dot = dots.first()
                    while (true) {
                        fcs << new XorEdgePair(cell, dot)
                        if (!cell.hasCell(dot)) {
                            break
                        }
                        cell = cell.cell(dot)
                        if (cell.clue != 2) {
                            break
                        }
                        dot = dot.dot(cell)
                    }
                }
            }
        }

        // 2-clue cells with an end forced onto them
        for (f in ForcedToOne.getForcedToList(p, 2)) {
            def dc = f.dot
            def cc = f.cell
            fcs << new XorEdgePair(cc, dc)
        }

        // propagate each final corner along chains of twos
        def propFcs = new HashSet<XorEdgePair>(fcs)
        while (!fcs.empty) {
            fcs = fcs.collect {
                def ps = []
                if (it.cell.clue == 2) {
                    def otherDot = it.dot.dot(it.cell)
                    ps << new XorEdgePair(it.cell, otherDot)
                    if (it.cell.hasCell(otherDot)) {
                        ps << new XorEdgePair(it.cell.cell(otherDot), otherDot)
                    }
                }
                ps
            }
            .flatten()
                .findAll {
                propFcs.add(it)
            }

        }
        propFcs.toList()
    }

}
