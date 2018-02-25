package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.HORIZ
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.TICK
import com.barneyb.slitherlink.VERT

fun stringgrid(p: Puzzle) = unicodegrid(p)

fun asciigrid(p: Puzzle) = unicodegrid(p)
    .replace(HORIZ, '-')
    .replace(VERT, '|')
    .replace(com.barneyb.slitherlink.DOT, '+')
    .replace(TICK, 'x')

fun unicodegrid(p: Puzzle) = p.toString()

fun stringgrid(str:String): Puzzle {
    val lines = str.trimIndent().trim().split('\n')
    val rows = lines.size
    if (rows % 2 == 0) {
        throw IllegalArgumentException("Raw grids always have an odd number of rows")
    }
    val cols = lines.first().length
    if (cols % 2 == 0) {
        throw IllegalArgumentException("Raw grids always have an odd number of columns")
    }
    lines.forEachIndexed { r, it ->
        if (it.length > cols) {
            throw IllegalArgumentException("Row ${r + 1} is too long (${it.length} vs $cols)")
        }
    }
    val p = Puzzle((rows - 1) / 2, (cols - 1) / 4) // 4 here cuzza three-char cell width
    lines.forEachIndexed { r, line ->
        line.filterIndexed { i, _ -> i % 2 == 0 }
            .forEachIndexed { c, chr ->
                if (r % 2 == 1) {
                    // clue row
                    if (c % 2 == 0) {
                        // vertical edge
                        if (chr == '|' || chr == VERT) {
                            p.state(r, c, ON)
                        } else if (chr == 'x' || chr == 'X' || chr == TICK) {
                            p.state(r, c, OFF)
                        }
                    } else {
                        // clue
                        when (chr) {
                            '0' -> p.clue(r, c, 0)
                            '1' -> p.clue(r, c, 1)
                            '2' -> p.clue(r, c, 2)
                            '3' -> p.clue(r, c, 3)
                        }
                    }
                } else {
                    // dot row
                    if (c % 2 == 1) {
                        if (chr == '-' || chr == HORIZ) {
                            p.state(r, c, ON)
                        } else if (chr == 'x' || chr == TICK) {
                            p.state(r, c, OFF)
                        }
                    }
                }
            }
    }
    return p
}
