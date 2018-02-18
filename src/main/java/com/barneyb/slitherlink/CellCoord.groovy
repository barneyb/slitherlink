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
    transient Puzzle p
    @PackageScope
    CellCoord withPuzzle(Puzzle p) {
        this.p = p
        this
    }

    CellCoord(int r, int c) {
        this.r = r
        this.c = c
    }

    int clue(Puzzle p) {
        p.cells[r * p.cols + c]
    }

    void clue(Puzzle p, int clue) {
        def curr = this.clue(p)
        if (curr != Puzzle.BLANK) {
            throw new IllegalArgumentException("Cell at row $r col $c is already set to $curr")
        }
        p.cells[r * p.cols + c] = clue
    }

    EdgeCoord toEdge(Dir d) {
        new EdgeCoord(this, d)
            .withPuzzle(p)
            .canonical()
    }

}
