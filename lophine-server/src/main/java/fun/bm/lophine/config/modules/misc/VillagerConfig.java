package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class VillagerConfig implements IConfigModule {
    @ConfigInfo(baseName = "villager-infinite-trade", comments =
            """
                    Allow villager infinite trade (limit of 16384 times)
                        ---- we won't edit saved data, only edit in send data to client.""")
    public static boolean villagerInfiniteTrade = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.MISC;
    }

    @Override
    public String getBaseName() {
        return "villager-config";
    }
}
