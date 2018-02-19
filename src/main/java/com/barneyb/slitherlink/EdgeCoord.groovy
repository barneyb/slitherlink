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
    transient final Puzzle p

    protected EdgeCoord(Puzzle p, int r, int c, Dir d) {
        this(r, c, d)
        this.p = p
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

    EdgeState getState() {
        if (d == Dir.WEST) {
            p.verticalEdges[vertIndex()]
        } else if (d == Dir.NORTH) {
            p.horizontalEdges[horizIndex()]
        } else {
            throw new IllegalArgumentException("non-canonical $this")
        }
    }

    private int vertIndex() {
        if (r < 0 || r >= p.rows || c < 0 || c > p.cols) {
            throw new IllegalStateException("$this isn't on the board")
        }
        r * (p.cols + 1) + c
    }

    private int horizIndex() {
        if (r < 0 || r > p.rows || c < 0 || c >= p.cols) {
            throw new IllegalStateException("$this isn't on the board")
        }
        r * p.cols + c
    }

    void setState(EdgeState state) {
        def curr = this.state
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

    List<CellCoord> clueCells() {
        cells().findAll {
            ! it.blank
        }
    }

    List<CellCoord> cells() {
        p.cells(this)
    }

    List<DotCoord> dots() {
        p.dots(this)
    }

}
