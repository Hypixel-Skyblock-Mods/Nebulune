package foo.starred.nebulune.mixin.mixins.athen;

import com.mojang.blaze3d.platform.InputConstants;
import foo.starred.athen.mixin.accessors.KeyMappingAccessor;
import foo.starred.athen.modules.impl.general.LoadoutKeybinds;
import foo.starred.kbus.data.event.traits.KBusCancellableTrait;
import foo.starred.nebulune.accessors.EquipmentKeybindsAccessor;
import foo.starred.nebulune.modules.impl.general.LoadoutHelper;
import foo.starred.snowbird.api.ClientKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadoutKeybinds.class)
public abstract class LoadoutKeybindsMixin implements EquipmentKeybindsAccessor {
    @Shadow private boolean getUseHotbar() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey0() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey1() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey2() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey3() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey4() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey5() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey6() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey7() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey8() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey9() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey10() { throw new AssertionError(); }
    @Shadow private InputConstants.Key getKey11() { throw new AssertionError(); }
    @Override
    public int nebulune$slotForKey(InputConstants.Key key) {
        InputConstants.Key[] keys = new InputConstants.Key[]{getKey0(), getKey1(), getKey2(), getKey3(), getKey4(), getKey5(), getKey6(), getKey7(), getKey8(), getKey9(), getKey10(), getKey11()};
        for (int index = 0; index < keys.length; index++) {
            InputConstants.Key binding = keys[index];
            if (getUseHotbar()) {
                if (index >= 9) continue;
                binding = ((KeyMappingAccessor) ClientKt.getClient().options.keyHotbarSlots[index]).getBoundKey();
            }
            if (!binding.equals(InputConstants.UNKNOWN) && binding.equals(key)) return 14 + (index / 3) * 9 + index % 3;
        }
        return -1;
    }

    @Inject(method = "fn", at = @At(value = "INVOKE", target = "Lfoo/starred/athen/utils/PlayerUtilsKt;guiClick$default(IIILnet/minecraft/world/inventory/ContainerInput;ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private void nebulune$fn(KBusCancellableTrait event, InputConstants.Key key, CallbackInfo ci) {
        if (LoadoutHelper.INSTANCE.getAutoClose()) LoadoutHelper.close(1);
    }
}