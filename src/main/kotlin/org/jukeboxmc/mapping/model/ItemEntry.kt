package org.jukeboxmc.mapping.model

/**
 * @author Kaooot
 * @version 1.0
 */
class ItemEntry(private var name: String, private var id: Int, private var remappedMetas: Map<Int, String>?) : Cloneable {

    fun getName() = this.name

    fun setName(name: String) {
        this.name = name
    }

    fun getId() = this.id

    fun setId(id: Int) {
        this.id = id
    }

    fun getRemappedMetas() = this.remappedMetas

    fun setRemappedMetas(remappedMetas: Map<Int, String>?) {
        this.remappedMetas = remappedMetas
    }

    public override fun clone(): ItemEntry {
        return try {
            super.clone() as ItemEntry
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }
}