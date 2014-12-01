package com.barneyb.slitherlink.grid
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *
 *
 * @author barneyb
 */
@Canonical
@EqualsAndHashCode(includes = ["width", "height", "dots"])
@ToString(includes = ["width", "height", "dots"], includePackage = false)
class Grid {

    final int width
    final int height
    final List<Face> faces = []
    final List<Dot> dots = []
    final List<Edge> edges = []

    Grid(int width, int height) {
        this.width = width
        this.height = height
    }

}
