package org.jukeboxmc.mapping.updater

import com.google.gson.JsonObject
import org.jukeboxmc.mapping.model.ItemEntry

/**
 * @author Kaooot
 * @version 1.0
 */
class RemappedMetasUpdater(private val remappedMetas: Map<String, Map<Int, String>> = HashMap()) : Updater {

    companion object {
        fun build(jsonObject: JsonObject): RemappedMetasUpdater {
            val remappedMetas: MutableMap<String, Map<Int, String>> = HashMap()
            for ((key, value) in jsonObject.entrySet()) {
                val metas: MutableMap<Int, String> = HashMap()
                for ((key1, value1) in value.asJsonObject.entrySet()) {
                    metas[key1.toInt()] = value1.asString
                }
                remappedMetas[key] = metas
            }
            return RemappedMetasUpdater(remappedMetas)
        }
    }

    override fun update(itemEntry: ItemEntry) {
        if (!this.remappedMetas.containsKey(itemEntry.getName())) {
            return
        }
        itemEntry.setRemappedMetas(this.remappedMetas[itemEntry.getName()])
    }
}