package org.jukeboxmc.mapping.updater

import com.google.gson.JsonObject
import org.jukeboxmc.mapping.model.ItemEntry

/**
 * @author Kaooot
 * @version 1.0
 */
class RenamedIdsUpdater(private val renamedIds: Map<String, String> = HashMap()) : Updater {

    companion object {
        fun build(jsonObject: JsonObject): RenamedIdsUpdater {
            val renamedIds: MutableMap<String, String> = HashMap()
            for ((key, value) in jsonObject.entrySet()) {
                renamedIds[key] = value.asString
            }
            return RenamedIdsUpdater(renamedIds)
        }
    }

    override fun update(itemEntry: ItemEntry) {
        if (!this.renamedIds.containsKey(itemEntry.getName())) {
            return
        }
        itemEntry.setName(this.renamedIds[itemEntry.getName()]!!)
    }
}