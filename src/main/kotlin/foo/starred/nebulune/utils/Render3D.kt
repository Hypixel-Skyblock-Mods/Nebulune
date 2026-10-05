package foo.starred.nebulune.utils

import foo.starred.parallax.api.primitives.ParallaxLine.singular as extractLine
import foo.starred.snowbird.api.client
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.AABB
import foo.starred.parallax.api.primitives.ParallaxBox

fun extractStyledBox(aabb: AABB, color: Int, style: Int, depth: Boolean = true) {
    if (style == 0 || style == 2) ParallaxBox.frame(aabb, color, depth = depth)
    if (style == 1 || style == 2) ParallaxBox.fill(aabb, color, depth = depth)
}

fun extractTracer(to: Vec3, color: Int, lineWidth: Float = 3f, depthTest: Boolean = false) {
    //~ if >= 26.2 'client.gameRenderer.mainCamera' -> 'client.gameRenderer.mainCamera()'
    val camera = client.gameRenderer.mainCamera
    val from = camera.position().add(Vec3.directionFromRotation(camera.xRot(), camera.yRot()))
    extractLine(from.toVector3f(), to.toVector3f(), color, lineWidth, depthTest)
}
