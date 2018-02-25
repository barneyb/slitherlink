package com.barneyb.slitherlink

import com.barneyb.slitherlink.io.stringgrid
import kotlin.test.assertEquals

fun assertPuzzle(expected: String, actual: Puzzle) {
    assertPuzzle(stringgrid(expected), actual)
}

fun assertPuzzle(expected: Puzzle, actual: Puzzle) {
    assertPuzzle(expected, actual, false)
}

fun assertMoves(goal: String, actual: Puzzle) {
    assertMoves(stringgrid(goal), actual)
}

fun assertMoves(goal: Puzzle, actual: Puzzle) {
    assertPuzzle(goal, actual, true)
}

private fun assertPuzzle(
    expected: Puzzle,
    actual: Puzzle,
    allowExtra: Boolean
) {
    val diff = expected.diff(actual, allowExtra).firstOrNull()
            ?: return
    val lines = actual.toString()
        .split('\n')
        .toMutableList()
    val value = when (diff) {
        is Edge -> if (diff.on) "on" else if (diff.off) "off" else "unknown"
        is Cell -> if (diff.blank) " " else diff.clue.toString()
        else    -> "... something?"
    }
    val msg = "Expected $diff to be $value"
    lines[diff.r] += " <-- $msg"
    println(lines.joinToString("\n"))
    for (i in 0..(diff.c * 2 - 1)) print(' ')
    println("^ $msg")
    throw AssertionError(msg)
}

fun stringdiff(a: String, b: String) {
    println(a)
    println(b)
    val minLen = Math.min(a.length, b.length)
    val maxLen = Math.max(a.length, b.length)
    val chars = mutableListOf<Char>()
    for (i in 0 until minLen) {
        chars.add(if (a[i] == b[i]) ' ' else '^')
    }
    for (i in minLen until maxLen) {
        chars.add('^')
    }
    println(chars.joinToString(""))

}

val KD_7x7_d1_V1_B1_P1_RAW = """
            · × ·───·───· × ·───· × · × ·
            × 2 │ 2 × 2 │ 3 │ 3 │ 1 ×   ×
            ·───· × · × ·───· × · × ·───·
            │   ×   × 0 × 2 ×   │ 3 │   │
            ·───·───· × ·───· × ·───· × ·
            ×   ×   │   │   │   ×   ×   │
            ·───· × · × · × · × ·───· × ·
            │ 3 │ 3 │   │   │ 3 │ 3 │   │
            · × ·───· × · × ·───· × · × ·
            │   ×   × 2 │   × 2 ×   │   │
            ·───· × ·───· × ·───·───· × ·
            × 3 │   │ 2 ×   │ 2 × 2 ×   │
            ·───· × · × ·───· × ·───· × ·
            │ 3 ×   │ 2 │   ×   │ 3 │   │
            ·───·───· × ·───·───· × ·───·
        """.trimIndent()

fun assert_KD_7x7_d1_V1_B1_P1(p: Puzzle) {
    // · × ·───·───· × ·───· × · × ·
    assertEquals(OFF,   p.state(0,  1))
    assertEquals(ON,    p.state(0,  3))
    assertEquals(ON,    p.state(0,  5))
    assertEquals(OFF,   p.state(0,  7))
    assertEquals(ON,    p.state(0,  9))
    assertEquals(OFF,   p.state(0, 11))
    assertEquals(OFF,   p.state(0, 13))
    // × 2 │ 2 × 2 │ 3 │ 3 │ 1 ×   ×
    assertEquals(OFF,   p.state(1,  0))
    assertEquals(2,     p.clue (1,  1))
    assertEquals(ON,    p.state(1,  2))
    assertEquals(2,     p.clue (1,  3))
    assertEquals(OFF,   p.state(1,  4))
    assertEquals(2,     p.clue (1,  5))
    assertEquals(ON,    p.state(1,  6))
    assertEquals(3,     p.clue (1,  7))
    assertEquals(ON,    p.state(1,  8))
    assertEquals(3,     p.clue (1,  9))
    assertEquals(ON,    p.state(1, 10))
    assertEquals(1,     p.clue (1, 11))
    assertEquals(OFF,   p.state(1, 12))
    assertEquals(BLANK, p.clue (1, 13))
    assertEquals(OFF,   p.state(1, 14))
    // ·───· × · × ·───· × · × ·───·
    assertEquals(ON,    p.state(2,  1))
    assertEquals(OFF,   p.state(2,  3))
    assertEquals(OFF,   p.state(2,  5))
    assertEquals(ON,    p.state(2,  7))
    assertEquals(OFF,   p.state(2,  9))
    assertEquals(OFF,   p.state(2, 11))
    assertEquals(ON,    p.state(2, 13))
    // │   ×   × 0 × 2 ×   │ 3 │   │
    assertEquals(ON,    p.state(3,  0))
    assertEquals(BLANK, p.clue (3,  1))
    assertEquals(OFF,   p.state(3,  2))
    assertEquals(BLANK, p.clue (3,  3))
    assertEquals(OFF,   p.state(3,  4))
    assertEquals(0,     p.clue (3,  5))
    assertEquals(OFF,   p.state(3,  6))
    assertEquals(2,     p.clue (3,  7))
    assertEquals(OFF,   p.state(3,  8))
    assertEquals(BLANK, p.clue (3,  9))
    assertEquals(ON,    p.state(3, 10))
    assertEquals(3,     p.clue (3, 11))
    assertEquals(ON,    p.state(3, 12))
    assertEquals(BLANK, p.clue (3, 13))
    assertEquals(ON,    p.state(3, 14))
    // ·───·───· × ·───· × ·───· × ·
    assertEquals(ON,    p.state(4,  1))
    assertEquals(ON,    p.state(4,  3))
    assertEquals(OFF,   p.state(4,  5))
    assertEquals(ON,    p.state(4,  7))
    assertEquals(OFF,   p.state(4,  9))
    assertEquals(ON,    p.state(4, 11))
    assertEquals(OFF,   p.state(4, 13))
    // ×   ×   │   │   │   ×   ×   │
    assertEquals(OFF,   p.state(5,  0))
    assertEquals(BLANK, p.clue (5,  1))
    assertEquals(OFF,   p.state(5,  2))
    assertEquals(BLANK, p.clue (5,  3))
    assertEquals(ON,    p.state(5,  4))
    assertEquals(BLANK, p.clue (5,  5))
    assertEquals(ON,    p.state(5,  6))
    assertEquals(BLANK, p.clue (5,  7))
    assertEquals(ON,    p.state(5,  8))
    assertEquals(BLANK, p.clue (5,  9))
    assertEquals(OFF,   p.state(5, 10))
    assertEquals(BLANK, p.clue (5, 11))
    assertEquals(OFF,   p.state(5, 12))
    assertEquals(BLANK, p.clue (5, 13))
    assertEquals(ON,    p.state(5, 14))
    // ·───· × · × · × · × ·───· × ·
    assertEquals(ON,    p.state(6,  1))
    assertEquals(OFF,   p.state(6,  3))
    assertEquals(OFF,   p.state(6,  5))
    assertEquals(OFF,   p.state(6,  7))
    assertEquals(OFF,   p.state(6,  9))
    assertEquals(ON,    p.state(6, 11))
    assertEquals(OFF,   p.state(6, 13))
    // │ 3 │ 3 │   │   │ 3 │ 3 │   │
    assertEquals(ON,    p.state(7,  0))
    assertEquals(3,     p.clue (7,  1))
    assertEquals(ON,    p.state(7,  2))
    assertEquals(3,     p.clue (7,  3))
    assertEquals(ON,    p.state(7,  4))
    assertEquals(BLANK, p.clue (7,  5))
    assertEquals(ON,    p.state(7,  6))
    assertEquals(BLANK, p.clue (7,  7))
    assertEquals(ON,    p.state(7,  8))
    assertEquals(3,     p.clue (7,  9))
    assertEquals(ON,    p.state(7, 10))
    assertEquals(3,     p.clue (7, 11))
    assertEquals(ON,    p.state(7, 12))
    assertEquals(BLANK, p.clue (7, 13))
    assertEquals(ON,    p.state(7, 14))
    // · × ·───· × · × ·───· × · × ·
    assertEquals(OFF,   p.state(8,  1))
    assertEquals(ON,    p.state(8,  3))
    assertEquals(OFF,   p.state(8,  5))
    assertEquals(OFF,   p.state(8,  7))
    assertEquals(ON,    p.state(8,  9))
    assertEquals(OFF,   p.state(8, 11))
    assertEquals(OFF,   p.state(8, 13))
    // │   ×   × 2 │   × 2 ×   │   │
    assertEquals(ON,    p.state(9,  0))
    assertEquals(BLANK, p.clue (9,  1))
    assertEquals(OFF,   p.state(9,  2))
    assertEquals(BLANK, p.clue (9,  3))
    assertEquals(OFF,   p.state(9,  4))
    assertEquals(2,     p.clue (9,  5))
    assertEquals(ON,    p.state(9,  6))
    assertEquals(BLANK, p.clue (9,  7))
    assertEquals(OFF,   p.state(9,  8))
    assertEquals(2,     p.clue (9,  9))
    assertEquals(OFF,   p.state(9, 10))
    assertEquals(BLANK, p.clue (9, 11))
    assertEquals(ON,    p.state(9, 12))
    assertEquals(BLANK, p.clue (9, 13))
    assertEquals(ON,    p.state(9, 14))
    // ·───· × ·───· × ·───·───· × ·
    assertEquals(ON,    p.state(10,  1))
    assertEquals(OFF,   p.state(10,  3))
    assertEquals(ON,    p.state(10,  5))
    assertEquals(OFF,   p.state(10,  7))
    assertEquals(ON,    p.state(10,  9))
    assertEquals(ON,    p.state(10, 11))
    assertEquals(OFF,   p.state(10, 13))
    // × 3 │   │ 2 ×   │ 2 × 2 ×   │
    assertEquals(OFF,   p.state(11,  0))
    assertEquals(3,     p.clue (11,  1))
    assertEquals(ON,    p.state(11,  2))
    assertEquals(BLANK, p.clue (11,  3))
    assertEquals(ON,    p.state(11,  4))
    assertEquals(2,     p.clue (11,  5))
    assertEquals(OFF,   p.state(11,  6))
    assertEquals(BLANK, p.clue (11,  7))
    assertEquals(ON,    p.state(11,  8))
    assertEquals(2,     p.clue (11,  9))
    assertEquals(OFF,   p.state(11, 10))
    assertEquals(2,     p.clue (11, 11))
    assertEquals(OFF,   p.state(11, 12))
    assertEquals(BLANK, p.clue (11, 13))
    assertEquals(ON,    p.state(11, 14))
    // ·───· × · × ·───· × ·───· × ·
    assertEquals(ON,    p.state(12,  1))
    assertEquals(OFF,   p.state(12,  3))
    assertEquals(OFF,   p.state(12,  5))
    assertEquals(ON,    p.state(12,  7))
    assertEquals(OFF,   p.state(12,  9))
    assertEquals(ON,    p.state(12, 11))
    assertEquals(OFF,   p.state(12, 13))
    // │ 3 ×   │ 2 │   ×   │ 3 │   │
    assertEquals(ON,    p.state(13,  0))
    assertEquals(3,     p.clue (13,  1))
    assertEquals(OFF,   p.state(13,  2))
    assertEquals(BLANK, p.clue (13,  3))
    assertEquals(ON,    p.state(13,  4))
    assertEquals(2,     p.clue (13,  5))
    assertEquals(ON,    p.state(13,  6))
    assertEquals(BLANK, p.clue (13,  7))
    assertEquals(OFF,   p.state(13,  8))
    assertEquals(BLANK, p.clue (13,  9))
    assertEquals(ON,    p.state(13, 10))
    assertEquals(3,     p.clue (13, 11))
    assertEquals(ON,    p.state(13, 12))
    assertEquals(BLANK, p.clue (13, 13))
    assertEquals(ON,    p.state(13, 14))
    // ·───·───· × ·───·───· × ·───·
    assertEquals(ON,    p.state(14,  1))
    assertEquals(ON,    p.state(14,  3))
    assertEquals(OFF,   p.state(14,  5))
    assertEquals(ON,    p.state(14,  7))
    assertEquals(ON,    p.state(14,  9))
    assertEquals(OFF,   p.state(14, 11))
    assertEquals(ON,    p.state(14, 13))
}
