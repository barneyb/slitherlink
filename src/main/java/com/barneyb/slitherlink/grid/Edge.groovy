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
@EqualsAndHashCode(includes = ["start", "end"])
@ToString(includes = ["start", "end"], includePackage = false)
final class Edge implements Comparable<Edge> {

    final Dot start
    final Dot end
    Face left
    Face right

    Edge(Dot start, Dot end) {
        this.start = start
        this.end = end
        if (start == end) {
            throw new IllegalArgumentException("An edge must have distinct start and end dots: $start-$end.")
        }
        if (start > end) {
            throw new IllegalArgumentException("An edge must end at least as far from the origin as it start: $start-$end.")
        }
    }

    @Override
    int compareTo(Edge o) {
        if (start < o.start) return -1
        if (start > o.start) return  1
        if (end < o.end) return -1
        if (end > o.end) return  1
        0
    }
}
