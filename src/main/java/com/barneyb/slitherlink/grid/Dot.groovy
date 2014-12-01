package com.barneyb.slitherlink.grid
import groovy.transform.Canonical
/**
 *
 *
 * @author barneyb
 */
@Canonical
final class Dot {

    final List<Dot> edges = []
    final List<Face> faces = []

}
