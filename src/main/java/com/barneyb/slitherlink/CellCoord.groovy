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
        if (d == Dir.NORTH && ! topRow) {
            return p.cellCoord(r - 1, c)
        }
        if (d == Dir.EAST && ! rightCol) {
            return p.cellCoord(r, c + 1)
        }
        if (d == Dir.SOUTH && ! bottomRow) {
            return p.cellCoord(r + 1, c)
        }
        if (d == Dir.WEST && ! leftCol) {
            return p.cellCoord(r, c - 1)
        }
        throw new IllegalArgumentException("There's no cell $d of $this")
    }

    int clue() {
        p.cells[index()]
    }

    private int index() {
        r * p.cols + c
    }

    void clue(int clue) {
        def curr = this.clue()
        if (curr != Puzzle.BLANK) {
            throw new IllegalArgumentException("Cell at row $r col $c is already set to $curr")
        }
        p.cells[index()] = clue
    }

    EdgeCoord toEdge(Dir d) {
        p.edgeCoord(r, c, d)
    }

    List<EdgeCoord> edges() {
        p.edges(this)
    }
}
