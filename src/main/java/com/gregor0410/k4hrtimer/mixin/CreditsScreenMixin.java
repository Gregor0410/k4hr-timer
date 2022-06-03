package com.gregor0410.k4hrtimer.mixin;

import com.gregor0410.k4hrtimer.K4hrAPI;
import com.gregor0410.k4hrtimer.K4hrTimer;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import com.redlimerl.speedrunigt.timer.category.RunCategories;
import net.minecraft.client.gui.screen.CreditsScreen;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin {
    @Inject(method = "init()V", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) throws IOException {
        @NotNull
        InGameTimer timer = InGameTimer.getInstance();
        if (timer.getStatus() != TimerStatus.NONE) {
            if (timer.getCategory() == K4hrAPI.K4HR_CATEGORY) {
                InGameTimer.complete();
                K4hrTimer.onComplete(InGameTimer.getInstance());
            }
        }
    }
}
