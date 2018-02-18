package com.barneyb.slitherlink.strat

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
                def e = s.findOtherEnd()
                endMap.put(e, s)
            }
        }

        for (entry in endMap) {
            DotCoord s = entry.key
            DotCoord e = entry.value
            // if they're one edge from closing the loop
            EdgeCoord ec
            if (s.adjacent(e)) {
                ec = s.edge(s.dir(e))
            } else {
                continue
            }
            if (ec.state() != EdgeState.UNKNOWN) {
                continue
            }
            if (endMap.size() == 1) {
                // get the cells along the edge
                def cs = ec.clueCells()
                // see if all cells are satisfied excepting along the edge
                def unsatisfied = p.clueCells().findAll {
                    it.clue() != it.edges(EdgeState.ON).size()
                }
                if (unsatisfied.size() == cs.size() && unsatisfied.containsAll(cs)) {
                    // turn edge on (to win)
                    return new MoveImpl(this, ec, EdgeState.ON)
                }
            }
            // turn edge off (incomplete loop)
            return new MoveImpl(this, ec, EdgeState.OFF)
        }
        null
    }
}
