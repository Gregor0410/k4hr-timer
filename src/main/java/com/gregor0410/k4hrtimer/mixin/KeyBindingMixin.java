package com.gregor0410.k4hrtimer.mixin;

import com.gregor0410.k4hrtimer.K4hrTimer;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Shadow @Final private static Map<InputUtil.Key, KeyBinding> keyToBindings;

    @Inject(method = "setKeyPressed", at = @At("TAIL"))
    private static void onPress(InputUtil.Key key, boolean pressed, CallbackInfo ci) throws IOException {
        KeyBinding keyBinding = keyToBindings.get(key);
        if(keyBinding== K4hrTimer.resetBinding && pressed){
            K4hrTimer.reset();
        }
    }
}
