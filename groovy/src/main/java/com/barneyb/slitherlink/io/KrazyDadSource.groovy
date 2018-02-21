package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.PuzzleSource
import groovy.transform.Canonical
/**
 *
 * @author bboisvert
 */
@Canonical
class KrazyDadSource implements PuzzleSource {

    static final char DOT = '.'
    static final char ZERO = '0'

    final int rows
    final int cols
    final String spec

    KrazyDadSource(int rows, int cols, String spec) {
        this.rows = rows
        this.cols = cols
        this.spec = spec
    }

    KrazyDadSource(String spec) {
        def size = Math.sqrt(spec.length()) as int
        if (size * size != spec.length()) {
            throw new IllegalArgumentException("Only square grids may be constructed w/out explicit dimensions")
        }
        this.rows = size
        this.cols = size
        this.spec = spec
    }

    Puzzle load() {
        if (spec.length() != rows * cols) {
            throw new IllegalArgumentException("Spec doesn't match dimensions")
        }
        def p = new Puzzle(rows, cols)
        def cs = spec.toCharArray()
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                def clue = cs[r * cols + c]
                if (clue == DOT) {
                    continue
                }
                p.humanCellCoord(r, c).clue = clue - ZERO
            }
        }
        p
    }

}
