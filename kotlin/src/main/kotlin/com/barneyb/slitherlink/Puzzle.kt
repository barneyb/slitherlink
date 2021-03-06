package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.getSegment
import java.util.*
import kotlin.coroutines.experimental.buildSequence

const val DOT = '·'
const val VERT = '│'
const val HORIZ = '─'
const val TICK = '×'

abstract class PuzzleItem(
    internal val p: Puzzle,
    val r: Int,
    val c: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PuzzleItem) return false
        return r == other.r && c == other.c
    }

    override fun hashCode() = (r shl 10) + c

}

private fun toGrid(human: Int) = human * 2 + 1

class Puzzle private constructor(
    private val grid: IntArray,
    val gridRows: Int,
    val gridCols: Int
) {
    constructor(humanRows: Int, humanCols: Int) : this(
        IntArray((toGrid(humanRows)) * toGrid(humanCols)).apply { fill(BLANK) },
            toGrid(humanRows),
            toGrid(humanCols)
    )

    fun scratch(): Puzzle {
        val p = Puzzle(Arrays.copyOf(grid, grid.size), gridRows, gridCols)
        p._solved = _solved
        return p
    }

    // grid accessors

    private fun index(r: Int, c: Int) = r * gridCols + c

    internal fun clue(r: Int, c: Int) = grid[index(r, c)]

    internal fun clue(r: Int, c: Int, value: Clue) {
        if (!(value in ZERO..THREE)) {
            throw IllegalArgumentException("$value is not a valid clue (for $this)")
        }
        val curr = clue(r, c)
        if (curr != BLANK) {
            throw IllegalStateException("$this is already set to $curr")
        }
        grid[index(r, c)] = value
    }

    internal fun state(r: Int, c: Int) = grid[index(r, c)]

    internal fun state(r: Int, c: Int, value: EdgeState) {
        val curr = state(r, c)
        if (curr == value) return
        if (curr != UNKNOWN) {
            throw IllegalArgumentException("$this is $curr, you can't set it $value")
        }
        grid[index(r, c)] = value
    }

    internal fun move(m: Move) {
        val edge = if (m.edge.p === this) {
            m.edge
        } else {
            edge(m.edge.r, m.edge.c)
        }
        if (edge.state == m.state) {
            throw IllegalArgumentException("$m is redundant")
        }
        var forTheWin = false
        if (m.on) {
            var dot = edge.dots.find { it.edges.count { it.on } == 2 }
            if (dot != null) {
                throw IllegalMoveException(m, "create a branch at $dot")
            }
            dot = edge.dots.find {
                it.edges.count { it.off } == it.edges.size - 1
            }
            if (dot != null) {
                throw IllegalMoveException(m, "strand $dot")
            }
            val cell = edge.cells.find { it.edges.count { it.on } == it.clue }
            if (cell != null) {
                throw IllegalMoveException(m, "over-satisfy $cell")
            }
            // check the loop
            val (a, b) = edge.dots
            val onEdges = a.edges(ON)
            if (onEdges.size == 1 && b.edges(ON).size == 1) {
                val segment = getSegment(a, onEdges.first())
                if (b == segment.end) {
                    // it'll close a loop. Test the rest...
                    if (edgeCount() > segment.length) {
                        throw IllegalMoveException(
                            m,
                            "close an incomplete loop"
                        )
                    }
                    val c = clueCells().find {
                        (if (edge in it.edges)
                            it.clue - 1
                        else
                            it.clue) != it.edges.count { it.on }
                    }
                    if (c != null) {
                        throw IllegalMoveException(m, "leave $c unsatisfied")
                    }
                    // it's on, it'll close a complete loop, and all clues
                    // will be satisfied. Nicely done.
                    forTheWin = true
                }
            }
        } else {
            val cell = edge.cells.find {
                it.edges.count { it.on || it.unknown } == it.clue
            }
            if (cell != null) {
                throw IllegalMoveException(m, "leave $cell unsatisfied")
            }
            val dot = edge.dots.find {
                it.edges.count { it.on } == 1 && it.edges.count { it.unknown } == 1
            }
            if (dot != null) {
                throw IllegalMoveException(m, "strand $dot")
            }
        }
        state(edge.r, edge.c, m.state)
        if (forTheWin) {
            _solved = true
        }
    }

    fun move(edge: Edge, state: EdgeState) = move(Move(edge, state))

    fun cell(r: Int, c: Int) = Cell(this, r, c)

    fun edge(r: Int, c: Int) = Edge(this, r, c)

    fun dot(r: Int, c: Int) = Dot(this, r, c)

    internal fun item(r: Int, c: Int) =
        if (r % 2 != c % 2) {
            edge(r, c)
        } else if (r % 2 == 1 && c % 2 == 1) {
            cell(r, c)
        } else {
            dot(r, c)
        }

    fun northWestCorner() = cell(1, 1)
    fun northEastCorner() = cell(1, gridCols - 2)
    fun southWestCorner() = cell(gridRows - 2, 1)
    fun southEastCorner() = cell(gridRows - 2, gridCols - 2)

    fun cells(): List<Cell> {
        val ds = mutableListOf<Cell>()
        for (r in 1 until gridRows step 2) {
            for (c in 1 until gridCols step 2) {
                ds.add(cell(r, c))
            }
        }
        return ds
    }

    fun clueCells() = cells().filter {
        !it.blank
    }

    fun clueCells(c: Clue) = cells().filter {
        it.clue == c
    }

    fun dots(): List<Dot> {
        val ds = mutableListOf<Dot>()
        for (r in 0 until gridRows step 2) {
            for (c in 0 until gridCols step 2) {
                ds.add(dot(r, c))
            }
        }
        return ds
    }

    fun edges(): List<Edge> {
        val es = mutableListOf<Edge>()
        for (r in 0 until gridRows) {
            for (c in (1 - r % 2) until gridCols step 2) {
                es.add(edge(r, c))
            }
        }
        return es
    }

    fun edges(state: EdgeState) = edges().filter { it.state == state }

    // human accessors

    val humanRows get() = (gridRows - 1) / 2
    val humanCols get() = (gridCols - 1) / 2

    fun humanClue(humanRow: Int, humanCol: Int) = clue(
        toGrid(humanRow),
        toGrid(humanCol)
    )

    fun humanClue(humanRow: Int, humanCol: Int, value: Clue) = clue(
        toGrid(humanRow),
        toGrid(humanCol),
        value
    )

    fun humanCell(humanRow: Int, humanCol: Int) = cell(
        toGrid(humanRow),
        toGrid(humanCol)
    )

    // end human accessors

    private var _solved = false
    fun isSolved() = _solved

    private fun edgeCount() = edges().count { it.on }

    override fun toString(): String {
        val sb = StringBuilder()
        for (r in 0 until gridRows) {
            if (r > 0) {
                sb.append('\n')
            }
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
        }
        return sb.toString()
    }

    fun diff(other: Puzzle, allowExtra: Boolean = false) = buildSequence {
        if (gridRows != other.gridRows || gridCols != other.gridCols) {
            throw IllegalArgumentException("This puzzle is ${humanRows}x$humanCols which can't be diffed w/ a ${other.humanRows}x${other.humanCols} one")
        }
        for (r in 0 until gridRows) {
            for (c in 0 until gridCols) {
                val i = index(r, c)
                if (grid[i] != other.grid[i]) {
                    if (!allowExtra || grid[i] != UNKNOWN) {
                        yield(item(r, c))
                    }
                }
            }
        }
    }

    operator fun minus(other: Puzzle) =
        this.diff(other)
            .map {
                if (it !is Edge) {
                    throw IllegalArgumentException("Found a non-edge diff. Check your input.")
                }
                Move(it, it.state)
            }
            .toSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Puzzle) return false

        if (gridRows != other.gridRows) return false
        if (gridCols != other.gridCols) return false
        if (!Arrays.equals(grid, other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(grid)
        result = 31 * result + gridRows
        result = 31 * result + gridCols
        return result
    }

}

