package com.ntu.igts.utils;

import com.ntu.igts.constants.Constants;

public class LevelUtil {

    private static int firstLevelExp = 1;
    private static int levelUpStep = 1;

    static {
        firstLevelExp = Integer.valueOf(ConfigManagmentUtil.getConfigProperties(Constants.LEVEL_EXP_FIRST));
        levelUpStep = Integer.valueOf(ConfigManagmentUtil.getConfigProperties(Constants.LEVEL_EXP_STEP));
    }

    public static int getMaxExpForLevel(int level) {
        int maxExp = firstLevelExp;
        for (int i = 1; i < level; i++) {
            maxExp *= levelUpStep;
        }
        return maxExp;
    }
}
