package com.barneyb.slitherlink

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString

import static com.barneyb.slitherlink.Dir.*
/**
 * These match the coord of the cell to their southeast.
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ["p"])
@ToString(includePackage = false)
class DotCoord {
    final int r
    final int c
    @PackageScope
    transient final Puzzle p

    protected DotCoord(Puzzle p, int r, int c) {
        this(r, c)
        this.p = p
    }

    DotCoord(int r, int c) {
        this.r = r
        this.c = c
    }

    boolean isTopRow() {
        r == 0
    }

    boolean isBottomRow() {
        r == p.rows
    }

    boolean isLeftCol() {
        c == 0
    }

    boolean isRightCol() {
        c == p.cols
    }

    boolean adjacent(DotCoord dc) {
        (Math.abs(dc.r - r) + Math.abs(dc.c - c)) == 1
    }

    Dir dir(DotCoord dc) {
        if (! adjacent(dc)) {
            throw new IllegalArgumentException("$dc isn't next to $this")
        }
        if (dc.r == r - 1) {
            return NORTH
        }
        if (dc.r == r + 1) {
            return SOUTH
        }
        if (dc.c == c + 1) {
            return EAST
        }
        if (dc.c == c - 1) {
            return WEST
        }
    }

    EdgeCoord edge(Dir d) {
        if (d == NORTH && ! topRow) {
            return toCell().cell(NORTH).edge(WEST)
        }
        if (d == EAST && ! rightCol) {
            return toCell().edge(NORTH)
        }
        if (d == SOUTH && ! bottomRow) {
            return toCell().edge(WEST)
        }
        if (d == WEST && ! leftCol) {
            return toCell().cell(WEST).edge(NORTH)
        }
        throw new IllegalArgumentException("There's no edge $d of $this")
    }

    DotCoord dot(Dir d) {
        if (d == NORTH && ! topRow) {
            return p.dotCoord(r - 1, c)
        }
        if (d == EAST && ! rightCol) {
            return p.dotCoord(r, c + 1)
        }
        if (d == SOUTH && ! bottomRow) {
            return p.dotCoord(r + 1, c)
        }
        if (d == WEST && ! leftCol) {
            return p.dotCoord(r, c - 1)
        }
        throw new IllegalArgumentException("There's no dot $d of $this")
    }

    CellCoord toCell() {
        p.cellCoord(r, c)
    }

    List<EdgeCoord> edges() {
        p.edges(this)
    }

    List<EdgeCoord> edges(EdgeState state) {
        p.edges(this).findAll {
            it.state() == state
        }
    }

    List<EdgeCoord> externalEdges(CellCoord cc) {
        edges() - cc.edges()
    }

    List<EdgeCoord> internalEdges(CellCoord cc) {
        edges().intersect(cc.edges())
    }

    DotCoord findOtherEnd() {
        def outbound = edges()
            .findAll {
            it.state() == EdgeState.ON
        }
        if (outbound.size() != 1) {
            throw new IllegalStateException("$this has ${outbound.size()} outbound edges")
        }
        def dots = outbound.first().dots()
        findOtherEndHelper(
            dots.find { it != this },
            this
        ).otherEnd
    }

    @Canonical
    @ToString(includePackage = false)
    @PackageScope
    static class FindOtherEndStats {
        final DotCoord otherEnd
        final int edges
    }

    @PackageScope
    static FindOtherEndStats findOtherEndHelper(DotCoord curr, DotCoord prev, DotCoord initial = null) {
        for (int i = 0;; i++) {
            def outbound = curr.edges(EdgeState.ON)
            if (outbound.size() == 1 || curr == initial) {
                return new FindOtherEndStats(curr, i)
            }
            assert outbound.size() == 2
            def itr = outbound.iterator()
            def ds = itr.next().dots() + itr.next().dots()
            def nexts = ds.findAll {
                it != curr && it != prev
            }
            assert nexts.size() == 1
            prev = curr
            curr = nexts.first()
        }
    }

}
