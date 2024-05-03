package myshampooisdrunk.drunk_server_toolkit.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentInstance;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.BiConsumer;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
    private final EnchantmentScreenHandler dis = (EnchantmentScreenHandler) (Object)this;
    private final Property seed = ((EnchantmentScreenHandlerAccessor)dis).getSeed();
    private final Random random = ((EnchantmentScreenHandlerAccessor)dis).getRandom();
    @ModifyArg(method="onButtonClick",at=@At(value = "INVOKE",target = "Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V"))
    private BiConsumer<World, BlockPos> bruhhhWhyThisNoUseEnchantMethod(BiConsumer<World, BlockPos> function, @Share("inv") LocalRef<Inventory> invRef, @Share("player") LocalRef<PlayerEntity> playerRef, @Share("id")LocalIntRef idRef, @Share("rand") LocalRef<Random> randRef){
        int id = idRef.get();
        PlayerEntity player = playerRef.get();
        Inventory inv = invRef.get();
        ItemStack itemStack = inv.getStack(0);
        ItemStack itemStack2 = inv.getStack(1);

        return (World world, BlockPos pos) -> {
            int i = id + 1;
            ItemStack itemStack3 = itemStack;
            Pair<List<CustomEnchantmentInstance>,List<EnchantmentLevelEntry>> pair = generateEnchantments(itemStack3, id, dis.enchantmentPower[id]);
            System.out.println("custom: " + pair.getLeft());
            System.out.println("real: " + pair.getRight());
            if(!pair.getRight().isEmpty() || !pair.getLeft().isEmpty()){
                player.applyEnchantmentCosts(itemStack3, i);
                boolean bl = itemStack3.isOf(Items.BOOK);
                if(bl){
                    itemStack3 = new ItemStack(Items.ENCHANTED_BOOK);
                    NbtCompound nbtCompound = itemStack.getNbt();
                    if (nbtCompound != null){
                        itemStack3.setNbt(nbtCompound.copy());
                    }
                    inv.setStack(0, itemStack3);
                }
                for (EnchantmentLevelEntry enchantmentLevelEntry : pair.getRight()) {
                    if (bl) {
                        EnchantedBookItem.addEnchantment(itemStack3, enchantmentLevelEntry);
                        continue;
                    }
                    itemStack3.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
                }
                for (CustomEnchantmentInstance enchantmentLevelEntry : pair.getLeft()) {
                    if (bl) {
                        CustomEnchantmentHelper.addBookEnchant(itemStack3, enchantmentLevelEntry);
                        continue;
                    }
                    CustomEnchantmentHelper.addCustomEnchantment(itemStack3,enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
                }
                if(!player.getAbilities().creativeMode) {
                    itemStack2.decrement(i);
                    if(itemStack2.isEmpty()) {
                        inv.setStack(1, ItemStack.EMPTY);
                    }
                }
                player.incrementStat(Stats.ENCHANT_ITEM);
                if(player instanceof ServerPlayerEntity) {
                    Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemStack3, i);
                }
                inv.markDirty();

                seed.set(player.getEnchantmentTableSeed());
                this.onContentChanged(inv);
                world.playSound(null, (BlockPos)pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.random.nextFloat() * 0.1f + 0.9f);
            }

        };
    }

    private Pair<List<CustomEnchantmentInstance>,List<EnchantmentLevelEntry>> generateEnchantments(ItemStack stack, int slot, int level) {
        random.setSeed(this.seed.get() + slot);
        Pair<List<CustomEnchantmentInstance>,List<EnchantmentLevelEntry>> list = CustomEnchantmentHelper.generate(random, stack, level, false);
        if (stack.isOf(Items.BOOK) && (list.getLeft().size()) > 1 || list.getRight().size() > 1) {
            int r = random.nextInt();
            if(list.getLeft().size() == 0){
                list.getRight().remove(r%list.getRight().size());
            }else if(list.getRight().size() == 0){
                list.getLeft().remove(r%list.getLeft().size());
            }
            else if(r % 2 == 0){
                list.getRight().remove(r%list.getRight().size());
            }
            else{
                list.getLeft().remove(r%list.getLeft().size());
            }
        }
        return list;
    }

    @Redirect(method="onButtonClick",at=@At(value = "INVOKE",target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;"))
    public ItemStack iActuallywannaDoNothing(Inventory instance, int i, @Share("inv") LocalRef<Inventory> invRef){
        invRef.set(instance);
        return instance.getStack(i);
    }
    @Inject(method="onButtonClick",at=@At("HEAD"))
    public void notTrynaInjectShitTbh(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir, @Share("player") LocalRef<PlayerEntity> playerRef, @Share("id")LocalIntRef idRef){
        idRef.set(id);
        playerRef.set(player);
    }

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }
}
