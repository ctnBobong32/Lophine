package fun.bm.lophine.config.modules.function;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.CommandSuggestions;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(category = EnumConfigCategory.FUNCTION, name = "container_expansion")
public class ContainerExpansionConfig implements IConfigModule {
    @TransformedConfig(name = "barrel_rows", directory = {"misc", "container_expansion"})
    @CommandSuggestions(suggest = {"1", "2", "3", "4", "5", "6"})
    @ConfigInfo(name = "barrel_rows", comments =
            """
                    range: 1~6""")
    public static int barrelRows = 3;

    @TransformedConfig(name = "enderchest_rows", directory = {"misc", "container_expansion"})
    @CommandSuggestions(suggest = {"1", "2", "3", "4", "5", "6"})
    @ConfigInfo(name = "enderchest_rows", comments =
            """
                    range: 1~6""")
    public static int enderchestRows = 3;

    @TransformedConfig(name = "shulker_stackable_count", directory = {"misc", "container_expansion"})
    @CommandSuggestions(suggest = {"1", "2", "32", "64"})
    @ConfigInfo(name = "shulker_stackable_count", comments =
            """
                    range: 1~64""")
    public static int shulkerCount = 1;

    @TransformedConfig(name = "same_nbt_shulker_stackable", directory = {"misc", "container_expansion"})
    @ConfigInfo(name = "same_nbt_shulker_stackable")
    public static boolean nbtShulkerStackable = false;
}