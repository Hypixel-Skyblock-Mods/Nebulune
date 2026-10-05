package foo.starred.nebulune.modules.impl.general

import foo.starred.athen.annotations.Load
import foo.starred.athen.annotations.OnlyIn
import foo.starred.athen.api.scheduling.Scheduler
import foo.starred.athen.config.dsl.impl.category.ConfigCategory as Category
import foo.starred.athen.events.InputEvent
import foo.starred.kbus.extensions.runWhen
import foo.starred.athen.mixin.accessors.KeyMappingAccessor
import foo.starred.athen.modules.Module
import foo.starred.athen.utils.customData
import foo.starred.athen.utils.id
import foo.starred.nebulune.utils.rightClick
import foo.starred.snowbird.api.client
import foo.starred.snowbird.api.scheduling.scheduler.extensions.clientTicks as client
import foo.starred.snowbird.api.scheduling.scheduler.extensions.start
import net.minecraft.client.KeyMapping
import net.minecraft.world.InteractionHand
//? if >= 26.3
//import net.minecraft.world.item.component.SwingAnimation

@Load
@OnlyIn(skyblock = true)
object EtherwarpHelper : Module(
    "Etherwarp helper",
    "Helper features for Etherwarp.",
    Category.GENERAL
) {
    private val lcew = config.switch("Left click warp").unique("lcew")
    private val shift by config.switch("Shift automatically")

    private val ints = intArrayOf(2, 3, 4)

    init {
        on<InputEvent.Mouse.Press> {
            //~ if >= 26.2 'client.screen' -> 'client.gui.screen()'
            if (client.screen != null) return@on
            if (buttonInfo.button != 0) return@on

            val p = client.player ?: return@on
            if (p.mainHandItem.customData()?.getBoolean("ethermerge")?.orElse(false) != true && p.mainHandItem.id() != "ETHERWARP_CONDUIT") return@on

            val a = p.isCrouching
            if (!a && !shift) return@on

            if (!a) {
                KeyMapping.set((client.options.keyShift as KeyMappingAccessor).boundKey, true)
                Scheduler.schedule(ints.random().client.start) {
                    action()

                    Scheduler.schedule(1.client.start) { KeyMapping.set((client.options.keyShift as KeyMappingAccessor).boundKey, false) }
                }

                return@on cancel()
            }

            cancel()
            action()
        }.runWhen(lcew.state)
    }

    private fun action() {
        rightClick()
        with(client.player ?: return) {
            //? if >= 26.3 {
            /*if (isSwinging) return
            swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, false)
            *///? } else {
            if (swinging && swingTime >= 0) return

            swingingArm = InteractionHand.MAIN_HAND
            swingTime = -1
            swinging = true
            //? }
        }
    }
}
