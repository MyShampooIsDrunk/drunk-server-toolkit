package myshampooisdrunk.drunk_server_toolkit.example;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.item.CustomToolItem;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class RandomToolItemCustomVeryVeryFastCustomCustomCustomCustom extends CustomToolItem {
    public RandomToolItemCustomVeryVeryFastCustomCustomCustomCustom() {
        super(Items.ACACIA_SLAB, Identifier.of(WeaponAPI.LOGGER.getName(), "they_not_like_us"), "kendr ickl amar",
                ToolMaterials.NETHERITE,1000f,1,
                CustomToolItem.alwaysDrop(1000f, BlockTags.BUTTONS,BlockTags.BANNERS,BlockTags.GOLD_ORES,BlockTags.PICKAXE_MINEABLE,
                        BlockTags.SHOVEL_MINEABLE, BlockTags.ANVIL, BlockTags.WOOL).toArray(new ToolComponent.Rule[]{})
        );
    }

    @Override
    public void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable cir) {
        Vec3d vel = player.getVelocity();
        Vec3d rot = player.getRotationVec(0);
        player.setVelocity(vel.add(rot.multiply(2)));
        HitResult res = player.raycast(25,0,true);
        Vec3d fin = res.getPos();
        TntEntity[] tnts = new TntEntity[16];
        TntEntity[] tnts1 = new TntEntity[16];
        TntEntity[] tnts2 = new TntEntity[16];
        TntEntity[] tnts3 = new TntEntity[16];
        TntEntity[] payload = new TntEntity[64];
        for(int i = 0; i < 64; i++){
            TntEntity tnt = new TntEntity(world,fin.x+0.1,fin.y+0.1,fin.z+0.1,player);
            TntEntity tnt2 = new TntEntity(world,fin.x-0.1,fin.y+0.1,fin.z+0.1,player);
            TntEntity tnt3 = new TntEntity(world,fin.x+0.1,fin.y+0.1,fin.z-0.1,player);
            TntEntity tnt4 = new TntEntity(world,fin.x-0.1,fin.y+0.1,fin.z-0.1,player);
//            tnt.setFuse(20);
//            tnt2.setFuse(20);
//            tnt3.setFuse(20);
//            tnt4.setFuse(20);
            switch(i % 4){
                case 0 -> tnts[i%16] = tnt;
                case 1 -> tnts1[i%16] = tnt2;
                case 2 -> tnts2[i%16] = tnt3;
                case 3 -> tnts3[i%16] = tnt4;
            }
            TntEntity tnt5 = new TntEntity(world,fin.x,fin.y-0.1,fin.z,player);
            tnt5.setFuse(2+i%3);
            payload[i] = tnt5;
        }
        for(int i = 0; i < 16; i++){
            world.spawnEntity(tnts[i]);
            world.spawnEntity(tnts1[i]);
            world.spawnEntity(tnts2[i]);
            world.spawnEntity(tnts3[i]);
        }
        for(int i = 0; i < 64; i++){
            world.spawnEntity(payload[i]);
        }
        cir.setReturnValue(TypedActionResult.pass(player.getStackInHand(hand)));
    }

    @Override
    public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfo ci){
        Text t = Text.of("I HATE YOU I HATE YOU I HATE YOU I HATE YOU");
        List<Text> t2 = t.getWithStyle((Style.EMPTY.withItalic(false).withBold(true).withColor(Colors.LIGHT_RED)));
        t2.forEach(text -> {
            if (miner instanceof PlayerEntity p) p.sendMessage(text, true);
        });
        final int vel = 2;
        final int oY = 5;//offsetY
        TntEntity tnt1 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt2 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt3 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt4 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt5 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt6 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt7 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity tnt8 = new TntEntity(world,pos.getX(),pos.getY()+oY,pos.getZ(),miner);
        TntEntity[] tnts = new TntEntity[]{tnt1,tnt2,tnt3,tnt4,tnt5,tnt6,tnt7,tnt8};
        for (TntEntity tnt : tnts) {
            tnt.setFuse(40);
        }
        for(int i = 0; i < 8; i++){
            // 1 rt2 0 -rt2 -1 -rt2 0 rt2
            // 0 -rt2 -1 -rt2 0 rt2 1 rt2
            double dx = i % 2 == 1 ? Math.sqrt(2) : i % 3 == 2 ? 1:0;
            if(i % 7 < 3)dx*=-1;
            int n = (7-i)%8;
            double dy = n % 2 == 1 ? Math.sqrt(2) : n % 3 == 2 ? 1:0;
            if(n % 7 < 3)dy*=-1;
            tnts[i].setVelocity(dx*vel,0.2,dy*vel);
        }
        for (TntEntity tnt : tnts) {
            world.spawnEntity(tnt);
        }
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            if(stack.isDamageable())
                stack.damage(1, miner, EquipmentSlot.MAINHAND);
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
        if(entity instanceof PlayerEntity p && !world.isClient()){
            if(selected){
                p.setStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10, 3), p);
            }else {
                p.setStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 10, 3), p);
            }
        }
    }
}
