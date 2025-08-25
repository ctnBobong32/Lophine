package fun.bm.lophine.config.modules.gameplay;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;

@ConfigClassInfo(configAttribution = EnumConfigCategory.GAMEPLAY, mainName = "end-void-ring")
public class EndVoidRingsConfig implements IConfigModule {
    @ConfigInfo(baseName = "enabled", comments = """
            If enabled, it will generate end void rings, like MC-159283.""")
    public static boolean enabled = false;
}
