package com.barneyb.slitherlink

/**
 *
 * @author bboisvert
 */
class SingleLoop implements Strategy {

    Move nextMove(Puzzle p) {
        def endMap = [:]
        // find an end
        p.dots()
        .findAll {
            1 == p.edges(it)
            .count {
                p.edge(it) == EdgeState.ON
            }
        }
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
                ec = new EdgeCoord(e.r, e.c, Dir.NORTH)
            } else if (s.r == e.r && s.c == e.c - 1) {
                ec = new EdgeCoord(s.r, s.c, Dir.NORTH)
            } else if (s.r == e.r + 1 && s.c == e.c) {
                ec = new EdgeCoord(e.r, e.c, Dir.WEST)
            } else if (s.r == e.r - 1 && s.c == e.c) {
                ec = new EdgeCoord(s.r, s.c, Dir.WEST)
            } else {
                continue
            }
            if (p.edge(ec) != EdgeState.UNKNOWN) {
                continue
            }
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
            } else {
                // turn edge off (incomplete loop)
                return new MoveImpl(ec, EdgeState.OFF)
            }
        }
        null
    }
}
