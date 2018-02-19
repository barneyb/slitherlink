package com.barneyb.slitherlink

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import groovy.transform.ToString

import static com.barneyb.slitherlink.Dir.*
/**
 *
 *
 * @author barneyb
 */
@EqualsAndHashCode(excludes = ["p"])
@ToString(includePackage = false)
class CellCoord {
    final int r
    final int c
    @PackageScope
    transient final Puzzle p

    protected CellCoord(Puzzle p, int r, int c) {
        this(r, c)
        this.p = p
    }

    CellCoord(int r, int c) {
        this.r = r
        this.c = c
    }

    boolean isTopRow() {
        r == 0
    }

    boolean isBottomRow() {
        r == p.rows - 1
    }

    boolean isLeftCol() {
        c == 0
    }

    boolean isRightCol() {
        c == p.cols - 1
    }

    CellCoord cell(Dir d) {
        if (d == NORTH && ! topRow) {
            return p.cellCoord(r - 1, c)
        }
        if (d == EAST && ! rightCol) {
            return p.cellCoord(r, c + 1)
        }
        if (d == SOUTH && ! bottomRow) {
            return p.cellCoord(r + 1, c)
        }
        if (d == WEST && ! leftCol) {
            return p.cellCoord(r, c - 1)
        }
        throw new IllegalArgumentException("There's no cell $d of $this")
    }

    EdgeCoord edge(Dir d) {
        p.edgeCoord(r, c, d)
    }

    boolean isBlank() {
        clue == Puzzle.BLANK
    }

    int getClue() {
        p.cells[index()]
    }

    private int index() {
        if (r < 0 || r >= p.rows || c < 0 || c >= p.cols) {
            throw new IllegalStateException("$this isn't on the board")
        }
        r * p.cols + c
    }

    void setClue(int clue) {
        if (! blank) {
            throw new IllegalArgumentException("Cell at row $r col $c is already set to $clue")
        }
        p.cells[index()] = clue
    }

    EdgeCoord toEdge(Dir d) {
        p.edgeCoord(r, c, d)
    }

    List<EdgeCoord> edges() {
        p.edges(this)
    }

    List<EdgeCoord> edges(EdgeState state) {
        p.edges(this).findAll {
            it.state == state
        }
    }

    List<DotCoord> dots() {
        p.dots(this)
    }
}
