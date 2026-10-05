@file:Suppress("ConstPropertyName")

package foo.starred.nebulune.modules.impl.general.messageactions.actions.impl

import foo.starred.athen.annotations.Load
import foo.starred.athen.modules.impl.general.messageactions.actions.base.IMessageAction
import foo.starred.athen.modules.impl.general.messageactions.actions.data.MessageActionType
import foo.starred.athen.modules.impl.general.messageactions.actions.data.MessageActionField
import foo.starred.snowbird.api.command

@Load
class CommandAction(val command: String) : IMessageAction {
    private val empty = command.isEmpty()

    override val id: Int = int
    override val name: String = str
    override val serializable: Map<String, String> = mapOf("text" to command)

    override fun run() {
        if (empty) return
        command.command()
    }

    override fun resolve(text: String, match: MatchResult?): IMessageAction {
        if (match == null) return this
        var value = command
        for (i in match.groupValues.indices.reversed()) value = value.replace("$$i", match.groupValues[i])
        return CommandAction(value)
    }

    companion object {
        const val int = 1
        const val str = "Command"

        init {
            IMessageAction.register(MessageActionType(int, str, listOf(MessageActionField("text", str, str))) { CommandAction(it["text"] ?: "") })
        }
    }
}