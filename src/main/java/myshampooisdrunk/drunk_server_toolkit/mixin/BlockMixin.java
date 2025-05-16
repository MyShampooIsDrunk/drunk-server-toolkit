package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {

    @Shadow public abstract BlockState getDefaultState();

    @Inject(method = "onPlaced", at=@At("HEAD"), cancellable = true)
    public void detectMultiblockStructure(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci){
        if(world instanceof ServerWorld sWorld && !world.isClient() && placer instanceof ServerPlayerEntity) {
            Set<Identifier> checked = new HashSet<>();
            for (Block block : MultiblockRegistry.BLOCKS.keySet()) {
                if (getDefaultState().isOf(block)) {
                    for (Identifier id : MultiblockRegistry.BLOCKS.get(block)) {
                        if (checked.contains(id)) break;
                        checked.add(id);
                        MultiblockStructure str;
                        if ((str = MultiblockRegistry.STRUCTURES.get(id)).detect(sWorld, pos)) {
                            str.generate(sWorld, pos);
                            ci.cancel();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "onBreak", at=@At("RETURN"))
    public void removeMultiblockStructure(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir){
        if(world instanceof ServerWorld sWorld && !world.isClient()) {
            Set<Identifier> checked = new HashSet<>();
            for (Block block : MultiblockRegistry.BLOCKS.keySet()) {
                if (getDefaultState().isOf(block)) {
                    for (Identifier id : MultiblockRegistry.BLOCKS.get(block)) {
                        if (checked.contains(id)) continue;
                        checked.add(id);
                        MultiblockStructure str;
                        if ((str = MultiblockRegistry.STRUCTURES.get(id)).detectBuiltMultiblock(sWorld, pos)) {
                            System.out.println("prebuilt shit alr detected yk");
                            str.remove(sWorld, pos);
                            return;
                        }
                    }
                }
            }
        }
    }

    public BlockMixin(Settings settings) {
        super(settings);
    }
}
