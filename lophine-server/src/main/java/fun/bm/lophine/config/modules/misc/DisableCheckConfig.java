package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(category = EnumConfigCategory.MISC, name = "disable-check")
public class DisableCheckConfig implements IConfigModule {
    @ConfigInfo(name = "disable-op-move-check", comments = """
            Disable the check for the operator's move check""")
    public static boolean disableOpMoveCheck = false;

    @ConfigInfo(name = "disable-op-fly-check", comments = """
            Disable the check for the operator's fly check""")
    public static boolean disableOpFlyCheck = false;
}
