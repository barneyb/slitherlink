package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.Puzzle

private const val DOT = '.'
private const val ZERO = '0'

fun krazydad(spec: String): Puzzle {
    val size = Math.sqrt(spec.length.toDouble()).toInt()
    if (size * size != spec.length) {
        throw IllegalArgumentException("Only square grids may be constructed w/out explicit dimensions")
    }
    return krazydad(size, size, spec)
}

fun krazydad(rows: Int, cols: Int, spec: String): Puzzle {
    if (spec.length != rows * cols) {
        throw IllegalArgumentException("Spec doesn't match dimensions")
    }
    val p = Puzzle(rows, cols)
    val cs = spec.toCharArray()
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val clue = cs[r * cols + c]
            if (clue == DOT) {
                continue
            }
            p.humanCell(r, c).clue = clue - ZERO
        }
    }
    return p
}
