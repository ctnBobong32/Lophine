package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;

@ConfigClassInfo(configAttribution = EnumConfigCategory.MISC, mainName = "villager")
public class VillagerConfig implements IConfigModule {
    @TransformedConfig(name = "villager-infinite-trade", category = {"misc", "villager-config"})
    @ConfigInfo(baseName = "villager-infinite-trade", comments =
            """
                    Allow villager infinite trade (limit of 524288 times)
                        ---- we won't edit saved data, only edit in send data to client.""")
    public static boolean villagerInfiniteTrade = false;
}
