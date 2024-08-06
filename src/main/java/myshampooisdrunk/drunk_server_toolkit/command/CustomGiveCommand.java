package myshampooisdrunk.drunk_server_toolkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CustomGiveCommand {
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(entityName -> Text.stringifiedTranslatable("commands.enchant.failed.entity", entityName));
    private static final DynamicCommandExceptionType FAILED_ITEMLESS_EXCEPTION = new DynamicCommandExceptionType(entityName -> Text.stringifiedTranslatable("commands.enchant.failed.itemless", entityName));
    private static final DynamicCommandExceptionType FAILED_INCOMPATIBLE_EXCEPTION = new DynamicCommandExceptionType(itemName -> Text.stringifiedTranslatable("commands.enchant.failed.incompatible", itemName));
    private static final Dynamic2CommandExceptionType FAILED_ITEM_COUNT_EXCEPTION = new Dynamic2CommandExceptionType((item, maxLevel) -> Text.stringifiedTranslatable("commands.enchant.failed.level", item, maxLevel));
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.enchant.failed"));
    private static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> {
        return CommandSource.suggestIdentifiers(WeaponAPI.ITEMS.stream().map(AbstractCustomItem::getIdentifier), builder);
    };

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("give_custom")
                        .requires(source -> source.hasPermissionLevel(2)))
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then((ArgumentBuilder<ServerCommandSource, ?>)((RequiredArgumentBuilder)CommandManager
                                        .argument("custom_item", IdentifierArgumentType.identifier())
                                        .suggests(SUGGESTION_PROVIDER)
                                        .executes(context -> execute(
                                                (ServerCommandSource)context.getSource(),
                                                EntityArgumentType.getPlayers(context, "targets"),
                                                IdentifierArgumentType.getIdentifier(context, "custom_item"), 1)
                                        ))
                                        .then(CommandManager.argument("count", IntegerArgumentType.integer(0))
                                                .executes(context -> execute(
                                                        (ServerCommandSource)context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        IdentifierArgumentType.getIdentifier(context, "custom_item"),
                                                        IntegerArgumentType.getInteger(context, "count")
                                                ))
                                        )
                                )
                        )
        );
    }

    private static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Identifier id, int count) throws CommandSyntaxException {
        AbstractCustomItem item = null;
        for (AbstractCustomItem i : WeaponAPI.ITEMS) {
            if(i.getIdentifier().equals(id)) {
                item = i;
                break;
            }
        }
        if(item == null){
            source.sendError(Text.of("Item " + id + " doesn't exist"));
        }
        int i = item.create().getMaxCount();
        int j = i * 100;
        ItemStack itemStack = item.create().copyWithCount(count);
        if (count > j) {
            source.sendError(Text.translatable("commands.give.failed.toomanyitems", new Object[]{j, itemStack.toHoverableText()}));
            return 0;
        } else {
            Iterator var7 = targets.iterator();

            label44:
            while(var7.hasNext()) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var7.next();
                int k = count;

                while(true) {
                    while(true) {
                        if (k <= 0) {
                            continue label44;
                        }

                        int l = Math.min(i, k);
                        k -= l;
                        ItemStack itemStack2 = item.create().copyWithCount(l);
                        boolean bl = serverPlayerEntity.getInventory().insertStack(itemStack2);
                        ItemEntity itemEntity;
                        if (bl && itemStack2.isEmpty()) {
                            itemStack2.setCount(1);
                            itemEntity = serverPlayerEntity.dropItem(itemStack2, false);
                            if (itemEntity != null) {
                                itemEntity.setDespawnImmediately();
                            }

                            serverPlayerEntity.getWorld().playSound((PlayerEntity)null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            serverPlayerEntity.currentScreenHandler.sendContentUpdates();
                        } else {
                            itemEntity = serverPlayerEntity.dropItem(itemStack2, false);
                            if (itemEntity != null) {
                                itemEntity.resetPickupDelay();
                                itemEntity.setOwner(serverPlayerEntity.getUuid());
                            }
                        }
                    }
                }
            }

            if (targets.size() == 1) {
                source.sendFeedback(() -> {
                    return Text.translatable("commands.give.success.single", new Object[]{count, itemStack.toHoverableText(), ((ServerPlayerEntity)targets.iterator().next()).getDisplayName()});
                }, true);
            } else {
                source.sendFeedback(() -> {
                    return Text.translatable("commands.give.success.single", new Object[]{count, itemStack.toHoverableText(), targets.size()});
                }, true);
            }

            return targets.size();
        }
    }
}
