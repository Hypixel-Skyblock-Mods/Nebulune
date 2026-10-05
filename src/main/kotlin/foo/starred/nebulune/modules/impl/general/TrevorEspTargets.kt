package foo.starred.nebulune.modules.impl.general

/** Pure target matching, shared by the render path and regression checks. */
object TrevorEspTargets {
    enum class Species { Cow, Pig, Sheep, Chicken, Rabbit, Horse }

    data class Candidate<T>(val value: T, val species: Species, val x: Double, val y: Double, val z: Double)

    private val formatting = Regex("§[0-9a-fk-or]", RegexOption.IGNORE_CASE)
    private val nametag = Regex(
        "^(?:\\[Lv\\s*\\d+]\\s*)?(Trackable|Untrackable|Undetected|Endangered|Elusive)\\s+" +
            "(Cow|Pig|Sheep|Chicken|Rabbit|Horse)(?:\\s+([\\d.,]+[kKmM]?)(?:/[\\d.,]+[kKmM]?)?\\s*❤)?$",
        RegexOption.IGNORE_CASE,
    )

    @JvmStatic
    fun nametagSpecies(text: String, rarity: String): Species? {
        val match = nametag.matchEntire(formatting.replace(text, "").trim()) ?: return null
        if (!match.groupValues[1].equals(rarity, ignoreCase = true)) return null
        val health = match.groupValues[3]
        if (health.isNotEmpty() && health.replace(",", "").trimEnd('k', 'K', 'm', 'M').toDoubleOrNull() == 0.0) return null
        return Species.entries.first { it.name.equals(match.groupValues[2], ignoreCase = true) }
    }

    /** Prefer a loaded animal; its nearby matching hologram must not add another tracer. */
    @JvmStatic
    fun <T> select(animals: List<Candidate<T>>, nametags: List<Candidate<T>>): List<Candidate<T>> =
        animals + nametags.filter { tag ->
            animals.none { animal ->
                val dx = animal.x - tag.x
                val dz = animal.z - tag.z
                animal.species == tag.species && dx * dx + dz * dz <= 2.25 &&
                    tag.y - animal.y in -0.5..3.5
            }
        }
}
