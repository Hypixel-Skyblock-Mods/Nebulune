package foo.starred.nebulune.utils

import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandCommandScope
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

interface NebuluneCommand : IKommand<FabricClientCommandSource> {
    override val loader: KommandCommandScope<FabricClientCommandSource>
        get() = NebuluneCommands.loader
}

private object NebuluneCommands {
    val loader = KommandCommandScope<FabricClientCommandSource>()

    init {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            loader.register(dispatcher)
        }
    }
}
