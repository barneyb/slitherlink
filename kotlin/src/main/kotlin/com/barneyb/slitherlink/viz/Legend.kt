package com.barneyb.slitherlink.viz

import javax.swing.JLabel
import javax.swing.JPanel

/**
 *
 * @author bboisvert
 */
class Legend() : JPanel() {

    constructor(items: Collection<String>) : this() {
        setItems(items)
    }

    fun setItems(items: Collection<String>) {
        removeAll()
        items.forEachIndexed { i, it ->
            val c = JLabel(it)
            c.isOpaque = true
            c.background = highlightColors[i % highlightColors.size]
            add(c)
        }
    }
}
