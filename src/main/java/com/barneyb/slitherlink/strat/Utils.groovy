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
        Utils.edges(null, edges, state)
    }

    static List<Move> edges(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state) {
        for (e in edges) {
            if (e.state != state) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(this, e, state))
            }
        }
        ms
    }

    static List<Move> edgesOffIf(Collection<EdgeCoord> edges, EdgeState state) {
        edgesOffIf(null, edges, state)
    }

    static List<Move> edgesOffIf(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state) {
        for (e in edges) {
            if (e.state == state) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(this, e, EdgeState.OFF))
            }
        }
        ms
    }

    static List<Move> edgesOffUnless(Collection<EdgeCoord> edges, EdgeState state) {
        edgesOffUnless(null, edges, state)
    }

    static List<Move> edgesOffUnless(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state) {
        for (e in edges) {
            if (e.state != state) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(this, e, EdgeState.OFF))
            }
        }
        ms
    }

    static List<Move> edgesOnIf(Collection<EdgeCoord> edges, EdgeState state) {
        edgesOnIf(null, edges, state)
    }

    static List<Move> edgesOnIf(List<Move> ms, Collection<EdgeCoord> edges, EdgeState state) {
        for (e in edges) {
            if (e.state == state) {
                if (ms == null) {
                    ms = []
                }
                ms.add(new MoveImpl(this, e, EdgeState.ON))
            }
        }
        ms
    }
}
