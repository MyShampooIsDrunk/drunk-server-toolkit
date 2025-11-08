package myshampooisdrunk.drunk_server_toolkit.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Predicate;

public class BlockUtil {

    public static Predicate<BlockState> simpleLookup(Block block) {
        return s -> s.isOf(block);
    }

    public static Predicate<BlockState> tagLookup(TagKey<Block> tag) {
        return s -> s.isIn(tag);
    }

    public static Predicate<BlockState> listLookup(RegistryEntryList<Block> blocks) {
        return s -> s.isIn(blocks);
    }

    public static Predicate<BlockState> tagLookup(TagKey<Block> tag, Predicate<AbstractBlock.AbstractBlockState> predicate) {
        return s -> s.isIn(tag, predicate);
    }
}
