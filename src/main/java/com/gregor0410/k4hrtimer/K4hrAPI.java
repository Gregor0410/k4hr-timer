package com.gregor0410.k4hrtimer;

import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.category.RunCategory;

public class K4hrAPI implements SpeedRunIGTApi {
    public static final RunCategory K4HR_CATEGORY =
            new RunCategory("k4hr", "mc", "K4HR");
    @Override
    public RunCategory registerCategory() {
        return K4HR_CATEGORY;
    }
}
