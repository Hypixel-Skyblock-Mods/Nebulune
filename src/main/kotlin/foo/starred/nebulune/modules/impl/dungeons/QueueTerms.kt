@file:Suppress("ObjectPropertyName")

package foo.starred.nebulune.modules.impl.dungeons

import foo.starred.athen.annotations.Load
import foo.starred.athen.config.dsl.impl.category.ConfigCategory as Category
import foo.starred.athen.modules.Module
import foo.starred.athen.modules.impl.dungeon.terminals.solver.data.TerminalClick as Click

@Load
object QueueTerms : Module(
    "Queue terms",
    "Queues terminal clicks to automatically fire.",
    Category.DUNGEONS
) {
    val timeout by config.slider("Resync timeout", 800, 400, 1000, "ms")

    val clicks = mutableListOf<Click>()
    var yearning = false
}