package org.jukeboxmc.mapping.entry

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jukeboxmc.mapping.model.ItemEntry
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author Kaooot
 * @version 1.0
 */
object ItemEntryUpdaterBuilder {

    private val gson = Gson()
    private val updaters: List<ItemEntryUpdater> = arrayListOf(
        build(649, "0161_1.20.50.23_beta_to_1.20.60.26_beta.json")
    )

    fun update(itemEntry: ItemEntry, targetVersion: Int): ItemEntry {
        for (updater in updaters) {
            if (updater.getVersion() > targetVersion) {
                continue
            }
            for (u in updater.getUpdaters()) {
                u.update(itemEntry)
            }
        }
        return itemEntry
    }

    private fun build(version: Int, fileName: String): ItemEntryUpdater {
        try {
            ItemEntryUpdaterBuilder::class.java.classLoader.getResourceAsStream("BedrockItemUpgradeSchema/id_meta_upgrade_schema/$fileName").use { inputStream ->
                if (inputStream != null) {
                    return ItemEntryUpdater.fromJson(version, gson.fromJson(InputStreamReader(inputStream), JsonObject::class.java))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        throw RuntimeException("Could not retrieve ItemEntryUpdater from file $fileName")
    }
}