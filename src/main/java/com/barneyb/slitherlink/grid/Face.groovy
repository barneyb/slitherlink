package com.barneyb.slitherlink.grid
import groovy.transform.Canonical
import groovy.transform.ToString

/**
 *
 *
 * @author barneyb
 */
@Canonical
@ToString(includePackage = false)
final class Face {

    final List<Dot> dots = []
    final List<Edge> edges = []
    Integer clue

}
