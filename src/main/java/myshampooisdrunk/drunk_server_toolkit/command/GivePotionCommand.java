package myshampooisdrunk.drunk_server_toolkit.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.item.potion.CustomPotion;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class GivePotionCommand {
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(entityName -> Text.stringifiedTranslatable("commands.enchant.failed.entity", entityName));
    private static final DynamicCommandExceptionType FAILED_ITEMLESS_EXCEPTION = new DynamicCommandExceptionType(entityName -> Text.stringifiedTranslatable("commands.enchant.failed.itemless", entityName));
    private static final DynamicCommandExceptionType FAILED_INCOMPATIBLE_EXCEPTION = new DynamicCommandExceptionType(itemName -> Text.stringifiedTranslatable("commands.enchant.failed.incompatible", itemName));
    private static final Dynamic2CommandExceptionType FAILED_ITEM_COUNT_EXCEPTION = new Dynamic2CommandExceptionType((item, maxLevel) -> Text.stringifiedTranslatable("commands.enchant.failed.level", item, maxLevel));
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.enchant.failed"));
    private static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestIdentifiers(DST.POTIONS.keySet(), builder);
    private static final SuggestionProvider<ServerCommandSource> TYPE_SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestMatching(CustomPotion.PotionType.types(), builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("give_potion")
                        .requires(CommandManager.requirePermissionLevel(CommandManager.ADMINS_CHECK)))
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then((CommandManager
                                        .argument("potion", IdentifierArgumentType.identifier())
                                        .suggests(SUGGESTION_PROVIDER)
                                        .then((CommandManager
                                                .argument("type", StringArgumentType.string())
                                                .suggests(TYPE_SUGGESTION_PROVIDER)
                                                .executes(context -> execute(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        IdentifierArgumentType.getIdentifier(context, "potion"),
                                                        StringArgumentType.getString(context,"type"),
                                                        0,0)
                                                ).then(CommandManager.argument("duration_level", IntegerArgumentType.integer(0, 255))
                                                        .executes(context -> execute(
                                                                context.getSource(),
                                                                EntityArgumentType.getPlayers(context, "targets"),
                                                                IdentifierArgumentType.getIdentifier(context, "potion"),
                                                                StringArgumentType.getString(context,"type"),
                                                                IntegerArgumentType.getInteger(context, "duration_level"),0)
                                                        ).then(CommandManager.argument("potency", IntegerArgumentType.integer(0, 255))
                                                                .executes(context -> execute(
                                                                        context.getSource(),
                                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                                        IdentifierArgumentType.getIdentifier(context, "potion"),
                                                                        StringArgumentType.getString(context,"type"),
                                                                        IntegerArgumentType.getInteger(context, "duration_level"),
                                                                        IntegerArgumentType.getInteger(context, "potency")
                                                                )))
                                                        )
                                                )
                                        ))
                                )
                        )
        );
    }

    private static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Identifier id, String typeString, int dur, int pot) throws CommandSyntaxException {
        CustomPotion potion = DST.POTIONS.get(id);
        CustomPotion.PotionType type = CustomPotion.PotionType.fromString(typeString);
        if(potion == null){
            source.sendError(Text.of("Potion " + id + " doesn't exist"));
            return 0;
        }
        ItemStack stack = potion.create(pot, dur, type);

        targets.forEach(t -> t.giveItemStack(stack.copy()));

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.give.success.single", 1, stack.toHoverableText(), targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.give.success.single", 1, stack.toHoverableText(), targets.size()), true);
        }

            return targets.size();

    }
}