package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(configAttribution = EnumConfigCategory.EXPERIMENT, mainName = "entity_damage_source_trace")
public class EntityDamageSourceTraceConfig implements IConfigModule {
    @ConfigInfo(baseName = "enabled", comments =
            """
                    Allow trace damage source cross different Region Scheduler.""")
    public static boolean enabled = false;
}