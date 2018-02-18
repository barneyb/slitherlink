package com.barneyb.slitherlink

import groovy.transform.Immutable
import groovy.transform.ToString

/**
 *
 *
 * @author barneyb
 */
@Immutable
@ToString(includePackage = false)
class CellCoord {
    int r
    int c

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
}
