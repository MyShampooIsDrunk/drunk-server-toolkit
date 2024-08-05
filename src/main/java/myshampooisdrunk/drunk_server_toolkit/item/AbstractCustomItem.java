package myshampooisdrunk.drunk_server_toolkit.item;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class AbstractCustomItem{
    protected final String key;
    protected final Item item;
    protected final int id;
    protected final Identifier identifier;
    public AbstractCustomItem(Item item, String path, Logger logger, String name){
        this(item, Identifier.of(logger.getName(), path), name);
    }
    public AbstractCustomItem(Item item, String path, Logger logger){
        this(item, Identifier.of(logger.getName(), path));
    }
    public AbstractCustomItem(Item item, Identifier identifier) {
        this(item,identifier,null);
    }
    public AbstractCustomItem(Item item, Identifier identifier, @Nullable String itemName){
        this(item, identifier,WeaponAPI.ITEM_COUNT.getOrDefault(item,0)+1,itemName);
        if(WeaponAPI.ITEM_COUNT.containsKey(item)){
            WeaponAPI.ITEM_COUNT.put(item,WeaponAPI.ITEM_COUNT.get(item)+1);
        }else{
            WeaponAPI.ITEM_COUNT.put(item,1);
        }

    }
    protected AbstractCustomItem(Item item, Identifier identifier, int id, String itemName) {
        this.identifier=identifier;
        this.key=itemName;
        this.item = item;
        this.id = id;
    }
    public void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable cir){
    }
    public void onAttack(Entity target, PlayerEntity attacker, CallbackInfo ci){
    }
    public void onSneak(boolean sneaking, PlayerEntity player, CallbackInfo ci){
    }
    public void whileSneak(PlayerEntity p, CallbackInfo ci){
    }
    public void onClick(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
    }
    public void onDrop(PlayerEntity p, ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfo ci){
    }
    public void onBlockInteraction(){
    }
    public void onEntityInteraction(PlayerEntity user, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir){
    }
    public void onJump(PlayerEntity p, CallbackInfo ci){
    }


    public Item getItem(){return item;}
    public int getId(){return id;}
    public Identifier getIdentifier(){return identifier;}
    public ItemStack create(){//put all custom component data like max_stack_size here
        MutableText t = key != null ? Text.translatable(key) : (MutableText)item.getName();
        ItemStack ret = new ItemStack(item);
        ret.set(DataComponentTypes.ITEM_NAME, t);
        NbtCompound custom = new NbtCompound();
        custom.putInt("custom_item_id:",id);
        ret.set(DataComponentTypes.CUSTOM_DATA,custom);
        return ret;
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
    }
    public void onCraft(ItemStack stack, World world) {
    }
    public UseAction getUseAction(ItemStack stack) {
        return stack.getItem().isFood() ? UseAction.EAT : UseAction.NONE;
    }
    public int getMaxUseTime(ItemStack stack) {
        return 0;
    }
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }
    public boolean isUsedOnRelease(ItemStack stack) {
        return false;
    }

}
