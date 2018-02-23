package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl

/**
 *
 *
 * @author barneyb
 */
final class Utils {

    private Utils() {}

    static List<Move> edges(Collection<EdgeCoord> edges, int state) {
        edgesUnless(edges, state, state)
    }

    static List<Move> edges(List<Move> ms, Collection<EdgeCoord> edges, int state) {
        edgesUnless(ms, edges, state, state)
    }

    static List<Move> edgesIf(Collection<EdgeCoord> edges, int state, int check) {
        edgesIf(null, edges, state, check)
    }

    static List<Move> edgesIf(List<Move> ms, Collection<EdgeCoord> edges, int state, int check) {
        for (e in edges) {
            if (e.state == check) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(e, state))
            }
        }
        ms
    }

    static List<Move> edgesUnless(Collection<EdgeCoord> edges, int state, int check) {
        edgesUnless(null, edges, state, check)
    }

    static List<Move> edgesUnless(List<Move> ms, Collection<EdgeCoord> edges, int state, int check) {
        for (e in edges) {
            if (e.state != check) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(e, state))
            }
        }
        ms
    }

}
