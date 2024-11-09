package myshampooisdrunk.drunk_server_toolkit.example;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.cooldown.CustomItemCooldownManagerI;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

public class GoofySillyGoofyItem extends AbstractCustomItem {
    public GoofySillyGoofyItem() {
        super(Items.BONE, Identifier.of(WeaponAPI.LOGGER.getName(),"goofy_silly_goofy_item"),"if.this.works.i.will.cry.tears.of.joy");
        addComponent(DataComponentTypes.MAX_STACK_SIZE, 94);
        addComponent(DataComponentTypes.FOOD, new FoodComponent.Builder()
                .alwaysEdible()
                .nutrition(30)
                .saturationModifier(3)
                .usingConvertsTo(Items.NETHERITE_BLOCK)
                .alwaysEdible()
                .build()
        );
        addComponent(DataComponentTypes.FIRE_RESISTANT, Unit.INSTANCE);
    }

    @Override
    public void use(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable cir) {
        if(!((CustomItemCooldownManagerI)player).getCustomItemCooldownManager().isCoolingDown("cooldown1")){
            ((CustomItemCooldownManagerI)player).getCustomItemCooldownManager().set("cooldown1",100);
            PlayerInventory inv = player.getInventory();
            List<Integer> slots = new ArrayList<>(List.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35));
            for(int s = 0; s < 36; s++){
                int n = world.getRandom().nextBetweenExclusive(0,slots.size());
                int s2 = slots.get(n);
                ItemStack one = inv.getStack(s);
                ItemStack two = inv.getStack(s2);
                slots.remove(n);
                inv.setStack(s,two);
                inv.setStack(s2,one);
            }
        }else{
            player.sendMessage(Text.of(create().getName().getString()+ " still has " +
                    (int)(0.95+((CustomItemCooldownManagerI) player).getCustomItemCooldownManager()
                            .getCooldownProgress("cooldown1",0)*5f) + " second(s) left." ),true);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
//        System.out.println("STOPPED USING");
        user.addVelocity(0,((float)(getMaxUseTime(stack, user) - remainingUseTicks))/100f,0);//the longer you use it the longer it takes to finish lol
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
//        System.out.println("THERE IS " + remainingUseTicks + " LEFT");
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity entity) {
        return 100;//5 sec
    }
}
