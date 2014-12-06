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
@EqualsAndHashCode(includes = ["x", "y"])
@ToString(includes = ["x", "y"], includePackage = false)
final class Dot implements Comparable<Dot> {

    final int x
    final int y
    final List<Edge> edges = []
    final List<Face> faces = []

    Dot(int x, int y) {
        this.x = x
        this.y = y
    }

    @Override
    int compareTo(Dot o) {
        if (y < o.y) return -1
        if (y > o.y) return  1
        if (x < o.x) return -1
        if (x > o.x) return  1
        0
    }
}
