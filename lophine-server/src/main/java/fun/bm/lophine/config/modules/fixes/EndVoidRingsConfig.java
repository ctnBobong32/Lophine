package fun.bm.lophine.config.modules.fixes;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(configAttribution = EnumConfigCategory.FIXES, mainName = "end-void-ring")
public class EndVoidRingsConfig implements IConfigModule {
    @TransformedConfig(name = "enabled", category = {"gameplay", "end_void_rings"})
    @ConfigInfo(baseName = "enabled", comments = """
            If enabled, it will generate end void rings, like MC-159283.""")
    public static boolean enabled = false;
}
