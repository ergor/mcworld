package no.netb.mc.mcworld.datastructs.minecraft;

import net.querz.nbt.CompoundTag;

public class Block extends CompoundTag {

    public enum BlockId {
        STONE_BLOCK("stone"),
        GRASS_BLOCK("grass_block"),
        WATER_BLOCK("water");

        String namespacedId;
        BlockId(String unNamespacedId) {
            this.namespacedId = "minecraft:" + unNamespacedId;
        }
    }

    private Block(BlockId blockId) {
        super();
        this.putString("Name", blockId.namespacedId);
    }

    public static final Block GRASS = new Block(BlockId.GRASS_BLOCK);
    public static final Block STONE = new Block(BlockId.STONE_BLOCK);
    public static final Block WATER = new Block(BlockId.WATER_BLOCK);
}
