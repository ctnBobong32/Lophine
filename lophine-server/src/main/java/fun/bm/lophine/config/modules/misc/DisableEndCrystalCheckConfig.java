package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class DisableEndCrystalCheckConfig implements IConfigModule {
    @ConfigInfo(baseName = "disable_end_crystal_check", comments =
            """
                    Disable paper's End Crystal position check.
                    It reverts to vanilla respawn dragon logic.""")
    public static boolean disableCheck = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.MISC;
    }

    @Override
    public String getBaseName() {
        return "end_crystal";
    }
}