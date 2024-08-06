package myshampooisdrunk.drunk_server_toolkit.item;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class CustomToolItem extends AbstractCustomItem{
    private final ToolMaterial material;
    private final List<ToolComponent.Rule> effectiveBlocks;
    private final float miningSpeed;
    private final int damagePerBlock;

    public CustomToolItem(Item item, Identifier identifier,
                          ToolMaterial material, float miningSpeed, int damagePerBlock, ToolComponent.Rule... effective) {
        this(item,identifier,null, false, material, miningSpeed, damagePerBlock, effective);
    }

    public CustomToolItem(Item item, Identifier identifier, @Nullable String itemName,
                          ToolMaterial material, float miningSpeed, int damagePerBlock, ToolComponent.Rule... effective) {
        this(item,identifier,itemName, false, material, miningSpeed, damagePerBlock, effective);
    }

    public CustomToolItem(Item item, Identifier identifier, @Nullable String itemName, boolean customModel,
                          ToolMaterial material, float miningSpeed, int damagePerBlock, ToolComponent.Rule... effective) {
        this(item, identifier,itemName, getModelId(item,customModel), customModel, material, miningSpeed, damagePerBlock, effective);
    }

    protected CustomToolItem(Item item, Identifier identifier, String itemName, int modelId, boolean customModel,
                             ToolMaterial material, float miningSpeed, int damagePerBlock, ToolComponent.Rule... effective) {
        super(item, identifier, itemName, modelId, customModel);
        this.material = material;
        this.effectiveBlocks = Arrays.asList(effective);
        this.miningSpeed = miningSpeed;
        this.damagePerBlock = damagePerBlock;
        ToolComponent tool = new ToolComponent(effectiveBlocks, miningSpeed, damagePerBlock);
        addComponent(DataComponentTypes.TOOL, tool);
    }
    public ToolMaterial getMaterial(){return material;}

    public static List<ToolComponent.Rule> alwaysDrop(Block... blocks){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        rules.add(ToolComponent.Rule.ofAlwaysDropping(List.of(blocks),1));
        return rules;
    }

    @SafeVarargs
    public static List<ToolComponent.Rule> alwaysDrop(TagKey<Block>... tags){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        for (TagKey<Block> tag : tags) {
            rules.add(ToolComponent.Rule.ofAlwaysDropping(tag,1));
        }
        return rules;
    }

    public static List<ToolComponent.Rule> alwaysDrop(Map<TagKey<Block>,Float> tagSpeeds){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        for (Map.Entry<TagKey<Block> , Float> entry : tagSpeeds.entrySet()) {
            rules.add(ToolComponent.Rule.ofAlwaysDropping(entry.getKey(),entry.getValue()));
        }
        return rules;
    }

    public static List<ToolComponent.Rule> alwaysDrop(float speed, Block... blocks){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        rules.add(ToolComponent.Rule.ofAlwaysDropping(List.of(blocks),speed));
        return rules;
    }

    @SafeVarargs
    public static List<ToolComponent.Rule> alwaysDrop(float speed, TagKey<Block>... blocks){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        for (TagKey<Block> tag: blocks) {
            rules.add(ToolComponent.Rule.ofAlwaysDropping(tag, speed));
        }
        return rules;
    }

    public static List<ToolComponent.Rule> alwaysDropBlock(Map<List<Block>,Float> blockSpeeds){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        for (Map.Entry<List<Block> , Float> entry : blockSpeeds.entrySet()) {
            rules.add(ToolComponent.Rule.ofAlwaysDropping(entry.getKey(),entry.getValue()));
        }
        return rules;
    }

    @SafeVarargs
    public static List<ToolComponent.Rule> neverDrop(TagKey<Block>... tags){
        List<ToolComponent.Rule> rules = new ArrayList<>();
        for (TagKey<Block> tag : tags) {
            rules.add(ToolComponent.Rule.ofNeverDropping(tag));
        }
        return rules;
    }
}
