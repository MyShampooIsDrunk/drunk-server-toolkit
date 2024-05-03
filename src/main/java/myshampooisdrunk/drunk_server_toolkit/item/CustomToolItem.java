package myshampooisdrunk.drunk_server_toolkit.item;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class CustomToolItem extends AbstractCustomItem{
    private final ToolMaterial material;
    private final List<TagKey<Block>> effectiveBlocks;
    private final float miningSpeed;

    public CustomToolItem(Item item, String path, Logger logger, String name, ToolMaterial material, float miningSpeed, TagKey<Block>... effective) {
        super(item, path, logger, name);
        this.material = material;
        effectiveBlocks = Arrays.asList(effective);
        this.miningSpeed = miningSpeed;
    }
    public CustomToolItem(Item item, String path, Logger logger, ToolMaterial material, float miningSpeed, TagKey<Block>... effective) {
        super(item, path, logger);
        this.material = material;
        effectiveBlocks = Arrays.asList(effective);
        this.miningSpeed = miningSpeed;
    }
    public CustomToolItem(Item item, Identifier identifier, ToolMaterial material, float miningSpeed, TagKey<Block>... effective) {
        super(item, identifier);
        this.material = material;
        effectiveBlocks = Arrays.asList(effective);
        this.miningSpeed = miningSpeed;
    }
    public CustomToolItem(Item item, Identifier identifier, @Nullable String itemName, ToolMaterial material, float miningSpeed, TagKey<Block>... effective) {
        super(item, identifier, itemName);
        this.material = material;
        effectiveBlocks = Arrays.asList(effective);
        this.miningSpeed = miningSpeed;
    }
    protected CustomToolItem(Item item, Identifier identifier, int id, String itemName, ToolMaterial material, float miningSpeed, TagKey<Block>... effective) {
        super(item, identifier, id, itemName);
        this.material = material;
        effectiveBlocks = Arrays.asList(effective);
        this.miningSpeed = miningSpeed;
    }

    public List<TagKey<Block>> getEffective(){return effectiveBlocks;}
    public ToolMaterial getMaterial(){return material;}

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        for(TagKey<Block> tag : effectiveBlocks){
            if(state.isIn(tag)) return this.miningSpeed;
        }
        return 1.0f;
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            if(!stack.isDamageable())
                stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
        if (i < MiningLevels.DIAMOND && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        }
        if (i < MiningLevels.IRON && state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        }
        if (i < MiningLevels.STONE && state.isIn(BlockTags.NEEDS_STONE_TOOL)) {
            return false;
        }
        for(TagKey<Block> tag : effectiveBlocks){
            if(state.isIn(tag)) return true;
        }
        return false;
    }

}
