package fun.bm.lophine.config.modules.function.protocol;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.enums.EnumConfigCategory;
import org.leavesmc.leaves.protocol.syncmatica.SyncmaticaProtocol;

@ConfigClassInfo(category = EnumConfigCategory.FUNCTION, name = "syncmatica", directory = {"protocol"})
public class SyncmaticaProtocolConfig implements IConfigModule {
    @ConfigInfo(name = "enabled", comments = """
            Enable Syncmatica protocol support""")
    public static boolean enabled = false;
    @ConfigInfo(name = "useQuota", comments = """
            Is there a limit on the size of projection files?""")
    public static boolean useQuota = false;
    @ConfigInfo(name = "quota-Limit", comments = """
            Maximum Projection File Size (in bytes)""")
    public static int quotaLimit = 40000000;

    public void onLoaded(CommentedFileConfig configInstance) {
        SyncmaticaProtocol.init(enabled);
    }
}
