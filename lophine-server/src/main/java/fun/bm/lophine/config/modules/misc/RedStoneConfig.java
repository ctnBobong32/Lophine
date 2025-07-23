package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class RedStoneConfig implements IConfigModule {
    @ConfigInfo(baseName = "allow_skip_cooldown", comments =
            """
                    Allows you to use the Shears to right-click to rotate the block.""")
    public static boolean shears = false;
    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.MISC;
    }

    @Override
    public String getBaseName() {
        return "redstone";
    }
}