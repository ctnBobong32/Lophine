package fun.bm.lophine.config.modules.function;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

import java.util.List;

@ConfigClassInfo(configAttribution = EnumConfigCategory.FUNCTION, mainName = "survux-protocol")
public class SurvuxProtocolConfig implements IConfigModule {
    @TransformedConfig(name =  "entity-protocol", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "entity-protocol")
    public static boolean entityProtocol = false;

    @TransformedConfig(name =  "hud-logger-protocol", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "hud-logger-protocol")
    public static boolean hudLoggerProtocol = false;

    @TransformedConfig(name =  "hud-metadata-protocol", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "hud-metadata-protocol")
    public static boolean hudMetadataProtocol = false;

    @TransformedConfig(name = "hud-metadata-share-seed", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "hud-metadata-share-seed")
    public static boolean hudMetadataShareSeed = false;

    @TransformedConfig(name ="structure-protocol", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "structure-protocol")
    public static boolean structureProtocol = false;

    @TransformedConfig(name = "hud-enabled-loggers", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "hud-enabled-loggers")
    public static List<String> hudEnabledLoggers = List.of("tps", "mob_caps");

    @TransformedConfig(name = "hud-update-interval", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "hud-update-interval")
    public static int hudUpdateInterval = 1;

    @TransformedConfig(name = "litematics-enabled", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "litematics-enabled")
    public static boolean litematicsEnabled = false;

    @TransformedConfig(name = "litematics-max-nbt-size", category = {"misc", "survux-protocol"})
    @ConfigInfo(baseName = "litematics-max-nbt-size")
    public static int litematicsMaxNbtSize = 2097152;
}
