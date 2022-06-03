package com.gregor0410.k4hrtimer;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;


public class K4hrTimer implements ModInitializer {
    public static Path path;
    public static KeyBinding resetBinding;

    public static void onComplete(InGameTimer timer) throws IOException {
        TimerState timerState = TimerState.getTimerState(path);
        long rtaMillis = timer.getRealTimeAttack();
        Instant startTime = Instant.now().minusMillis(rtaMillis);
        timerState.addRun(new TimerState.Run(startTime,Instant.now(),timer.getInGameTime()));
        timerState.save(path);
    }

    public static void reset() throws IOException {
        Files.deleteIfExists(path);
        TimerState.getTimerState(path).save(path);
    }

    @Override
    public void onInitialize() {
        path = Paths.get(SystemUtils.getUserHome().getPath().concat("/speedrunigt/k4hr.json"));
        resetBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "k4hr-timer.reset",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "k4hr-timer.category"));

    }
}
