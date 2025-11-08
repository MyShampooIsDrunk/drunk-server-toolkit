package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.slf4j.Marker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {

    @Inject(method = "onPlaced", at=@At("HEAD"))
    public void detectMultiblockStructure(World w, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci){
        if(w instanceof ServerWorld world && !w.isClient() && placer instanceof ServerPlayerEntity) {
            Set<MultiblockStructureType<?>> types = MultiblockRegistry.getPotentialMultiblocks(pos, world);
            for (MultiblockStructureType<?> type : types) {
                if(type.createAtBlock(world, pos)) return;

            }
        }
    }

    @Inject(method = "onBreak", at=@At("RETURN"))
    public void removeMultiblockStructure(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir){
        if(!world.isClient()) {
            Set<MultiblockStructureType<?>> types = MultiblockRegistry.getPotentialMultiblocks(pos, world);
            for (UUID uuid : ((MultiblockCacheI) world).drunk_server_toolkit$getStructuresAtBlock(pos)) {
                MultiblockStructure structure;
                if(types.contains((structure = ((MultiblockCacheI) world).drunk_server_toolkit$getStructure(uuid)).getType()))
                    structure.destroy();
            }
//            Box box = new Box(pos);
//            List<MarkerEntity> cores = new ArrayList<>();
//            world.collectEntitiesByType(MultiblockRegistry.CORE_ENTITY, box.expand(0.05), MarkerEntity::isAlive, cores, 1);
//            if (!cores.isEmpty()) {
//                if(types.contains(cores.getFirst().getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY).getStructureType())) {
//                    ((MultiblockCacheI) world).drunk_server_toolkit$getStructure(cores.getFirst().getUuid()).destroy();
//                }
//            }
        }
    }

    public BlockMixin(Settings settings) {
        super(settings);
    }
}
