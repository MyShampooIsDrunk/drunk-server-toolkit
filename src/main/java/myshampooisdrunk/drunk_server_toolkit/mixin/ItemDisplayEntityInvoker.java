package myshampooisdrunk.drunk_server_toolkit.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = {"Lnet/minecraft/entity/decoration/DisplayEntity$ItemDisplayEntity"})
public interface ItemDisplayEntityInvoker {
    @Invoker("setItemStack")
    void invokeSetItemStack(ItemStack stack);
}
