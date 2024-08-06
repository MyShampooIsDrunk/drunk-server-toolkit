package myshampooisdrunk.drunk_server_toolkit.item;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

public abstract class AbstractCustomItem{
    private final ComponentMap.Builder components;

    protected final String key;
    protected final Item item;
    protected final int modelId;
    protected final Identifier identifier;
    protected final boolean customModel;
    private Optional<CustomItemModel> model = Optional.empty();

    public AbstractCustomItem(Item item, Identifier identifier) {
        this(item,identifier,null, false);
    }
    public AbstractCustomItem(Item item, Identifier identifier, @Nullable String itemName) {
        this(item,identifier,itemName, false);
    }
    public AbstractCustomItem(Item item, Identifier identifier, @Nullable String itemName, boolean customModel){
        this(item, identifier,itemName, getModelId(item,customModel), customModel);
    }
    protected AbstractCustomItem(Item item, Identifier identifier, String itemName, int modelId, boolean customModel) {
        this.identifier=identifier;
        this.key=itemName;
        this.item = item;
        this.modelId = modelId;
        this.customModel = customModel;
        this.components = ComponentMap.builder();
    }
    public Item getItem(){return item;}
    public int getModelId(){return modelId;}
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
        if(hasCustomModel()) {
            CustomModelDataComponent model = new CustomModelDataComponent(modelId);
            ret.set(DataComponentTypes.CUSTOM_MODEL_DATA, model);
        }
        ret.set(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(getCustomNbt()));
        ret.applyComponentsFrom(components.build());
//        components.build().forEach(c -> ret.set(Util.forceCast(c.type()),c)); frankly i have no idea how THE FUCK this works, but i dont need it so idc
        return ret;
    }

    public boolean hasCustomModel(){
        return customModel;
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
    }
    public void onCraft(ItemStack stack, World world, CallbackInfo ci) {
    }
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        this.onCraft(stack, world, ci);
    }
    public UseAction getUseAction(ItemStack stack) {
        return stack.contains(DataComponentTypes.FOOD) ? UseAction.EAT : UseAction.NONE;
    }
    public int getMaxUseTime(ItemStack stack, LivingEntity entity) {
        FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
        return foodComponent != null ? foodComponent.getEatTicks() : 0;
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
    public int getItemBarColor(ItemStack stack) {
        int i = stack.getMaxDamage();
        float f = Math.max(0.0F, ((float)i - (float)stack.getDamage()) / (float)i);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
    public int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp(Math.round(13.0F - (float)stack.getDamage() * 13.0F / (float)stack.getMaxDamage()), 0, 13);
    }
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.isDamaged();
    }
    public void onSneak(boolean sneaking, PlayerEntity player, CallbackInfo ci){
    }
    public void whileSneak(PlayerEntity p, CallbackInfo ci){
    }
    public void onJump(PlayerEntity p, CallbackInfo ci){
    }
    //cir is here in case you don't wanna override normal right click
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable cir) {
//        FoodComponent foodComponent = stack.get(DataComponentTypes.FOOD);
//        cir.setReturnValue(foodComponent != null ? user.eatFood(world, stack, foodComponent) : stack);
    }
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable cir) {
    }
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable cir) {
        cir.setReturnValue(ActionResult.PASS);
    }
    public void onEntityInteraction(PlayerEntity user, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir){
    }
    public void postProcessComponents(ItemStack stack, CallbackInfo ci) {
    }
    public void onItemEntityDestroyed(ItemEntity entity, CallbackInfo ci) {
    }
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
    }
    public void postDrop(PlayerEntity user, ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir){
    }
    public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfo ci) {
    }
    //should be run in the initializer of a new custom item if you want custom item attributes and shit
    public <T> void addComponent(ComponentType<T> type, T val){
        components.add(type, val);
    }

    protected static int getModelId(Item item, boolean customModel){
        int modelId = -1;
        if(customModel){
            if(WeaponAPI.MODEL_COUNT.containsKey(item)){
                WeaponAPI.MODEL_COUNT.put(item,WeaponAPI.MODEL_COUNT.get(item)+1);
            }else{
                WeaponAPI.MODEL_COUNT.put(item,1);
            }
            modelId = WeaponAPI.MODEL_COUNT.get(item);
        }
        return modelId;
    }
}
