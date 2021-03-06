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
    annotate(actual, diff)
    throw AssertionError("$diff is wrong")
}

fun annotate(p: Puzzle, item: PuzzleItem, value: String) {
    val lines = p.toString()
        .split('\n')
        .toMutableList()
    annotateItem(lines, item, value)
    println(lines.joinToString("\n"))
}

fun annotate(p: Puzzle, vararg items: PuzzleItem) {
    val lines = p.toString()
        .split('\n')
        .toMutableList()
    for (it in items) {
        val value = when (it) {
            is Edge -> humanState(it.state)
            is Cell -> humanClue(it.clue)
            else    -> "... something?"
        }
        annotateItem(lines, it, value)
    }
    println(lines.joinToString("\n"))
}

private fun annotateItem(
    lines: MutableList<String>,
    item: PuzzleItem,
    expected: String
) {
    val msg = "Expected $item to be $expected"
    lines[item.r] += " <-- $msg"
    val foot = StringBuilder()
    for (i in 0..(item.c * 2 - 1)) foot.append(' ')
    foot.append("^ $msg")
    lines.add(foot.toString())
}

/**
 * I accept a puzzle grid string extended with `'!'` and `'?'` to indicate edges
 * that should [OFF] and [ON] respectively, and return a pair of grid strings,
 * the first with the marks stripped, and the second with the marks converted
 * to their in-place equivalents. I.e., to create a start/goal pair of grids
 * from a single grid.
 */
fun toStartEnd(grid: String) = Pair(
    grid.replace('!', ' ')
        .replace('?', ' '),
    grid.replace('!', 'x')
        .replace("· ? ·", ".---.")
        .replace(". ? .", ".---.")
        .replace("+ ? +", ".---.")
        .replace('?', '|')
)

/**
 * I convert the [startEnd] string into start and end strings and pass through
 * to [assertStrategy].
 */
fun assertStrategy(strat: Strategy, startEnd: String) {
    val p = toStartEnd(startEnd)
    assertStrategy(strat, p.first, p.second)
}

/**
 * I request one batch of moves from the provided [Strategy], given the [start]
 * state, and ensure the batch is exactly the delta needed to get to the [end]
 * state. Both missing and extra moves are disallowed.
 */
fun assertStrategy(strat: Strategy, start: String, end: String) {
    val p = stringgrid(start)
    val expected = stringgrid(end) - p
    val actual = strat(p).toSet()
    val missingMoves = expected - actual
    val extraMoves = actual - expected
    val toUnknown = missingMoves.find { it.state == UNKNOWN }
    if (toUnknown != null) {
        annotate(p, missingMoves.first().edge)
        throw IllegalArgumentException("Test asserts an UNKNOWN move?! ERROR!")
    }
    if (missingMoves.isNotEmpty()) {
        actual.forEach {
            p.move(it)
        }
        annotate(p, missingMoves.first().edge)
        assertEquals(expected, actual)
    }
    if (extraMoves.isNotEmpty()) {
        actual.forEach {
            p.move(it)
        }
        annotate(p, extraMoves.first().edge, "UNKNOWN")
        assertEquals(expected, actual)
    }
}

fun assertSolveResult(start: String, goal: String) {
    if (start.trim().isEmpty()) throw IllegalArgumentException("you can't have an empty start")
    if (goal.trim().isEmpty()) throw IllegalArgumentException("you can't have an empty goal")
    val p = stringgrid(start)
    solve(p)
    assertMoves(goal, p)
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
    //@formatter:off
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
    //@formatter:on
}
