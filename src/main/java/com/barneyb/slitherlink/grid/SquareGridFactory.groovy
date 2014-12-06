package com.barneyb.slitherlink.grid
/**
 * I will create a square grid of the specified dimensions.
 */
class SquareGridFactory implements GridFactory {

    @Override
    Grid create(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid width: $width")
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Invalid height: $height")
        }
        def g = new Grid(width, height)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                def f = new Face(4)
                f.dots << getDot(g, x, y)
                f.dots << getDot(g, x + 1, y)
                f.dots << getDot(g, x + 1, y + 1)
                f.dots << getDot(g, x, y + 1)
                g.faces << f
            }
        }
        makeConsistent(g)
    }

    protected Grid makeConsistent(Grid g) {
        def es = new TreeSet<Edge>()
        def getEdge = { Dot a, Dot b ->
            def e = a < b ? new Edge(a, b) : new Edge(b, a)
            def existing = es.find { it == e }
            if (! existing) {
                es.add(e)
                existing = e
            }
            existing
        }

        // Face.edges
        g.faces.each { f ->
            f.dots.eachWithIndex { d, i ->
                def edge = getEdge(d, f.dots[(i + 1) % f.order])
                if (edge.start == d) {
                    assert ! edge.right
                    edge.right = f
                } else {
                    assert ! edge.left
                    edge.left = f
                }
                f.edges << edge
                d.faces << f
            }
        }
        g.edges.addAll(es)

        // Dot.edges
        g.dots.each { d ->
            def list = d.faces.collect { f ->
                f.edges.findAll {
                    it.start == d || it.end == d
                }
            }.flatten().unique()
            while (list.min() != list.first()) {
                list.add list.remove(0)
            }
            d.edges.addAll list
        }

        g
    }

    protected Dot getDot(Grid g, int x, int y) {
        def i = y * (g.width + 1) + x
        def d = g.dots[i]
        if (! d) {
            d = g.dots[i] = new Dot(x, y)
        }
        d
    }

}
