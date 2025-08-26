package fun.bm.lophine.config.modules.function;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(configAttribution = EnumConfigCategory.FUNCTION, mainName = "container_expansion")
public class ContainerExpansionConfig implements IConfigModule {
    @TransformedConfig(name = "barrel_rows", category = {"misc", "container_expansion"})
    @ConfigInfo(baseName = "barrel_rows", comments =
            """
                    range: 1~6""")
    public static int barrelRows = 3;

    @TransformedConfig(name = "enderchest_rows", category = {"misc", "container_expansion"})
    @ConfigInfo(baseName = "enderchest_rows", comments =
            """
                    range: 1~6""")
    public static int enderchestRows = 3;

    @TransformedConfig(name = "shulker_stackable_count", category = {"misc", "container_expansion"})
    @ConfigInfo(baseName = "shulker_stackable_count", comments =
            """
                    range: 1~64""")
    public static int shulkerCount = 1;

    @TransformedConfig(name = "same_nbt_shulker_stackable", category = {"misc", "container_expansion"})
    @ConfigInfo(baseName = "same_nbt_shulker_stackable")
    public static boolean nbtShulkerStackable = false;
}