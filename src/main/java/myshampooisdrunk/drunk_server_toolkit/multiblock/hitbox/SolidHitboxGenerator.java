package myshampooisdrunk.drunk_server_toolkit.multiblock.hitbox;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockHitboxEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.util.BoxPolyhedronDecomposer;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public record SolidHitboxGenerator(Set<Box> hitboxes, Map<Box, BlockState> blocks){
    public Map<Vec3d, List<MultiblockHitboxEntity<?, ?>>> get(World world, Vec3d relative) {
        Map<Vec3d, List<MultiblockHitboxEntity<?, ?>>> map = new HashMap<>();
//        Multimap<Vec3d, MultiblockHitboxEntity<?,?>> ret = HashMultimap.create();

        HashMap<BlockState,Set<Box>> stateBoxes = new HashMap<>();
        for (Box box : blocks.keySet()) {
            BlockState state = blocks.get(box);
            Set<Box> b = stateBoxes.computeIfAbsent(blocks.get(box), __ -> new HashSet<>());
//            if(!stateBoxes.containsKey(state = blocks.get(box)))
//                stateBoxes.put(state, new HashSet<>());
            b.add(box);
            b.addAll(stateBoxes.get(state));
            stateBoxes.put(state, b);
        }

        stateBoxes.forEach((s,bs) -> {
            Map<Vec3d, Double> i = BoxPolyhedronDecomposer.decomposer(bs).decompose();
            i.forEach((v,d) -> {
                Vec3d pos = v.subtract(0.5).add(relative);
                List<MultiblockHitboxEntity<?, ?>> ents = map.computeIfAbsent(pos, (__) -> new ArrayList<>());

                MultiblockHitboxEntity.SolidHitboxEntity solid = MultiblockRegistry.SOLID_HITBOX_ENTITY.create(world);
                assert solid != null;
                solid.setSize(d.floatValue());
                ents.add(solid);
//                ret.put(pos, solid);

                MultiblockHitboxEntity.BlockHitboxEntity block = MultiblockRegistry.BLOCK_HITBOX_ENTITY.create(world);
                assert block != null;
                block.setSize(d.floatValue());
                block.setBlock(s);
//                ret.put(pos, block);
                ents.add(block);
                map.put(pos, ents);
            });
        });

        return map;
    }

    public static class Builder {
        private final Set<Box> hitboxes;
        private final Map<Box, BlockState> blocks;

        public Builder(){
            this.hitboxes = new HashSet<>();
            this.blocks = new HashMap<>();
        }

        public Builder add(Box hitbox) {
            hitboxes.add(hitbox);
            return this;
        }

        public Builder add(Box... hitboxes) {
            this.hitboxes.addAll(List.of(hitboxes));
            return this;
        }

        public Builder add(Box hitbox, BlockState block) {
            blocks.put(hitbox, block);
            return this;
        }

        public SolidHitboxGenerator build() {
            if(hitboxes.isEmpty() && blocks.isEmpty()) throw new RuntimeException("Attempted building SolidHitboxGenerator with no added hitboxes");
            return new SolidHitboxGenerator(hitboxes, blocks);
        }
    }
}
