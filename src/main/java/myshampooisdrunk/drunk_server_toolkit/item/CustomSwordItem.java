package myshampooisdrunk.drunk_server_toolkit.item;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public abstract class CustomSwordItem extends AbstractCustomItem{
    protected final List<EntityGroup> bonusDamage = Lists.newArrayList();
    public CustomSwordItem(SwordItem item, String path, Logger logger, String name){
        this(item, new Identifier(logger.getName(), path), name);
    }
    public CustomSwordItem(SwordItem item, String path, Logger logger){
        this(item, new Identifier(logger.getName(), path));
    }
    public CustomSwordItem(SwordItem item, Identifier identifier) {
        this(item,identifier, (String) null);
    }
    public CustomSwordItem(SwordItem item, Identifier identifier, @Nullable String itemName){
        this(item, identifier,WeaponAPI.ITEM_COUNT.getOrDefault(item,0)+1,itemName);
        if(WeaponAPI.ITEM_COUNT.containsKey(item)){
            WeaponAPI.ITEM_COUNT.put(item,WeaponAPI.ITEM_COUNT.get(item)+1);
        }else{
            WeaponAPI.ITEM_COUNT.put(item,1);
        }

    }

    public CustomSwordItem(SwordItem item, String path, Logger logger, String name, EntityGroup... groups){
        this(item, new Identifier(logger.getName(), path), name);
    }
    public CustomSwordItem(SwordItem item, String path, Logger logger, EntityGroup... groups){
        this(item, new Identifier(logger.getName(), path),groups);
    }
    public CustomSwordItem(SwordItem item, Identifier identifier, EntityGroup... groups) {
        this(item,identifier,null,groups);
    }
    public CustomSwordItem(SwordItem item, Identifier identifier, @Nullable String itemName, EntityGroup... groups){
        this(item, identifier,WeaponAPI.ITEM_COUNT.getOrDefault(item,0)+1,itemName,groups);
        if(WeaponAPI.ITEM_COUNT.containsKey(item)){
            WeaponAPI.ITEM_COUNT.put(item,WeaponAPI.ITEM_COUNT.get(item)+1);
        }else{
            WeaponAPI.ITEM_COUNT.put(item,1);
        }

    }
    protected CustomSwordItem(SwordItem item, Identifier identifier, int id, String itemName, EntityGroup... groups) {
        super(item,identifier,id,itemName);
        this.bonusDamage.addAll(List.of(groups));
    }
    public float getBonusDamage(EntityGroup target, float damage){
        //use bonus damage list here; eg: if(bonusDamage.contains(target))return damage * 5;
        return 0;
    }
    public void onAttack(Entity target, PlayerEntity attacker, CallbackInfo ci){
    }
}
