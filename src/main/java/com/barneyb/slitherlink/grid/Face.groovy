package com.barneyb.slitherlink.grid
import groovy.transform.Canonical
/**
 *
 *
 * @author barneyb
 */
@Canonical
final class Face {

    final List<Dot> dots = []
    final List<Edge> edges = []
    Integer clue

}
