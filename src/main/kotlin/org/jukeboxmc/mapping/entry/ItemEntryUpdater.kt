package org.jukeboxmc.mapping.entry

import com.google.gson.JsonObject
import org.jukeboxmc.mapping.updater.RemappedMetasUpdater
import org.jukeboxmc.mapping.updater.RenamedIdsUpdater
import org.jukeboxmc.mapping.updater.Updater

/**
 * @author Kaooot
 * @version 1.0
 */
class ItemEntryUpdater(private val version: Int, private val updaters: Set<Updater>) {

    companion object {
        fun fromJson(version: Int, jsonObject: JsonObject): ItemEntryUpdater {
            val updaters: MutableSet<Updater> = HashSet()
            for (key in jsonObject.keySet()) {
                if (key.equals("renamedIds", ignoreCase = true)) {
                    updaters.add(RenamedIdsUpdater.build(jsonObject.getAsJsonObject("renamedIds")))
                    continue
                }
                if (key.equals("remappedMetas", ignoreCase = true)) {
                    updaters.add(RemappedMetasUpdater.build(jsonObject.getAsJsonObject("remappedMetas")))
                }
            }
            return ItemEntryUpdater(version, updaters)
        }
    }

    fun getVersion() = this.version

    fun getUpdaters() = this.updaters
}