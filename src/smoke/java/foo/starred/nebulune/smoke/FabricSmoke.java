package foo.starred.nebulune.smoke;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.util.Arrays;

/** Exercises production mixin transformation without login or game startup. */
public final class FabricSmoke implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        if (!Boolean.getBoolean("nebulune.smoke")) throw new IllegalStateException("Development checks only");
        String[] targets = {
            "net.minecraft.client.Camera",
            "net.minecraft.world.entity.player.Inventory",
            "foo.starred.athen.modules.impl.general.WardrobeKeybinds",
            "foo.starred.athen.modules.impl.general.LoadoutKeybinds",
            "foo.starred.athen.modules.impl.kuudra.StunHelper",
            "foo.starred.athen.modules.impl.slayer.SlayerHighlight",
            "foo.starred.athen.modules.impl.render.highlight.MobHighlight",
            "foo.starred.athen.modules.impl.dungeon.terminals.solver.base.ITerminalSolver"
        };
        try {
            ClassLoader loader = getClass().getClassLoader();
            for (String target : targets) {
                Class<?> transformed = Class.forName(target, false, loader);
                String expected = target.endsWith("Inventory") ? "getSelectedSlot" : "nebulune$";
                if (Arrays.stream(transformed.getDeclaredMethods()).noneMatch(m -> m.getName().contains(expected))) {
                    throw new AssertionError("Nebulune mixin was not applied to " + target);
                }
                System.out.println("NEBULUNE_SMOKE_TRANSFORMED " + target);
            }
            System.out.println("NEBULUNE_SMOKE_PASSED");
            System.exit(0);
        } catch (Throwable error) {
            error.printStackTrace();
            System.exit(1);
        }
    }
}
