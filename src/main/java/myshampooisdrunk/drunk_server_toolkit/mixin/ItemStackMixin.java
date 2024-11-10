package myshampooisdrunk.drunk_server_toolkit.mixin;


import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStack.class)
public class ItemStackMixin{
    private final ItemStack dis = (ItemStack)(Object)this;

//    @Inject(method = "hasEnchantments",at=@At("TAIL"),cancellable = true)
//    public void doesActuallyIsHasEnchantments(CallbackInfoReturnable<Boolean> cir){
//        if (dis.getNbt() != null && dis.getNbt().contains(CUSTOM_ENCHANT_KEY, NbtElement.LIST_TYPE)) {
//            cir.setReturnValue(!dis.getNbt().getList(CUSTOM_ENCHANT_KEY, NbtElement.COMPOUND_TYPE).isEmpty());
//        }
//    }

    @Inject(method="postMine",at=@At("HEAD"), cancellable = true)
    public void postMineForCustomItem(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci){
        CustomItemHelper.getCustomItem(dis).ifPresent(custom -> custom.postMine(dis,world,state,pos,miner,ci));
    }
}
