package me.zeroX150.atomic.feature.module.impl.render.OreSim;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalTwoBlocks;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.ChunkRandom;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class OreSim extends Module {

    private final HashMap<Long, HashMap<Ore.Type, HashSet<Vec3d>>> chunkRenderers = new HashMap<>();
    List<Ore> oreConfig;
    SliderValue chunkRange;
    DynamicValue<String> seedInput;
    MultiValue airCheck;
    MultiValue version;
    String versionString;
    private Long worldSeed = null;
    private ChunkPos prevOffset = new ChunkPos(0, 0);
    private final HashSet<Vec3d> blackList = new HashSet<>();
    public boolean automine = false;

    public OreSim() {
        super("OreSim", "xray on crack", ModuleType.RENDER);
        version = (MultiValue) this.config.create("Version", "1.17.1", "1.14", "1.15", "1.16", "1.17.0", "1.17.1").description("Minecraft version of the world");
        versionString = version.getValue();
        oreConfig = Ore.getConfig(versionString);
        airCheck = (MultiValue) this.config.create("Air-check mode", "Off", "Off", "On load", "Rescan").description("The mode to check placements in the air by");
        seedInput = this.config.create("Seed", "69420").description("The seed of the world used to simulate ore placements");
        chunkRange = (SliderValue) this.config.create("Chunk Range", 5, 0, 10, 10).description("The range of chunks to simulate around the player");

        oreConfig.stream().map(ore -> ore.enabled).distinct().sorted(Comparator.comparingDouble(value ->
                Atomic.fontRenderer.getStringWidth(value.getKey()))).forEach(ore -> this.config.getAll().add(ore.description("Whether or not to simulate " + ore.getKey().toLowerCase())
        ));

        this.config.organizeClickGUIList = false;
    }

    @Override
    public void onWorldRender(MatrixStack ms) {
        if (Atomic.client.player == null) return;
        if (worldSeed != null) {
            int chunkX = Atomic.client.player.getChunkPos().x;
            int chunkZ = Atomic.client.player.getChunkPos().z;

            int rangeVal = chunkRange.getValue().intValue();
            for (int range = 0; range <= rangeVal; range++) {
                for (int x = -range + chunkX; x <= range + chunkX; x++) {
                    renderChunk(x, chunkZ + range - rangeVal, ms);
                }
                for (int x = (-range) + 1 + chunkX; x < range + chunkX; x++) {
                    renderChunk(x, chunkZ - range + rangeVal + 1, ms);
                }
            }
        }

    }

    @Override
    public void onHudRender() {

    }

    private void renderChunk(int x, int z, MatrixStack ms) {
        long chunkKey = (long) x + ((long) z << 32);

        if (chunkRenderers.containsKey(chunkKey)) {
            for (Ore ore : oreConfig) {
                if (ore.enabled.getValue()) {
                    if (!chunkRenderers.get(chunkKey).containsKey(ore.type)) continue;
                    BufferBuilder buffer = Renderer.renderPrepare(ore.color);
                    for (Vec3d pos : chunkRenderers.get(chunkKey).get(ore.type)) {
                        Renderer.renderOutlineIntern(pos, new Vec3d(1, 1, 1), ms, buffer);
                    }
                    buffer.end();
                    BufferRenderer.draw(buffer);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (hasSeedChanged() || hasVersionChanged()) {
            loadVisibleChunks();
        } else if (airCheck.getValue().equals("Rescan")) {
            if (Atomic.client.player == null || Atomic.client.world == null) return;
            long chunkX = Atomic.client.player.getChunkPos().x;
            long chunkZ = Atomic.client.player.getChunkPos().z;
            ClientWorld world = Atomic.client.world;
            int renderdistance = MinecraftClient.getInstance().options.viewDistance;

            //maybe another config option? But its already crowded
            int chunkCounter = 5;

            loop:
            while (true) {
                for (long offsetX = prevOffset.x; offsetX <= renderdistance; offsetX++) {
                    for (long offsetZ = prevOffset.z; offsetZ <= renderdistance; offsetZ++) {
                        prevOffset = new ChunkPos((int) offsetX, (int) offsetZ);
                        if (chunkCounter <= 0) {
                            break loop;
                        }
                        long chunkKey = (chunkX + offsetX) + ((chunkZ + offsetZ) << 32);

                        if (chunkRenderers.containsKey(chunkKey)) {
                            chunkRenderers.get(chunkKey).values().forEach(oreSet ->
                                    oreSet.removeIf(ore ->
                                            !world.getBlockState(new BlockPos((int) ore.x, (int) ore.y, (int) ore.z)).isOpaque())
                            );
                        }
                        chunkCounter--;
                    }
                    prevOffset = new ChunkPos((int) offsetX, -renderdistance);
                }
                prevOffset = new ChunkPos(-renderdistance, -renderdistance);
            }
        }
        if (!automine) return;
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive()) return;

        ArrayList<Vec3d> oreGoals = new ArrayList<>();
        int chunkX = Atomic.client.player.getChunkPos().x;
        int chunkZ = Atomic.client.player.getChunkPos().z;
        int rangeVal = 3;
        for (int range = 0; range <= rangeVal; range++) {
            for (int x = -range + chunkX; x <= range + chunkX; x++) {
                oreGoals.addAll(addToBaritone(x, chunkZ + range - rangeVal));
            }
            for (int x = (-range) + 1 + chunkX; x < range + chunkX; x++) {
                oreGoals.addAll(addToBaritone(x, chunkZ - range + rangeVal + 1));
            }
        }

        Vec3d playerPos = Atomic.client.player.getPos();

        Optional<Vec3d> optGoal = oreGoals.stream().filter(pos -> pos.getY() > 4 && !blackList.contains(pos)).min(Comparator.comparingDouble(pos -> pos.squaredDistanceTo(playerPos)));

        if (optGoal.isPresent()) {
            BlockPos goal = new BlockPos(optGoal.get());
            blackList.add(optGoal.get());
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalTwoBlocks(goal.getX(), goal.getY(), goal.getZ()));
        }
    }

    private ArrayList<Vec3d> addToBaritone(int chunkX, int chunkZ) {
        ArrayList<Vec3d> baritoneGoals = new ArrayList<>();
        long chunkKey = (long) chunkX + ((long) chunkZ << 32);
        if (!chunkRenderers.containsKey(chunkKey)) return baritoneGoals;

        oreConfig.stream().filter(config -> config.enabled.getValue()).forEach(ore -> {
            baritoneGoals.addAll(chunkRenderers.get(chunkKey).getOrDefault(ore.type, new HashSet<>()));
        });
        return baritoneGoals;
    }

    @Override
    public void enable() {
        hasSeedChanged();
        if (worldSeed == null) {
            Client.notifyUser("input a world seed into the seed field in the oresim config");
            setEnabled(false);
        }
        reload();
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    private boolean hasSeedChanged() {
        Long tempSeed;
        try {
            tempSeed = Long.parseLong(seedInput.getValue());
        } catch (Exception e) {
            tempSeed = (long) seedInput.getValue().hashCode();
        }
        if (tempSeed != 69420 && !tempSeed.equals(this.worldSeed)) {
            this.worldSeed = tempSeed;
            chunkRenderers.clear();
            return true;
        }
        return false;
    }

    private boolean hasVersionChanged() {
        if (!versionString.equals(version.getValue())) {
            versionString = version.getValue();
            this.oreConfig = Ore.getConfig(versionString);
            this.config.getAll().removeIf(dynamicValue -> dynamicValue instanceof BooleanValue);
            oreConfig.stream().map(ore -> ore.enabled).distinct().sorted(Comparator.comparingDouble(value ->
                    Atomic.fontRenderer.getStringWidth(value.getKey()))).forEach(ore -> this.config.getAll().add(ore.description("Whether or not to simulate " + ore.getKey().toLowerCase())
            ));
            //update ores in gui. fix this not being called when module is off
            if (ClickGUI.INSTANCE != null)
                ClickGUI.INSTANCE.showModuleConfig(this);
            chunkRenderers.clear();
            return true;
        }
        return false;
    }

    private void loadVisibleChunks() {
        int renderdistance = MinecraftClient.getInstance().options.viewDistance;

        if (Atomic.client.player == null) return;
        int playerChunkX = Atomic.client.player.getChunkPos().x;
        int playerChunkZ = Atomic.client.player.getChunkPos().z;

        for (int i = playerChunkX - renderdistance; i < playerChunkX + renderdistance; i++) {
            for (int j = playerChunkZ - renderdistance; j < playerChunkZ + renderdistance; j++) {
                doMathOnChunk(i, j);
            }
        }
    }

    public void reload() {
        chunkRenderers.clear();
        loadVisibleChunks();
    }

    public void doMathOnChunk(int chunkX, int chunkZ) {
        if (worldSeed == null) {
            this.disable();
            return;
        }
        long chunkKey = (long) chunkX + ((long) chunkZ << 32);

        ClientWorld world = Atomic.client.world;

        if (chunkRenderers.containsKey(chunkKey) || world == null)
            return;

        if (world.getChunkManager().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false) == null) return;

        chunkX = chunkX << 4;
        chunkZ = chunkZ << 4;

        ChunkRandom random = new ChunkRandom();
        HashMap<Ore.Type, HashSet<Vec3d>> h = new HashMap<>();

        long populationSeed = random.setPopulationSeed(worldSeed, chunkX, chunkZ);

        Identifier id = world.getRegistryManager().get(Registry.BIOME_KEY)
                .getId(world.getBiomeAccess().getBiomeForNoiseGen(new ChunkPos(chunkX >> 4, chunkZ >> 4)));
        if (id == null) {
            Client.notifyUser("Something went wrong, you may have some mods that mess with world generation");
            this.setEnabled(false);
            return;
        }
        String biomeName = id.getPath();
        String dimensionName = ((DimensionTypeCaller) world.getDimension()).getInfiniburn().getPath();

        for (Ore ore : oreConfig) {

            if (!dimensionName.endsWith(ore.dimension)) continue;

            HashSet<Vec3d> ores = new HashSet<>();

            int index;
            if (ore.index.containsKey(biomeName)) {
                index = ore.index.get(biomeName);
            } else {
                index = ore.index.get("default");
            }
            if (index < 0)
                continue;

            random.setDecoratorSeed(populationSeed, index, ore.step);

            int repeat = ore.count.get(random);

            if (biomeName.equals("basalt_deltas") && (ore.type == Ore.Type.GOLD_NETHER || ore.type == Ore.Type.QUARTZ)) {
                repeat *= 2;
            }

            for (int i = 0; i < repeat; i++) {

                int x = random.nextInt(16) + chunkX;
                int z;
                int y;
                if (versionString.equals("1.14")) {
                    y = ore.depthAverage ? random.nextInt(ore.maxY) + random.nextInt(ore.maxY) - ore.maxY : random.nextInt(ore.maxY - ore.minY);
                    z = random.nextInt(16) + chunkZ;
                } else {
                    z = random.nextInt(16) + chunkZ;
                    y = ore.depthAverage ? random.nextInt(ore.maxY) + random.nextInt(ore.maxY) - ore.maxY : random.nextInt(ore.maxY - ore.minY);
                }
                y += ore.minY;

                switch (ore.generator) {
                    case DEFAULT -> ores.addAll(generateNormal(world, random, new BlockPos(x, y, z), ore.size));
                    case EMERALD -> {
                        if (airCheck.getValue().equals("Off") || world.getBlockState(new BlockPos(x, y, z)).isOpaque())
                            ores.add(new Vec3d(x, y, z));
                    }
                    case NO_SURFACE -> ores.addAll(generateHidden(world, random, new BlockPos(x, y, z), ore.size));
                    default -> Atomic.log(Level.ERROR, ore.type + " has some unknown generator. Fix it!");
                }
            }
            if (!ores.isEmpty())
                h.put(ore.type, ores);
        }
        chunkRenderers.put(chunkKey, h);
    }

    // ====================================
    // Mojang code
    // ====================================

    private ArrayList<Vec3d> generateNormal(ClientWorld world, Random random, BlockPos blockPos, int veinSize) {
        float f = random.nextFloat() * 3.1415927F;
        float g = (float) veinSize / 8.0F;
        int i = MathHelper.ceil(((float) veinSize / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d = (double) blockPos.getX() + Math.sin(f) * (double) g;
        double e = (double) blockPos.getX() - Math.sin(f) * (double) g;
        double h = (double) blockPos.getZ() + Math.cos(f) * (double) g;
        double j = (double) blockPos.getZ() - Math.cos(f) * (double) g;
        double l = (blockPos.getY() + random.nextInt(3) - 2);
        double m = (blockPos.getY() + random.nextInt(3) - 2);
        int n = blockPos.getX() - MathHelper.ceil(g) - i;
        int o = blockPos.getY() - 2 - i;
        int p = blockPos.getZ() - MathHelper.ceil(g) - i;
        int q = 2 * (MathHelper.ceil(g) + i);
        int r = 2 * (2 + i);

        for (int s = n; s <= n + q; ++s) {
            for (int t = p; t <= p + q; ++t) {
                if (o <= world.getTopY(Heightmap.Type.MOTION_BLOCKING, s, t)) {
                    return this.generateVeinPart(world, random, veinSize, d, e, h, j, l, m, n, o, p, q, r);
                }
            }
        }

        return new ArrayList<>();
    }

    private ArrayList<Vec3d> generateVeinPart(ClientWorld world, Random random, int veinSize, double startX, double endX, double startZ,
                                              double endZ, double startY, double endY, int x, int y, int z, int size, int i) {

        BitSet bitSet = new BitSet(size * i * size);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        double[] ds = new double[veinSize * 4];

        ArrayList<Vec3d> poses = new ArrayList<>();

        int n;
        double p;
        double q;
        double r;
        double s;
        for (n = 0; n < veinSize; ++n) {
            float f = (float) n / (float) veinSize;
            p = MathHelper.lerp(f, startX, endX);
            q = MathHelper.lerp(f, startY, endY);
            r = MathHelper.lerp(f, startZ, endZ);
            s = random.nextDouble() * (double) veinSize / 16.0D;
            double m = ((double) (MathHelper.sin(3.1415927F * f) + 1.0F) * s + 1.0D) / 2.0D;
            ds[n * 4] = p;
            ds[n * 4 + 1] = q;
            ds[n * 4 + 2] = r;
            ds[n * 4 + 3] = m;
        }

        for (n = 0; n < veinSize - 1; ++n) {
            if (!(ds[n * 4 + 3] <= 0.0D)) {
                for (int o = n + 1; o < veinSize; ++o) {
                    if (!(ds[o * 4 + 3] <= 0.0D)) {
                        p = ds[n * 4] - ds[o * 4];
                        q = ds[n * 4 + 1] - ds[o * 4 + 1];
                        r = ds[n * 4 + 2] - ds[o * 4 + 2];
                        s = ds[n * 4 + 3] - ds[o * 4 + 3];
                        if (s * s > p * p + q * q + r * r) {
                            if (s > 0.0D) {
                                ds[o * 4 + 3] = -1.0D;
                            } else {
                                ds[n * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for (n = 0; n < veinSize; ++n) {
            double u = ds[n * 4 + 3];
            if (!(u < 0.0D)) {
                double v = ds[n * 4];
                double w = ds[n * 4 + 1];
                double aa = ds[n * 4 + 2];
                int ab = Math.max(MathHelper.floor(v - u), x);
                int ac = Math.max(MathHelper.floor(w - u), y);
                int ad = Math.max(MathHelper.floor(aa - u), z);
                int ae = Math.max(MathHelper.floor(v + u), ab);
                int af = Math.max(MathHelper.floor(w + u), ac);
                int ag = Math.max(MathHelper.floor(aa + u), ad);

                for (int ah = ab; ah <= ae; ++ah) {
                    double ai = ((double) ah + 0.5D - v) / u;
                    if (ai * ai < 1.0D) {
                        for (int aj = ac; aj <= af; ++aj) {
                            double ak = ((double) aj + 0.5D - w) / u;
                            if (ai * ai + ak * ak < 1.0D) {
                                for (int al = ad; al <= ag; ++al) {
                                    double am = ((double) al + 0.5D - aa) / u;
                                    if (ai * ai + ak * ak + am * am < 1.0D) {
                                        int an = ah - x + (aj - y) * size + (al - z) * size * i;
                                        if (!bitSet.get(an)) {
                                            bitSet.set(an);
                                            mutable.set(ah, aj, al);
                                            if (aj > 0 && (airCheck.getValue().equals("Off") || world.getBlockState(mutable).isOpaque())) {
                                                poses.add(new Vec3d(ah, aj, al));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return poses;
    }

    private ArrayList<Vec3d> generateHidden(ClientWorld world, Random random, BlockPos blockPos, int size) {

        ArrayList<Vec3d> poses = new ArrayList<>();

        int i = random.nextInt(size + 1);

        for (int j = 0; j < i; ++j) {
            size = Math.min(j, 7);
            int x = this.randomCoord(random, size) + blockPos.getX();
            int y = this.randomCoord(random, size) + blockPos.getY();
            int z = this.randomCoord(random, size) + blockPos.getZ();
            if (airCheck.getValue().equals("Off") || world.getBlockState(new BlockPos(x, y, z)).isOpaque())
                poses.add(new Vec3d(x, y, z));
        }

        return poses;
    }

    private int randomCoord(Random random, int size) {
        return Math.round((random.nextFloat() - random.nextFloat()) * (float) size);
    }

    public interface DimensionTypeCaller {
        Identifier getInfiniburn();
    }
}
