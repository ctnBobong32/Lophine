package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class ContainerExpansionConfig implements IConfigModule {
    @ConfigInfo(baseName = "barrel_rows", comments =
            """
                    range: 1~6""")
    public static int barrelRows = 3;

    @ConfigInfo(baseName = "enderchest_rows", comments =
            """
                    range: 1~6""")
    public static int enderchestRows = 3;

    @ConfigInfo(baseName = "shulker_stackable_count", comments =
            """
                    range: 1~64""")
    public static int shulkerCount = 1;

    @ConfigInfo(baseName = "same_nbt_shulker_stackable", comments =
            """
                    range: 1~64""")
    public static boolean nbtShulkerStackable = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.MISC;
    }

    @Override
    public String getBaseName() {
        return "container_expansion";
    }
}