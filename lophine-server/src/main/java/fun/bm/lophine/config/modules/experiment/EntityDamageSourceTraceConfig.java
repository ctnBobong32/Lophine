package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class EntityDamageSourceTraceConfig implements IConfigModule {
    @ConfigInfo(baseName = "enabled", comments =
            """
                    Allow trace damage source cross different Region Scheduler.""")
    public static boolean enabled = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.EXPERIMENT;
    }

    @Override
    public String getBaseName() {
        return "entity_damage_source_trace";
    }
}