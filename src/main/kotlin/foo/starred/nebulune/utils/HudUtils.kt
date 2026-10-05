package foo.starred.nebulune.utils

import foo.starred.athen.api.minecraft.text.measurer.VanillaFontMeasurer
import foo.starred.athen.api.minecraft.text.renderer.VanillaFontRenderer
import foo.starred.athen.config.dsl.base.ConfigScope
import foo.starred.athen.utils.render.fcs
import foo.starred.snowbird.utils.toCamelCase

fun ConfigScope.textHud(name: String, example: String, text: () -> String?) = hud(name) {
    constrain { VanillaFontMeasurer.constrain((text() ?: example).fcs) }
    preview { VanillaFontRenderer.extract(graphics, example, 0, 0) }
    render {
        val value = text() ?: return@render
        VanillaFontRenderer.extract(graphics, value, 0, 0)
    }
}.unique(name.toCamelCase())
