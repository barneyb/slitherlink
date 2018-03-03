package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.BLANK
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle

private const val DOT = '.'
private const val ZERO = '0'

fun krazydad(puzz: String): Puzzle {
    val p = puzz.replace(Regex("\\s+"), "")
    val size = Math.sqrt(p.length.toDouble()).toInt()
    if (size * size != p.length) {
        println("${size}x$size doesn't match '$p' (${p.length})")
        throw IllegalArgumentException("Only square grids may be constructed w/out explicit dimensions")
    }
    return krazydad(size, size, p)
}

fun krazydad(puzz: String, solved: String): Puzzle {
    val p = krazydad(puzz)
    solve(p, solved)
    return p
}

private fun solve(p: Puzzle, solved: String) {
    val edges = solved.replace(Regex("\\s+"), "")
        .map {
            it.toString().toInt(16)
        }.map { nyb ->
            (0..3).map { b ->
                (nyb and (1 shl b)) != 0
            }
        }
        .flatten()
    val itr = edges
        .map { if (it) ON else OFF }
        .iterator()
    for (r in 0 until p.humanRows) {
        for (c in 0 until p.humanCols) {
            if (r == 0) {
                p.state(0, c * 2 + 1, itr.next())
            }
            p.state(r * 2 + 1, c * 2 + 2, itr.next())
            p.state(r * 2 + 2, c * 2 + 1, itr.next())
            if (c == 0) {
                p.state(r * 2 + 1, 0, itr.next())
            }
        }
    }
}

fun krazydad(p: Puzzle): Pair<String, String> {
    val puzz = StringBuilder()
    for (r in 1 until p.gridRows step 2) {
        for (c in 1 until p.gridCols step 2) {
            val clue = p.clue(r, c)
            puzz.append(if (clue == BLANK) DOT else clue)
        }
    }
    val edges = mutableListOf<Boolean>()
    fun doEdge(s: EdgeState) = when (s) {
        OFF  -> edges.add(false)
        ON   -> edges.add(true)
        else -> throw IllegalArgumentException("KrazyDad can't represent puzzles w/ unknown edges")
    }
    for (r in 0 until p.humanRows) {
        for (c in 0 until p.humanCols) {
            if (r == 0) {
                doEdge(p.state(0, c * 2 + 1))
            }
            doEdge(p.state(r * 2 + 1, c * 2 + 2))
            doEdge(p.state(r * 2 + 2, c * 2 + 1))
            if (c == 0) {
                doEdge(p.state(r * 2 + 1, 0))
            }
        }
    }
    val solved = StringBuilder()
    var temp = 0
    edges.forEachIndexed { i, on ->
        if (on) {
            temp += (1 shl (i % 4))
        }
        if (i % 4 == 3) {
            solved.append(temp.toString(16))
            temp = 0
        }
    }
    return Pair(puzz.toString(), solved.toString().toUpperCase())
}

fun krazydad(rows: Int, cols: Int, puzz: String): Puzzle {
    if (puzz.length != rows * cols) {
        throw IllegalArgumentException("Spec doesn't match dimensions")
    }
    val p = Puzzle(rows, cols)
    val cs = puzz.toCharArray()
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val clue = cs[r * cols + c]
            if (clue != DOT) {
                p.humanClue(r, c, clue - ZERO)
            }
        }
    }
    return p
}

fun krazydad(rows: Int, cols: Int, puzz: String, solved: String): Puzzle {
    val p = krazydad(rows, cols, puzz)
    solve(p, solved)
    return p
}
