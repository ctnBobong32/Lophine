package fun.bm.lophine.config.modules.optimizations;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;

@ConfigClassInfo(configAttribution = EnumConfigCategory.OPTIMIZATIONS, mainName = "language")
public class LanguageConfig implements IConfigModule {
    @ConfigInfo(baseName = "lang", comments = """
            Please use the key from https://minecraft.wiki/w/Language
            Sample of format: en_us zh_cn zh_hk zh_tw""")
    public static String lang = "en_us";
}