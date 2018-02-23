package com.barneyb.slitherlink

import org.junit.Test

import static com.barneyb.slitherlink.Dir.*

/**
 *
 *
 * @author barneyb
 */
class DotCoordTest {

    @Test
    void adjacent() {
        assert new DotCoord(1, 1).adjacent(new DotCoord(0, 1))
        assert new DotCoord(1, 1).adjacent(new DotCoord(1, 0))
        assert !new DotCoord(1, 1).adjacent(new DotCoord(1, 1))
        assert !new DotCoord(1, 1).adjacent(new DotCoord(0, 0))
    }

    @Test
    void dir() {
        assert new DotCoord(1, 1).dir(new DotCoord(0, 1)) == NORTH
        assert new DotCoord(1, 1).dir(new DotCoord(1, 0)) == WEST
        assert new DotCoord(1, 1).dir(new DotCoord(2, 1)) == SOUTH
        assert new DotCoord(1, 1).dir(new DotCoord(1, 2)) == EAST
    }

}
