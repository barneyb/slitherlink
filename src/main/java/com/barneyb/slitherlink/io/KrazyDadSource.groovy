package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.PuzzleSource
import groovy.transform.Immutable

/**
 *
 * @author bboisvert
 */
@Immutable
class KrazyDadSource implements PuzzleSource {

    static final char DOT = '.'
    static final char ZERO = '0'

    int rows
    int cols
    String spec

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
                p.cellCoord(r, c).clue(clue - ZERO)
            }
        }
        p
    }

}
