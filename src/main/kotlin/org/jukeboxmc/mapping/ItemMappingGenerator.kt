package org.jukeboxmc.mapping

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import org.jukeboxmc.mapping.entry.ItemEntryUpdaterBuilder
import org.jukeboxmc.mapping.model.ItemEntry
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * @author Kaooot
 * @version 1.0
 */
class ItemMappingGenerator {

    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        private val versionToProtocol = mapOf(
            "1.20.50" to 630,
            "1.20.60" to 649
        )

        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size < 2) {
                return
            }

            val itemPaletteDir = File("src/main/resources/item_palette/")
            val itemsMap: MutableMap<Int, List<ItemEntry>> = HashMap()
            val files = itemPaletteDir.listFiles()

            Arrays.sort(Objects.requireNonNull(files)) { obj: File, pathname: File? -> obj.compareTo(pathname) }

            for (file in files!!) {
                val version = file.name.split(".").toTypedArray()[1].split(".").toTypedArray()[0].replace("_", ".")
                val items: MutableList<ItemEntry> = ArrayList()

                try {
                    FileReader(file).use { fileReader ->
                        val jsonItems: JsonArray = gson.fromJson(fileReader, JsonArray::class.java)
                        for (i in 0..<jsonItems.size()) {
                            val jsonObject = jsonItems[i].asJsonObject
                            if (jsonObject.has("name") && jsonObject.has("id")) {
                                items.add(ItemEntry(jsonObject["name"].asString, jsonObject["id"].asInt, null))
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                itemsMap[versionToProtocol[version]!!] = items
                println("Successfully loaded items of $version")
            }

            val sourceProtocolVersion = versionToProtocol[args[0]]!!
            val targetProtocolVersion = versionToProtocol[args[1]]!!
            val sourcePalette: List<ItemEntry> = itemsMap[sourceProtocolVersion]!!
            val targetPalette: List<ItemEntry> = itemsMap[targetProtocolVersion]!!

            val mapping: MutableMap<ItemEntry, ItemEntry> = HashMap<ItemEntry, ItemEntry>()

            for (itemEntry in sourcePalette) {
                val target: ItemEntry = ItemEntryUpdaterBuilder.update(itemEntry, targetProtocolVersion)
                if (targetPalette.stream().noneMatch { i: ItemEntry -> i.getId() == target.getId() }) {
                    println("Cannot find targetItemEntry for $itemEntry")
                    continue
                }
                val finalEntry: ItemEntry = itemEntry.clone()

                // do not allow remapped metas on source
                finalEntry.setRemappedMetas(null)
                mapping[finalEntry] = target
            }

            try {
                FileOutputStream("src/main/resources/mappings/item_mapping_" + sourceProtocolVersion + "_to_" + targetProtocolVersion + ".json").use { fileOutputStream ->
                    val entries: MutableList<MappedItemEntry> = ArrayList()
                    for ((key, value) in mapping) {
                        val target: ItemEntry = value.clone()
                        val remappedMetas = value.getRemappedMetas()

                        // do not allow remapped metas on target because we use the remapped metas of the MappedItemEntry
                        target.setRemappedMetas(null)

                        // update networkId of target
                        itemsMap[targetProtocolVersion]!!.stream()
                            .filter { e -> e.getName().equals(target.getName(), ignoreCase = true) }
                            .findAny()
                            .ifPresent { e -> target.setId(e.getId()) }
                        entries.add(MappedItemEntry(key, target, remappedMetas))
                    }
                    fileOutputStream.write(gson.toJson(entries).toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    data class MappedItemEntry(private val source: ItemEntry, private val target: ItemEntry, private val remappedMetas: Map<Int, String>?) {
        fun getSource() = this.source

        fun getTarget() = this.target

        fun getRemappedMetas() = this.remappedMetas
    }
}