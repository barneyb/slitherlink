package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

typealias Dir = Int

const val NORTH: Dir = 0
const val EAST: Dir = 1
const val SOUTH: Dir = 2
const val WEST: Dir = 3

private const val DOT = '·'
private const val VERT = '│'
private const val HORIZ = '─'
private const val TICK = '×'

data class Puzzle(
        val humanRows: Int,
        val humanCols: Int
) {
    val gridRows = humanRows * 2 + 1
    val gridCols = humanCols * 2 + 1
    internal val grid: IntArray

    init {
        assert(BLANK == UNKNOWN)
        grid = generateSequence { BLANK }.take(gridRows * gridCols).toList().toIntArray()
    }

    // grid accessors

    fun cell(r: Int, c: Int): Cell {
        return Cell(this, r, c)
    }

    fun edge(r: Int, c: Int): Edge {
        return Edge(this, r, c)
    }

    // human accessors

    fun humanCell(r: Int, c: Int): Cell {
        return cell(
                r * 2 + 1,
                c * 2 + 1
        )
    }

    fun humanEdge(r: Int, c: Int, d: Dir): Edge {
        var humanDir = d
        var humanRow = r
        var humanCol = c
        if (humanDir == EAST) {
            humanDir = WEST
            humanCol += 1
        }
        if (humanDir == SOUTH) {
            humanDir = NORTH
            humanRow += 1
        }
        return edge(
                humanRow * 2 + if (humanDir == NORTH) 0 else 1,
                humanCol * 2 + if (humanDir == WEST) 0 else 1
        )
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (r in 0 until gridRows) {
            if (r % 2 == 0) {
                // dot row
                for (c in 0 until gridCols) {
                    if (c % 2 == 0) {
                        // dot
                        sb.append(DOT)
                    } else {
                        // edge
                        val v = edge(r, c).state
                        if (v == ON) {
                            sb.append(HORIZ).append(HORIZ).append(HORIZ)
                        } else if (v == OFF) {
                            sb.append(' ').append(TICK).append(' ')
                        } else {
                            sb.append(' ').append(' ').append(' ')
                        }
                    }
                }
            } else {
                // cell row
                for (c in 0 until gridCols) {
                    if (c % 2 == 0) {
                        // edge
                        val v = edge(r, c).state
                        if (v == ON) {
                            sb.append(VERT)
                        } else if (v == OFF) {
                            sb.append(TICK)
                        } else {
                            sb.append(' ')
                        }
                    } else {
                        // cell
                        val cell = cell(r, c).clue
                        sb.append(' ')
                        if (cell == BLANK) {
                            sb.append(' ')
                        } else {
                            sb.append(cell)
                        }
                        sb.append(' ')
                    }
                }
            }
            sb.append('\n')
        }
        return sb.toString()
    }

}

