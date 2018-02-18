package com.barneyb.slitherlink

import groovy.transform.Immutable

/**
 *
 * @author bboisvert
 */
@Immutable
class KrazyDadLoader implements PuzzleLoader {

    int rows
    int cols
    String spec

    Puzzle load() {
        def p = new Puzzle(rows, cols)
        def cs = spec.toCharArray()
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                def clue = cs[r * rows + c]
                if (clue == '.') {
                    continue
                }
                p.cell(r, c, clue - ('0' as char))
            }
        }
        p
    }

}
