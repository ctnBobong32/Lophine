package fun.bm.lophine.config.modules.function;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(configAttribution = EnumConfigCategory.FUNCTION, mainName = "language")
public class LanguageConfig implements IConfigModule {
    @TransformedConfig(name = "lang", category = {"optimizations", "language"})
    @ConfigInfo(baseName = "lang", comments = """
            Please use the key from https://minecraft.wiki/w/Language
            Sample of format: en_us zh_cn zh_hk zh_tw""")
    public static String lang = "en_us";
}