package fun.bm.lophine.config.modules.misc;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;

@ConfigClassInfo(configAttribution = EnumConfigCategory.MISC, mainName = "old_feature")
public class OldFeatureConfig implements IConfigModule {
    @ConfigInfo(baseName = "spawn_invulnerable_time")
    public static boolean spawnInvulnerableTime = false;

    @ConfigInfo(baseName = "old_zombie_reinforcement")
    public static boolean oldZombieReinforcement = false;

    @ConfigInfo(baseName = "old_explosion_damage_calculator")
    public static boolean oldExplosionDamageCalculator = false;

    @TransformedConfig(name = "give_bad_omen_when_kill_raid_captain", category = {"misc", "revert_raid_changes"}, transformComments = false)
    @ConfigInfo(baseName = "old_raid_behavior")
    public static boolean oldRaidBehavior = false;
}