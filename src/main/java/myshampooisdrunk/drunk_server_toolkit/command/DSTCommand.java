package myshampooisdrunk.drunk_server_toolkit.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class DSTCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("dst")
                .requires(CommandManager.requirePermissionLevel(2))
                .then(CommandManager.literal("structures")
                        .executes(context -> executeStructures(context.getSource()))
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                                .executes(context -> executeStructures(
                                        context.getSource(),
                                        EntityArgumentType.getEntity(context, "target")))
                        )
                ).then(CommandManager.literal("entities")
                        .executes(context -> executeEntities(context.getSource()))
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                                .executes(context -> executeEntities(
                                        context.getSource(),
                                        EntityArgumentType.getEntity(context, "target")))
                        )
                )
        );
    }

    private static int executeStructures(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        MultiblockCacheI cache = ((MultiblockCacheI) world);
        MutableText message = Text.empty();

        for (UUID uuid : cache.drunk_server_toolkit$listMultiblocks()) {
            MultiblockStructure structure = cache.drunk_server_toolkit$getStructure(uuid);
            message.append(Text.literal("Multiblock with UUID ")
                    .append(Texts.bracketedCopyable(uuid.toString()).withColor(Colors.GREEN))
                    .append(" of type ").append(Text.literal(structure.getType().toString()).withColor(Colors.GREEN)));
            BlockPos pos = structure.getCore().getEntity().getBlockPos();
            Text text = Texts.bracketed(Text.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ()))
                    .styled(
                            style -> style.withColor(Formatting.GREEN)
                                    .withClickEvent(new ClickEvent.SuggestCommand("/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                                    .withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.coordinates.tooltip")))
                    );
            message.append(" \n - Position: ").append(text);
            message.append("\n");
        }

        source.sendMessage(message);
        return 1;
    }

    private static int executeStructures(ServerCommandSource source, Entity entity) {
        ServerWorld world = source.getWorld();
        MultiblockCacheI cache = ((MultiblockCacheI) world);
        MutableText message = Text.empty();

        if(!cache.drunk_server_toolkit$containsUuid(entity.getUuid())) {
            source.sendError(Text.literal("Multiblock entity with UUID " + entity.getUuid() + " could not be found."));
            return 0;
        }

        MultiblockStructure structure = cache.drunk_server_toolkit$getStructure(cache.drunk_server_toolkit$getMultiblockEntity(entity.getUuid()).getCoreUuid());

        if(structure == null) {
            source.sendError(Text.literal("Entity with UUID " + entity.getUuid() + " is not linked to a loaded structure."));
            return 0;
        }

        message.append(Text.literal("Multiblock with UUID ")
                .append(Text.literal(entity.getUuid().toString()).withColor(Colors.GREEN))
                .append(" of type ").append(Text.literal(structure.getType().toString()).withColor(Colors.GREEN)));
        BlockPos pos = structure.getCore().getEntity().getBlockPos();
        Text text = Texts.bracketed(Text.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ()))
                .styled(
                        style -> style.withColor(Formatting.GREEN)
                                .withClickEvent(new ClickEvent.SuggestCommand("/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                                .withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.coordinates.tooltip")))
                );
        message.append(" \n - Position: ").append(text);
        message.append("\n\nLinked Entities").styled(s -> s.withUnderline(true).withBold(true)).append("\n");

        for (UUID uuid : structure.getLinkedEntities()) {
            message.append("\n").append(getEntityText(source, uuid, false)).append("\n");
        }

        source.sendMessage(message);
        return 1;
    }

    private static int executeEntities(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        MultiblockCacheI cache = ((MultiblockCacheI) world);
        MutableText message = Text.empty();

        for (UUID uuid : cache.drunk_server_toolkit$listMultiblockEntities()) {
            Text temp = getEntityText(source, uuid, false);
            message.append(temp).append("\n");
        }

        source.sendMessage(message);
        return 1;
    }

    private static int executeEntities(ServerCommandSource source, Entity entity) {
        source.sendMessage(getEntityText(source, entity.getUuid(), true));
        return 1;
        //Text.literal("Multiblock entity with UUID " + uuid + " could not be found.")

    }

    private static Text getEntityText(ServerCommandSource source, UUID uuid, boolean expanded) {
        ServerWorld world = source.getWorld();
        MultiblockCacheI cache = ((MultiblockCacheI) world);
        MultiblockEntity<?,?> entity = cache.drunk_server_toolkit$getMultiblockEntity(uuid);
        BlockPos pos = entity.getEntity().getBlockPos();
        Text text = Texts.bracketed(Text.translatable("chat.coordinates", pos.getX(), pos.getY(), pos.getZ()))
                .styled(
                        style -> style.withColor(Formatting.GREEN)
                                .withClickEvent(new ClickEvent.SuggestCommand("/tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                                .withHoverEvent(new HoverEvent.ShowText(Text.translatable("chat.coordinates.tooltip")))
                );
        MutableText message = Text.literal("Multiblock Entity with UUID ").append(Texts.bracketedCopyable(uuid.toString()).withColor(Colors.GREEN))
                .append(" of type ").append(Text.literal(entity.getType().toString()).withColor(Colors.GREEN));
        message = message.append(" \n - Position: ").append(text).append("\n");
        if (expanded) {
            message.append("\n - Attached to Multiblock Structure with UUID ").append(Texts.bracketedCopyable(entity.getCoreUuid().toString()).withColor(Colors.GREEN));
        }
        return message;
    }
}

/*
options:
get all structures -> list uuids of all structures (dst structures)
get specific structure data -> core uuid, linked entities' uuids (dst structures <UUID>)
get all entities -> list uuids of all entities (dst entities)
get data of specific entity -> (dst entities <UUID>)
*/