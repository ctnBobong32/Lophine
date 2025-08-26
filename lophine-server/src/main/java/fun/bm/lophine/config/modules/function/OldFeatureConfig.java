package fun.bm.lophine.config.modules.function;

import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;
import me.earthme.luminol.enums.EnumConfigCategory;

@ConfigClassInfo(configAttribution = EnumConfigCategory.FUNCTION, mainName = "old-feature")
public class OldFeatureConfig implements IConfigModule {
    @TransformedConfig(name = "spawn_invulnerable_time", category = {"misc", "old-feature"})
    @ConfigInfo(baseName = "spawn_invulnerable_time")
    public static boolean spawnInvulnerableTime = false;

    @TransformedConfig(name = "old_zombie_reinforcement", category = {"misc", "old-feature"})
    @ConfigInfo(baseName = "old_zombie_reinforcement")
    public static boolean oldZombieReinforcement = false;

    @TransformedConfig(name = "old_explosion_damage_calculator", category = {"misc", "old-feature"})
    @ConfigInfo(baseName = "old_explosion_damage_calculator")
    public static boolean oldExplosionDamageCalculator = false;

    @TransformedConfig(name = "old_raid_behavior", category = {"misc", "old-feature"})
    @TransformedConfig(name = "give_bad_omen_when_kill_raid_captain", category = {"misc", "revert_raid_changes"}, transformComments = false)
    @ConfigInfo(baseName = "old_raid_behavior")
    public static boolean oldRaidBehavior = false;
}