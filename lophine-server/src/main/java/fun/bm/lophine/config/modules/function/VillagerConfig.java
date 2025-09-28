package fun.bm.lophine.config.modules.function;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(category = EnumConfigCategory.MISC, name = "villager")
public class VillagerConfig implements IConfigModule {
    @TransformedConfig(name = "villager-infinite-trade", directory = {"misc", "villager"})
    @TransformedConfig(name = "villager-infinite-trade", directory = {"misc", "villager-config"})
    @ConfigInfo(name = "villager-infinite-trade", comments =
            """
                    Allow villager infinite trade (limit of 524288 times)
                        ---- we won't edit saved data, only edit in send data to client.
                        (after we fixed void trade, this config will drop to void trade)""")
    public static boolean villagerInfiniteTrade = false;
}
