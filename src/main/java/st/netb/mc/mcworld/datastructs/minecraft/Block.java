package st.netb.mc.mcworld.datastructs.minecraft;

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

    public static class Grass extends Block {
        public Grass() {
            super(BlockId.GRASS_BLOCK);
        }
    }

    public static class Stone extends Block {
        public Stone() {
            super(BlockId.STONE_BLOCK);
        }
    }

    public static class StillWater extends Block {
        public StillWater() {
            super(BlockId.WATER_BLOCK);
        }
    }
}
