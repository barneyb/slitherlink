package com.barneyb.slitherlink.grid

import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class SquareGridFactoryTest {

    @Test(expected = IllegalArgumentException)
    void badWidth() {
        new SquareGridFactory().create(0, 3)
    }

    @Test(expected = IllegalArgumentException)
    void badHeight() {
        new SquareGridFactory().create(3, 0)
    }

    @Test
    void oneByOne() {
        def g = new SquareGridFactory().create(1, 1)
        // counts
        assert g.dots.size() == 4
        assert g.edges.size() == 4
        assert g.faces.size() == 1

        def face = g.faces.first()
        def (e1, e2, e3, e4) = g.edges
        def (d1, d2, d3, d4) = g.dots

        // the face
        assert face.edges == g.edges
        assert face.dots == g.dots
        assert face.clue == null

        // d1
        assert d1.edges == [e4, e1]
        assert d1.faces == [face]

        // d2
        assert d2.edges == [e1, e2]
        assert d2.faces == [face]

        // d3
        assert d3.edges == [e2, e3]
        assert d3.faces == [face]

        // d4
        assert d4.edges == [e3, e4]
        assert d4.faces == [face]

        // e1
        assert e1.start == d1
        assert e1.end == d2
        assert e1.left == null
        assert e1.right == face

        // e2
        assert e2.start == d2
        assert e2.end == d3
        assert e2.left == face
        assert e2.right == null

        // e3
        assert e3.start == d3
        assert e3.end == d4
        assert e3.left == face
        assert e3.right == null

        // e4
        assert e4.start == d4
        assert e4.end == d1
        assert e4.left == null
        assert e4.right == face
    }

}
