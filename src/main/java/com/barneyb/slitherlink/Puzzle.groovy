package com.barneyb.slitherlink
/**
 * I represent a Slitherlink puzzle. Rows and columns refer to the cells, not
 * the dots (so a 10x10 puzzle has 100 cells and 121 dots).
 *
 * @author barneyb
 */
class Puzzle {

    final int rows
    final int cols

    final int[] cells
    final EdgeState[] horizontalEdges
    final EdgeState[] verticalEdges

    Puzzle(rows, cols) {
        this.rows = rows
        this.cols = cols
        this.cells = new int[rows * cols];
        Arrays.setAll(this.cells, { i -> BLANK })
        this.horizontalEdges = new EdgeState[(rows + 1) * cols]
        Arrays.setAll(this.horizontalEdges, { i -> EdgeState.UNKNOWN })
        this.verticalEdges = new EdgeState[rows * (cols + 1)]
        Arrays.setAll(this.verticalEdges, { i -> EdgeState.UNKNOWN })
    }

    static final int BLANK = -1
    static final char DOT = '·'
    static final char VERT = '│'
    static final char HORIZ = '─'
    static final char TICK = '×'

    @Override
    String toString() {
        def sb = new StringBuilder()
        for (int r = 0; r <= rows; r++) {
            sb.append(DOT)
            for (int c = 0; c < cols; c++) {
                def v = edge(r, c, Dir.NORTH)
                (v == EdgeState.ON
                    ? sb.append(HORIZ).append(HORIZ).append(HORIZ)
                    : v == EdgeState.OFF
                        ? sb.append(' ').append(TICK).append(' ')
                        : sb.append(' ').append(' ').append(' '))
                sb.append(DOT)
            }
            sb.append('\n')
            if (r < rows) {
                def v = edge(r, 0, Dir.WEST)
                sb.append(v == EdgeState.ON
                    ? VERT
                    : v == EdgeState.OFF
                        ? TICK
                        : ' ')
                for (int c = 0; c < cols; c++) {
                    def cell = cell(r, c)
                    sb.append(' ')
                    sb.append(cell == BLANK ? ' ' : cell)
                    sb.append(' ')
                    v = edge(r, c, Dir.EAST)
                    sb.append(v == EdgeState.ON
                        ? VERT
                        : v == EdgeState.OFF
                            ? TICK
                            : ' ')
                }
                sb.append('\n')
            }
        }
        sb.toString()
    }

    EdgeState edge(int r, int c, Dir d) {
        edge(new EdgeCoord(r, c, d))
    }

    EdgeState edge(EdgeCoord ec) {
        ec.canonical()
            .state(this)
    }

    Puzzle edge(int r, int c, Dir d, EdgeState state) {
        edge(new EdgeCoord(r, c, d), state)
    }

    Puzzle edge(EdgeCoord ec, EdgeState state) {
        ec = ec.canonical()
        def curr = ec.state(this)
        if (curr != EdgeState.UNKNOWN) {
            throw new IllegalArgumentException("Edge $ec.d of row $ec.r col $ec.c is already set to $curr")
        }
        ec.state(this, state)
        this
    }

    private int cellIndex(int r, int c) {
        r * cols + c
    }

    int cell(int r, int c) {
        cell(new CellCoord(r, c))
    }

    int cell(CellCoord cc) {
        cc.clue(this)
    }

    Puzzle cell(int r, int c, int clue) {
        cell(new CellCoord(r, c), clue)
    }

    Puzzle cell(CellCoord cc, int clue) {
        def curr = cc.clue(this)
        if (curr != BLANK) {
            throw new IllegalArgumentException("Cell at row $cc.r col $cc.c is already set to $curr")
        }
        cc.clue(this, clue)
        this
    }

    Puzzle move(Move m) {
        edge(m.edge, m.state)
        this
    }

}
