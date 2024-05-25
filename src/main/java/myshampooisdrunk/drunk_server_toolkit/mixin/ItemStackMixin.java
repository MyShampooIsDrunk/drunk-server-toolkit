package myshampooisdrunk.drunk_server_toolkit.mixin;


import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import myshampooisdrunk.drunk_server_toolkit.item.CustomToolItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper.CUSTOM_ENCHANT_KEY;

@Mixin(value = ItemStack.class)
public class ItemStackMixin{
    private final ItemStack dis = (ItemStack)(Object)this;
    @Inject(method = "hasEnchantments",at=@At("TAIL"),cancellable = true)
    public void doesActuallyIsHasEnchantments(CallbackInfoReturnable<Boolean> cir){
        if (dis.getNbt() != null && dis.getNbt().contains(CUSTOM_ENCHANT_KEY, NbtElement.LIST_TYPE)) {
            cir.setReturnValue(!dis.getNbt().getList(CUSTOM_ENCHANT_KEY, NbtElement.COMPOUND_TYPE).isEmpty());
        }
    }

    /**
     * @author my shampoo my shampoo is drunk drunk drunk
     * @reason cuz i gotta rewrite their whole fucking code anyways lol
     */
    @Overwrite
    public float getMiningSpeedMultiplier(BlockState state){
        if(WeaponAPI.ITEMS.containsKey(dis.getItem())) {
            for (AbstractCustomItem custom : WeaponAPI.ITEMS.get(dis.getItem())) {
                if(custom instanceof CustomToolItem item){
                    if(custom.getItem().equals(dis.getItem()) && dis.getOrCreateNbt().getInt("CustomModelData") == custom.getId()){
                        return item.getMiningSpeedMultiplier(dis,state);
                    }
                }
            }
        }
        return dis.getItem().getMiningSpeedMultiplier(dis, state);
    }

    @Inject(method = "isSuitableFor", at = @At("HEAD"),cancellable = true)
    public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(WeaponAPI.ITEMS.containsKey(dis.getItem())) {
            for (AbstractCustomItem custom : WeaponAPI.ITEMS.get(dis.getItem())) {
                if(custom instanceof CustomToolItem item){
                    if(custom.getItem().equals(dis.getItem()) && dis.getOrCreateNbt().getInt("CustomModelData") == custom.getId()){
                        if(item.isSuitableFor(state))cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Inject(method="postMine",at=@At("HEAD"))
    public void postMineForCustomItem(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci){
        if(WeaponAPI.ITEMS.containsKey(dis.getItem())) {
            for (AbstractCustomItem custom : WeaponAPI.ITEMS.get(dis.getItem())) {
                if(custom instanceof CustomToolItem item){
                    if(custom.getItem().equals(dis.getItem()) && dis.getOrCreateNbt().getInt("CustomModelData") == custom.getId()){
                        item.postMine(dis,world,state,pos,miner,ci);
                    }
                }
            }
        }
    }
}
