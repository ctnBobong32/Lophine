package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigInfo;

public class OldFeatureConfig implements IConfigModule {
    @ConfigInfo(baseName = "old_nether_portal_collision")
    public static boolean oldNetherPortalCollision = false;

    @ConfigInfo(baseName = "spawn_invulnerable_time")
    public static boolean spawnInvulnerableTime = false;

    @ConfigInfo(baseName = "old_zombie_reinforcement")
    public static boolean oldZombieReinforcement = false;

    @ConfigInfo(baseName = "old_replaceable_by_mushrooms")
    public static boolean oldReplaceableByMushrooms = false;

    @Override
    public EnumConfigCategory getCategory() {
        return EnumConfigCategory.MISC;
    }

    @Override
    public String getBaseName() {
        return "old-feature";
    }
}