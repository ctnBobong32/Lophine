package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class WaypointConfig implements IConfigModule {
    @ConfigInfo(baseName = "enable-waypoint", comments =
            """
                    Enable waypoint bar unsafe""")
    public static boolean forceEnableWaypointUnsafe = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.EXPERIMENT;
    }

    @Override
    public String getBaseName() {
        return "waypoint bar";
    }
}
