package com.barneyb.slitherlink.grid
import groovy.transform.Canonical
/**
 *
 *
 * @author barneyb
 */
@Canonical
final class Face {

    final List<Edge> edges = []
    final List<Dot> dots = []
    Integer clue

}
