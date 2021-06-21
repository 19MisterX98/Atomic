package me.zeroX150.atomic.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class HologramManager {
    public static Hologram generate(String text, Vec3d pos) {
        return new Hologram().withPosition(pos).withText(text).withEgg(false).withChild(false);
    }

    public static class Hologram {
        String text;
        Vec3d pos;
        boolean isEgg = false;
        boolean isChild = false;
        boolean isVisible = false;
        boolean hasGravity = false;
        boolean wrapName = true;

        public Hologram() {
            this.text = "";
            this.pos = Vec3d.ZERO;
        }

        public Hologram withText(String text) {
            this.text = text;
            return this;
        }

        public Hologram withGravity(boolean hasGravity) {
            this.hasGravity = hasGravity;
            return this;
        }

        public Hologram withVisible(boolean isVisible) {
            this.isVisible = isVisible;
            return this;
        }

        public Hologram withPosition(Vec3d pos) {
            this.pos = pos;
            return this;
        }

        public Hologram withWrapName(boolean wrapName) {
            this.wrapName = wrapName;
            return this;
        }

        public Hologram withEgg(boolean isEgg) {
            this.isEgg = isEgg;
            return this;
        }

        public Hologram withChild(boolean isChild) {
            this.isChild = isChild;
            return this;
        }

        public ItemStack generate() {
            ItemStack stack = new ItemStack(isEgg ? Items.BEE_SPAWN_EGG : Items.ARMOR_STAND);
            NbtCompound tag = new NbtCompound();
            NbtList pos = new NbtList();
            pos.add(NbtDouble.of(this.pos.x));
            pos.add(NbtDouble.of(this.pos.y));
            pos.add(NbtDouble.of(this.pos.z));
            tag.put("CustomNameVisible", NbtByte.ONE);
            tag.put("CustomName", wrapName ? NbtString.of("{\"text\":\"" + this.text.replaceAll("&", "§") + "\"}") : NbtString.of(this.text.replaceAll("&", "§")));
            tag.put("Invisible", NbtByte.of(!isVisible));
            tag.put("Invulnerable", NbtByte.ONE);
            tag.put("NoGravity", NbtByte.of(!hasGravity));
            tag.put("Small", NbtByte.of(isChild));
            tag.put("Pos", pos);
            if (isEgg) tag.put("id", NbtString.of("minecraft:armor_stand"));
            stack.putSubTag("EntityTag", tag);
            stack.setCustomName(Text.of("§r§cHologram"));
            return stack;
        }
    }
}
