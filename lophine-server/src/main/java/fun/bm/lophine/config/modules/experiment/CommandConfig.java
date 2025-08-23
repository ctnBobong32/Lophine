package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;

@ConfigClassInfo(configAttribution = EnumConfigCategory.EXPERIMENT, mainName = "command")
public class CommandConfig implements IConfigModule {
    @ConfigInfo(baseName = "tick_command_enabled", comments =
            """
                    Allow to use tick command""")
    public static boolean tick = false;

    @ConfigInfo(baseName = "datapack_command_enabled", comments =
            """
                    Allow to use datapack command
                        --- datapack hot-update unsupported, please restart server""")
    public static boolean datapack = false;
}