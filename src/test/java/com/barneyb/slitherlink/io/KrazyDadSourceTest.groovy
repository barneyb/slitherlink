package com.barneyb.slitherlink.io

import com.barneyb.slitherlink.CellCoord
import org.junit.Test

import static com.barneyb.slitherlink.Puzzle.*
import static org.junit.Assert.*
/**
 *
 * @author bboisvert
 */
class KrazyDadSourceTest {

    @Test
    void cornerOfSomething() {
        def p = new KrazyDadSource("2223..02....33..").load()
        println p
        assertEquals([
            (new CellCoord(0, 0)): 2,
            (new CellCoord(0, 1)): 2,
            (new CellCoord(0, 2)): 2,
            (new CellCoord(0, 3)): 3,
            (new CellCoord(1, 0)): BLANK,
            (new CellCoord(1, 1)): BLANK,
            (new CellCoord(1, 2)): 0,
            (new CellCoord(1, 3)): 2,
            (new CellCoord(2, 0)): BLANK,
            (new CellCoord(2, 1)): BLANK,
            (new CellCoord(2, 2)): BLANK,
            (new CellCoord(2, 3)): BLANK,
            (new CellCoord(3, 0)): 3,
            (new CellCoord(3, 1)): 3,
            (new CellCoord(3, 2)): BLANK,
            (new CellCoord(3, 3)): BLANK,
        ], p.cells().collectEntries { [it, it.clue] })
    }

    @Test
    void twoByFour() {
        def p = new KrazyDadSource(2, 4, ".11.....").load()
        println p
        assertEquals([
            (new CellCoord(0, 0)): BLANK,
            (new CellCoord(0, 1)): 1,
            (new CellCoord(0, 2)): 1,
            (new CellCoord(0, 3)): BLANK,
            (new CellCoord(1, 0)): BLANK,
            (new CellCoord(1, 1)): BLANK,
            (new CellCoord(1, 2)): BLANK,
            (new CellCoord(1, 3)): BLANK,
        ], p.cells().collectEntries { [it, it.clue] })
    }

    @Test
    void fourByTwo() {
        def p = new KrazyDadSource(4, 2, ".11.....").load()
        println p
        assertEquals([
            (new CellCoord(0, 0)): BLANK,
            (new CellCoord(0, 1)): 1,
            (new CellCoord(1, 0)): 1,
            (new CellCoord(1, 1)): BLANK,
            (new CellCoord(2, 0)): BLANK,
            (new CellCoord(2, 1)): BLANK,
            (new CellCoord(3, 0)): BLANK,
            (new CellCoord(3, 1)): BLANK,
        ], p.cells().collectEntries { [it, it.clue] })
    }

}
