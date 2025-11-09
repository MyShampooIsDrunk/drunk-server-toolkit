package myshampooisdrunk.drunk_server_toolkit.item;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

public abstract class AbstractCustomItem{
    private final ComponentMap.Builder components;
    private final CustomModelDataComponent customModelData;
    private final Identifier customModelId;
    protected final String key;
    protected final Item item;
    protected final Identifier identifier;

    public AbstractCustomItem(Item item, Identifier identifier) {
        this(item,identifier,null, null);
    }
    public AbstractCustomItem(Item item, Identifier identifier, @Nullable String itemName) {
        this(item,identifier,itemName, null);
    }
    protected AbstractCustomItem(Item item, Identifier identifier, String itemName, @Nullable Either<CustomModelDataComponent, Identifier> customModelData) {
        this.identifier = identifier;
        this.key = itemName;
        this.item = item;
        this.components = ComponentMap.builder();
        if(customModelData == null) {
            this.customModelData = null;
            this.customModelId = null;
        } else {
            if(customModelData.left().isPresent()) {
                this.customModelData = customModelData.left().get();
                this.customModelId = null;
            } else if(customModelData.right().isPresent()){
                this.customModelId = customModelData.right().get();
                this.customModelData = null;
            } else {
                this.customModelData = null;
                this.customModelId = null;
            }

        }
//        if(customModelData != null) addComponent(DataComponentTypes.CUSTOM_MODEL_DATA, customModelData);
    }
    public Item getItem(){return item;}
    public Identifier getIdentifier(){return identifier;}

    public NbtCompound getCustomNbt(){
        NbtCompound ret = new NbtCompound();
        ret.putString("custom_item",identifier.toString());
        return ret;
    }

    public ItemStack create(){//put all custom component data like max_stack_size here
        MutableText t = key != null ? Text.translatable(key) : (MutableText)item.getName();
        ItemStack ret = new ItemStack(item);
        ret.set(DataComponentTypes.ITEM_NAME, t);
        ret.set(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(getCustomNbt()));
        if(customModelData != null) {
            ret.set(DataComponentTypes.CUSTOM_MODEL_DATA, customModelData);
        } else if(customModelId != null) ret.set(DataComponentTypes.ITEM_MODEL, customModelId); //holy shit i love this update
        ret.applyComponentsFrom(components.build());
        return ret;
    }

    public boolean hasCustomModel(){
        return customModelData != null;
    }

    public Optional<CustomModelDataComponent> getCustomModel(){
        return Optional.ofNullable(customModelData);
    }

    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, EquipmentSlot slot, CallbackInfo ci) {
    }

    public void onCraft(ItemStack stack, World world, CallbackInfo ci) {
    }

    public void onCraftByPlayer(ItemStack stack, PlayerEntity player, CallbackInfo ci) {
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity entity) {
        ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
        return consumableComponent != null ? consumableComponent.getConsumeTicks() : 0;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
    }

    public boolean isUsedOnRelease(ItemStack stack) {
        return false;
    }

    public void postHit(ItemStack stack, Entity target, LivingEntity attacker, CallbackInfo ci) {
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
    }
    //idk if i wanna code these in yet cuz i feel like they could be buggy since a lot of that shit is also done client-side
//    public int getItemBarColor(ItemStack stack) {
//        int i = stack.getMaxDamage();
//        float f = Math.max(0.0F, ((float)i - (float)stack.getDamage()) / (float)i);
//        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
//    }
//    public int getItemBarStep(ItemStack stack) {
//        return MathHelper.clamp(Math.round(13.0F - (float)stack.getDamage() * 13.0F / (float)stack.getMaxDamage()), 0, 13);
//    }
//    public boolean isItemBarVisible(ItemStack stack) {
//        return stack.isDamaged();
//    }
    public void onSneak(boolean sneaking, PlayerEntity player, CallbackInfo ci){
    }

    public void whileSneak(PlayerEntity p, CallbackInfo ci){
    }

    public void onJump(PlayerEntity p, CallbackInfo ci){
    }

    //cir is here in case you don't wanna override normal right click
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
//        FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
//        cir.setReturnValue(foodComponent != null ? user.eatFood(world, stack, foodComponent) : stack);
    }

    public void use(World world, LivingEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    }

    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        cir.setReturnValue(ActionResult.PASS);
    }

    public void onEntityInteraction(LivingEntity user, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir){
    }

    public void onItemEntityDestroyed(ItemEntity entity, CallbackInfo ci) {
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
    }

    public void postDrop(LivingEntity user, ItemStack stack, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir){
    }

    public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfo ci) {
    }

    //should be run in the initializer of a new custom item if you want custom item attributes and shit
    protected <T> void addComponent(ComponentType<T> type, T val){
        components.add(type, val);
    }
//
//    protected static int getModelId(Item item, boolean customModel){
//        int modelId = -1;
//        if(customModel){
//            if(DST.MODEL_COUNT.containsKey(item)){
//                DST.MODEL_COUNT.put(item, DST.MODEL_COUNT.get(item)+1);
//            }else{
//                DST.MODEL_COUNT.put(item,1);
//            }
//            modelId = DST.MODEL_COUNT.get(item);
//        }
//        return modelId;
//    }
}
