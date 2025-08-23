package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;

@ConfigClassInfo(configAttribution = EnumConfigCategory.MISC, mainName = "redstone")
public class RedStoneConfig implements IConfigModule {
    @TransformedConfig(name = "allow_skip_cooldown", category = {"misc", "redstone"})
    @ConfigInfo(baseName = "shears_rotate", comments =
            """
                    Allows you to use the Shears to right-click to rotate the block.""")
    public static boolean shears = false;
}