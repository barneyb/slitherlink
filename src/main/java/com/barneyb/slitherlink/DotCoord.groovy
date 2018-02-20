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
final class DotCoord {
    final int r
    final int c
    @PackageScope
    transient final Puzzle p

    /** I speak grid coordinates */
    protected DotCoord(Puzzle p, int r, int c) {
        this.r = r
        this.c = c
        this.p = p
        validate()
    }

    private void validate() {
        if (p != null) {
            if (r < 0 || r >= p.gridRows() || c < 0 || c >= p.gridCols()) {
                throw new IllegalStateException("$this isn't on the board")
            }
        }
        if (r % 2 == 1) {
            throw new IllegalArgumentException("$this isn't a valid dot (odd row)")
        }
        if (c % 2 == 1) {
            throw new IllegalArgumentException("$this isn't a valid dot (odd col)")
        }
    }
    /** I speak human coordinates */
    DotCoord(int r, int c) {
        this.r = r * 2
        this.c = c * 2
        validate()
    }

    @Override
    String toString() {
        new StringBuilder(getClass().simpleName)
            .append("(")
            .append(r)
            .append(", ")
            .append(c)
            .append(")")
            .toString()
    }

    boolean isTopRow() {
        r == 0
    }

    boolean isBottomRow() {
        r == p.gridRows() - 1
    }

    boolean isLeftCol() {
        c == 0
    }

    boolean isRightCol() {
        c == p.gridCols() - 1
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

    DotCoord dot(CellCoord cc) {
        return p.dotCoord(r + (cc.r - r) * 2, c + (cc.c - c) * 2)
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
            it.state == state
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
            it.state == EdgeState.ON
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
            if (outbound.size() != 2) {
                throw new IllegalArgumentException("branch at $curr")
            }
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
