package com.barneyb.slitherlink.viz

import javax.swing.JLabel
import javax.swing.JPanel

/**
 *
 * @author bboisvert
 */
class Legend(
    val highlights: Collection<String>
) : JPanel() {

    init {
        highlights.forEachIndexed { i, it ->
            val c = JLabel(it)
            c.isOpaque = true
            c.background = highlightColors[i % highlightColors.size]
            add(c)
        }
    }
}
