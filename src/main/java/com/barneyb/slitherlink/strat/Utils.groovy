package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.EdgeCoord
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.MoveImpl
import groovy.transform.CompileStatic

/**
 *
 *
 * @author barneyb
 */
@CompileStatic
final class Utils {

    private Utils() {}

    static List<Move> edges(Collection<EdgeCoord> edges, EdgeState state) {
        edgesUnless(edges, state, state)
    }

    static List<Move> edges(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state) {
        edgesUnless(ms, edges, state, state)
    }

    static List<Move> edgesIf(Collection<EdgeCoord> edges, EdgeState state, EdgeState check) {
        edgesIf(null, edges, state, check)
    }

    static List<Move> edgesIf(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state, EdgeState check) {
        for (e in edges) {
            if (e.state == check) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(this, e, state))
            }
        }
        ms
    }

    static List<Move> edgesUnless(Collection<EdgeCoord> edges, EdgeState state, EdgeState check) {
        edgesUnless(null, edges, state, check)
    }

    static List<Move> edgesUnless(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state, EdgeState check) {
        for (e in edges) {
            if (e.state != check) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(this, e, state))
            }
        }
        ms
    }

}
