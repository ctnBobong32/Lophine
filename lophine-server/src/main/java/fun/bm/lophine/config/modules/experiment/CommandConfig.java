package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class CommandConfig implements IConfigModule {
    @ConfigInfo(baseName = "command_block_enabled", comments =
            """
                    Allow to use command block""")
    public static boolean block = false;

    @ConfigInfo(baseName = "tick_command_enabled", comments =
            """
                    Allow to use tick command""")
    public static boolean tick = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.EXPERIMENT;
    }

    @Override
    public String getBaseName() {
        return "command";
    }
}