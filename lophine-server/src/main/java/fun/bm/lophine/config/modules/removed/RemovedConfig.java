package fun.bm.lophine.config.modules.removed;

import me.earthme.luminol.config.EnumConfigCategory;
import me.earthme.luminol.config.IConfigModule;
import me.earthme.luminol.config.flags.ConfigClassInfo;
import me.earthme.luminol.config.flags.ConfigInfo;
import me.earthme.luminol.config.flags.TransformedConfig;

@ConfigClassInfo(configAttribution = EnumConfigCategory.REMOVED, mainName = "removed_config")
public class RemovedConfig implements IConfigModule {
    @TransformedConfig(name = "disable_end_crystal_check", category = {"fixes", "end_crystal"}, transform = false)
    @TransformedConfig(name = "disable_end_crystal_check", category = {"misc", "end_crystal"}, transform = false)
    @TransformedConfig(name = "allow_skip_cooldown", category = {"misc", "revert_raid_changes"}, transform = false)
    @TransformedConfig(name = "bad_omen_infinite", category = {"misc", "revert_raid_changes"}, transform = false)
    @TransformedConfig(name = "skip_height_check", category = {"misc", "revert_raid_changes"}, transform = false)
    @TransformedConfig(name = "skip_self_raid_check", category = {"misc", "revert_raid_changes"}, transform = false)
    @TransformedConfig(name = "use_old_position_find", category = {"misc", "revert_raid_changes"}, transform = false)
    @TransformedConfig(name = "vanilla_hopper", category = {"misc", "redstone"}, transform = false)
    @TransformedConfig(name = "old_replaceable_by_mushrooms", category = {"misc", "old-feature"}, transform = false)
    @TransformedConfig(name = "old_nether_portal_collision", category = {"misc", "old-feature"}, transform = false)
    @TransformedConfig(name = "better_shulker_box", category = {"misc", "container_expansion"}, transform = false)
    @ConfigInfo(baseName = "removed", comments =
            """
                    RemovedConfig redirect to here, no any function.""")
    public static boolean enabled = true;
}