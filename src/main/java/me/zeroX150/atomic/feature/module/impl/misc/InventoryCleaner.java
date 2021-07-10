package me.zeroX150.atomic.feature.module.impl.misc;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;

public class InventoryCleaner extends Module {
    Item[] useless = new Item[]{
            Items.GRANITE,
            Items.POLISHED_GRANITE,
            Items.DIORITE,
            Items.POLISHED_DIORITE,
            Items.ANDESITE,
            Items.POLISHED_ANDESITE,
            Items.DEEPSLATE,
            Items.COBBLED_DEEPSLATE,
            Items.POLISHED_DEEPSLATE,
            Items.CALCITE,
            Items.TUFF,
            Items.DRIPSTONE_BLOCK,
            Items.DIRT,
            Items.COARSE_DIRT,
            Items.PODZOL,
            Items.ROOTED_DIRT,
            Items.SAND,
            Items.RED_SAND,
            Items.GRAVEL,
            Items.WET_SPONGE,
            Items.SANDSTONE,
            Items.CHISELED_SANDSTONE,
            Items.CUT_SANDSTONE,
            Items.GRASS,
            Items.FERN,
            Items.AZALEA,
            Items.FLOWERING_AZALEA,
            Items.DEAD_BUSH,
            Items.SEAGRASS,
            Items.SEA_PICKLE,
            Items.POPPY,
            Items.BLUE_ORCHID,
            Items.ALLIUM,
            Items.AZURE_BLUET,
            Items.RED_TULIP,
            Items.ORANGE_TULIP,
            Items.WHITE_TULIP,
            Items.PINK_TULIP,
            Items.OXEYE_DAISY,
            Items.CORNFLOWER,
            Items.LILY_OF_THE_VALLEY,
            Items.SPORE_BLOSSOM,
            Items.KELP,
            Items.BIG_DRIPLEAF,
            Items.SMALL_DRIPLEAF,
            Items.BAMBOO,
            Items.BRICKS,
            Items.SNOW,
            Items.SNOW_BLOCK,
            Items.CLAY,
            Items.NETHERRACK,
            Items.INFESTED_STONE,
            Items.INFESTED_COBBLESTONE,
            Items.INFESTED_STONE_BRICKS,
            Items.INFESTED_MOSSY_STONE_BRICKS,
            Items.INFESTED_CRACKED_STONE_BRICKS,
            Items.INFESTED_CHISELED_STONE_BRICKS,
            Items.INFESTED_DEEPSLATE,
            Items.VINE,
            Items.GLOW_LICHEN,
            Items.MYCELIUM,
            Items.LILY_PAD,
            Items.NETHER_BRICKS,
            Items.CRACKED_NETHER_BRICKS,
            Items.CHISELED_NETHER_BRICKS,
            Items.NETHER_BRICK_FENCE,
            Items.NETHER_BRICK_STAIRS,
            Items.BRICK_WALL,
            Items.GRANITE_WALL,
            Items.ANDESITE_WALL,
            Items.DIORITE_WALL,
            Items.SUNFLOWER,
            Items.LILAC,
            Items.ROSE_BUSH,
            Items.PEONY,
            Items.TALL_GRASS,
            Items.LARGE_FERN,
            Items.FLINT,
            Items.BRICK,
            Items.EGG,
            Items.GRASS_BLOCK,
            Items.COBBLESTONE,
            Items.WHEAT_SEEDS,
            Items.MELON_SEEDS,
            Items.BEETROOT_SEEDS,
            Items.STRING,
            Items.FEATHER,
            Items.LEATHER
    };

    public InventoryCleaner() {
        super("InventoryCleaner", "Cleans inventory from useless stuff", ModuleType.MISC);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 36; i++) {
            ItemStack s = Atomic.client.player.getInventory().getStack(i);
            if (Arrays.stream(useless).filter(item -> item == s.getItem()).count() != 0) {
                Client.drop(i);
                break;
            }
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

