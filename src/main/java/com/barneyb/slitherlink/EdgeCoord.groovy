package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString
/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ["p"])
@ToString(includePackage = false)
class EdgeCoord {
    final int r
    final int c
    final Dir d
    @PackageScope
    transient Puzzle p
    @PackageScope
    EdgeCoord withPuzzle(Puzzle p) {
        this.p = p
        this
    }

    EdgeCoord(int r, int c, Dir d) {
        this.r = r
        this.c = c
        this.d = d
    }

    EdgeCoord(CellCoord cc, Dir d) {
        this(cc.r, cc.c, d)
    }

    EdgeCoord(DotCoord dc, Dir d) {
        this(dc.r, dc.c, d)
    }

    EdgeState state() {
        if (d == Dir.WEST) {
            p.verticalEdges[vertIndex()]
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[horizIndex()]
        } else {
            throw new IllegalArgumentException("non-canonical $this")
        }
    }

    private int vertIndex() {
        r * (p.cols + 1) + c
    }

    private int horizIndex() {
        r * p.cols + c
    }

    void state(EdgeState state) {
        def curr = this.state()
        if (curr == state) return
        if (curr != EdgeState.UNKNOWN) {
            throw new IllegalArgumentException("$this is $curr, you can't set it $state")
        }
        if (d == Dir.WEST) {
            p.verticalEdges[vertIndex()] = state
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[horizIndex()] = state
        } else {
            throw new IllegalArgumentException("non-canonical $this")
        }
    }
}
