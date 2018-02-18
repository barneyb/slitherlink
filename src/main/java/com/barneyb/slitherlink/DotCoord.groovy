package com.barneyb.slitherlink

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

}
