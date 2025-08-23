package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;

@ConfigClassInfo(configAttribution = EnumConfigCategory.EXPERIMENT, mainName = "waypoint_bar")
public class WaypointConfig implements IConfigModule {
    @TransformedConfig(name = "enable-waypoint", category = {"experiment", "waypoint bar"})
    @ConfigInfo(baseName = "enabled", comments =
            """
                    Enable waypoint bar unsafe""")
    public static boolean forceEnableWaypointUnsafe = false;
}
