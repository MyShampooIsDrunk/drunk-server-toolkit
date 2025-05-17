package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockData;
import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{
    @Inject(at=@At("HEAD"), method = "damage", cancellable = true)
    public void injectMultiblockEntityDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = this.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.damage(world, source, amount, this, cir);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "takeShieldHit", cancellable = true)
    public void injectMultiblockEntityTakeShieldHit(LivingEntity attacker, CallbackInfo ci){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = this.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.takeShieldHit(attacker, this, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "tryUseDeathProtector", cancellable = true)//why the fuck did they call it a death protector so stupid smh my head smh
    public void injectMultiblockEntityTryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = this.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.tryUseTotem(source, this, cir);
            }
        }
    }

    @Inject(at=@At("HEAD"),method= "jump", cancellable = true)
    private void jump(CallbackInfo ci) {
        if((Entity)this instanceof PlayerEntity user){
            ItemStack item = user.getStackInHand(Hand.MAIN_HAND);
            CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onJump(user,ci));
        }
    }

    public LivingEntityMixin(EntityType<?> type, World world) {super(type, world);}
}
