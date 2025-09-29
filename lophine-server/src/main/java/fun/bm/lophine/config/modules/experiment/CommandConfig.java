package fun.bm.lophine.config.modules.experiment;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(category = EnumConfigCategory.EXPERIMENT, name = "command")
public class CommandConfig implements IConfigModule {
    @ConfigInfo(name = "tick_command_enabled", comments =
            """
                    Allow to use tick command""")
    public static boolean tick = false;

    @ConfigInfo(name = "datapack_command_enabled", comments =
            """
                    Allow to use datapack command
                        --- datapack hot-update unsupported, please restart server""")
    public static boolean datapack = false;

    @ConfigInfo(name = "function_command_enabled", comments =
            """
                    Allow to use function command""")
    public static boolean function = false;

    @TransformedConfig(name = "enable-waypoint", directory = {"experiment", "waypoint bar"})
    @TransformedConfig(name = "enable-waypoint", directory = {"experiment", "waypoint_bar"})
    @ConfigInfo(name = "waypoint_command_enabled", comments =
            """
                    Allow to use waypoint command""")
    public static boolean waypoint = false;
    
    @ConfigInfo(name = "scoreboard_command_enabled", comments =
            """
                    Allow to use scoreboard command""")
    public static boolean scoreboard = false;
}