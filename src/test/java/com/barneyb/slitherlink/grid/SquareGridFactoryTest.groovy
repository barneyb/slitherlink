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
        /*
         0  0  1
          +---+
        1 | 0 | 2
          +---+
         2  3  3
         */
        def g = new SquareGridFactory().create(1, 1)
        // counts
        assert g.dots.size() == 4 // (x+1) * (y+1)
        assert g.edges.size() == 4 // (x+1) * y + x * (y+1)
        assert g.faces.size() == 1 // x * y

        def face = g.faces.first()
        def (Edge e0, e1, e2, e3) = g.edges
        def (Dot d0, d1, d2, d3) = g.dots

        assert d0 == new Dot(0, 0)
        assert d1 == new Dot(1, 0)
        assert d2 == new Dot(0, 1)
        assert d3 == new Dot(1, 1)

        assert e0 == new Edge(d0, d1)
        assert e1 == new Edge(d0, d2)
        assert e2 == new Edge(d1, d3)
        assert e3 == new Edge(d2, d3)

        // the face
        assert face.order == 4
        assert face.edges as Set == g.edges as Set
        assert face.edges == [e0, e2, e3, e1]
        assert face.dots as Set == g.dots as Set
        assert face.dots == [d0, d1, d3, d2]
        assert face.clue == null

        assert e0.start == d0
        assert e0.end == d1
        assert e0.left == null
        assert e0.right == face

        assert e1.start == d0
        assert e1.end == d2
        assert e1.left == face
        assert e1.right == null

        assert e2.start == d1
        assert e2.end == d3
        assert e2.left == null
        assert e2.right == face

        assert e3.start == d2
        assert e3.end == d3
        assert e3.left == face
        assert e3.right == null

        assert d0.faces == [face]
        assert d0.edges == [e0, e1]

        assert d1.faces == [face]
        assert d1.edges == [e0, e2]

        assert d2.faces == [face]
        assert d2.edges == [e1, e3]

        assert d3.faces == [face]
        assert d3.edges == [e2, e3]
    }

}
