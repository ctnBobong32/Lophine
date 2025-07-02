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

    @ConfigInfo(baseName = "datapack_command_enabled", comments =
            """
                    Allow to use datapack command
                        --- datapack hot-update unsupported, please restart server""")
    public static boolean datapack = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.EXPERIMENT;
    }

    @Override
    public String getBaseName() {
        return "command";
    }
}