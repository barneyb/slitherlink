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

    EdgeState state(Puzzle p___) {
        if (d == Dir.WEST) {
            p.verticalEdges[r * (p.cols + 1) + c]
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[r * p.cols + c]
        } else {
            throw new IllegalArgumentException("non-canonical $this")
        }
    }

    void state(Puzzle p, EdgeState state) {
        if (d == Dir.WEST) {
            p.verticalEdges[r * (p.cols + 1) + c] = state
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[r * p.cols + c] = state
        } else {
            throw new IllegalArgumentException("non-canonical $this")
        }
    }
}
