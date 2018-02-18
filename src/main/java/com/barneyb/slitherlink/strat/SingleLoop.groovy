package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.DotCoord
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
class SingleLoop implements Strategy {

    Move nextMove(Puzzle p) {
        def endMap = [:]
        // find an end
        p.ends()
        // find the other end
        .each{ s ->
            if (! endMap.containsKey(s)) {
                def e = p.findOtherEnd(s)
                endMap.put(e, s)
            }
        }

        for (entry in endMap) {
            DotCoord s = entry.key
            DotCoord e = entry.value
            // if they're one edge from closing the loop
            EdgeCoord ec
            if (s.r == e.r && s.c == e.c + 1) {
                ec = e.toCell().toEdge(Dir.NORTH)
            } else if (s.r == e.r && s.c == e.c - 1) {
                ec = s.toCell().toEdge(Dir.NORTH)
            } else if (s.r == e.r + 1 && s.c == e.c) {
                ec = e.toCell().toEdge(Dir.WEST)
            } else if (s.r == e.r - 1 && s.c == e.c) {
                ec = s.toCell().toEdge(Dir.WEST)
            } else {
                continue
            }
            if (p.edge(ec) != EdgeState.UNKNOWN) {
                continue
            }
            if (endMap.size() == 1) {
                // get the cells along the edge
                def cs = p.clues(ec).keySet()
                // see if all cells are satisfied excepting along the edge
                def unsatisfied = p.clues()
                .findAll { cc, c ->
                    c != p.edges(cc)
                    .collect {
                        p.edge(it)
                    }
                    .findAll {
                        it == EdgeState.ON
                    }
                    .size()
                }
                if (unsatisfied.size() == cs.size() && unsatisfied.keySet().containsAll(cs)) {
                    // turn edge on (to win)
                    return new MoveImpl(ec, EdgeState.ON)
                }
            }
            // turn edge off (incomplete loop)
            return new MoveImpl(ec, EdgeState.OFF)
        }
        null
    }
}
