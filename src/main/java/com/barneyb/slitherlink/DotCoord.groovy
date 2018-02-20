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

    /** I speak human coordinates */
    DotCoord(int humanRow, int humanCol) {
        this.r = humanRow * 2
        this.c = humanCol * 2
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

    boolean adjacent(DotCoord dot) {
        def dr = Math.abs(dot.r - r)
        def dc = Math.abs(dot.c - c)
        if (dr == 2 && dc == 0) return true
        if (dr == 0 && dc == 2) return true
        false
    }

    Dir dir(DotCoord dc) {
        if (! adjacent(dc)) {
            throw new IllegalArgumentException("$dc isn't next to $this")
        }
        if (dc.r == r - 2) {
            return NORTH
        }
        if (dc.r == r + 2) {
            return SOUTH
        }
        if (dc.c == c + 2) {
            return EAST
        }
        if (dc.c == c - 2) {
            return WEST
        }
    }

    EdgeCoord edge(Dir d) {
        if (d == NORTH && ! topRow) {
            return p.edgeCoord(r - 1, c)
        }
        if (d == EAST && ! rightCol) {
            return p.edgeCoord(r, c + 1)
        }
        if (d == SOUTH && ! bottomRow) {
            return p.edgeCoord(r + 1, c)
        }
        if (d == WEST && ! leftCol) {
            return p.edgeCoord(r, c - 1)
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
