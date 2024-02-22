package org.jukeboxmc.mapping.updater

import org.jukeboxmc.mapping.model.ItemEntry

/**
 * @author Kaooot
 * @version 1.0
 */
interface Updater {

    fun update(itemEntry: ItemEntry)
}