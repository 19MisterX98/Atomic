package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Draggable {
    List<PositionD> recordedPositions = new ArrayList<>();
    double animProg = 0;
    double width = 100;
    double posX;
    double posY;
    double lastRenderX = -1;
    double lastRenderY = -1;
    double trackedLastRenderX = -1;
    double trackedLastRenderY = -1;
    boolean expanded;
    boolean dragged = false;
    long lastRender = System.currentTimeMillis();
    String title;
    List<Clickable> children = new ArrayList<>();
    double lrXDiff = 0;

    public Draggable(String title, boolean isExpanded) {
        this.title = title;
        this.expanded = isExpanded;
    }

    double getMargin() {
        return ClickGUI.currentActiveTheme.h_margin();
    }

    double getPaddingX() {
        return ClickGUI.currentActiveTheme.h_paddingX();
    }

    public void addChild(Clickable child) {
        this.children.add(child);
    }

    public boolean mouseClicked(boolean isLeft, double x, double y) {
        if (lastRenderX + width + getMargin() > x && lastRenderX - getMargin() < x && lastRenderY + 9 + getMargin() > y && lastRenderY - getMargin() < y) {
            if (isLeft) {
                this.dragged = true;
            } else this.expanded = !this.expanded;
            return true;
        } else if (this.expanded) {
            double yOffset = 9 + getMargin() * 2;
            for (Clickable child : children) {
                double childPosY = lastRenderY + yOffset;
                double childPosX = lastRenderX;
                if (childPosX + width + getMargin() > x && childPosX - getMargin() < x && childPosY + 9 + getMargin() > y && childPosY - getMargin() < y) {
                    child.clicked(isLeft);
                    break;
                }
                yOffset += 9 + getMargin() * 2;
            }
        }
        return false;
    }

    public void tick() {
        float xDiff = (float) (lastRenderX - posX);
        float yDiff = (float) (lastRenderY - posY);
        double nxDiff = (xDiff / (me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue()));
        double nyDiff = (yDiff / (me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue()));
        if (Math.abs(nxDiff) < 0.02) nxDiff = xDiff;
        if (Math.abs(nyDiff) < 0.02) nyDiff = yDiff;
        lastRenderX -= nxDiff;
        lastRenderY -= nyDiff;
        if (trackedLastRenderX == -1) trackedLastRenderX = lastRenderX;
        if (trackedLastRenderY == -1) trackedLastRenderY = lastRenderY;
        lrXDiff = lastRenderX - trackedLastRenderX;
        trackedLastRenderX = lastRenderX;
        trackedLastRenderY = lastRenderY;
    }

    double easeOutBounce(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

    public void mouseReleased() {
        this.dragged = false;
    }

    public void render(MatrixStack stack, float delta, double aProgI) {

        if (this.expanded) animProg += (System.currentTimeMillis() - lastRender) / 500d;
        else animProg -= (System.currentTimeMillis() - lastRender) / 500d;
        if (System.currentTimeMillis() - lastRender > 1) lastRender = System.currentTimeMillis();
        animProg = MathHelper.clamp(animProg, 0, 1);
        double animProgInter = easeOutBounce(animProg);
        if (lastRenderX == -1) lastRenderX = posX;
        if (lastRenderY == -1) lastRenderY = posY;
        stack.translate(lastRenderX - getMargin() - getPaddingX(), lastRenderY - getMargin(), 0);
        double rotation = MathHelper.clamp(lrXDiff, -50, 50) * me.zeroX150.atomic.feature.module.impl.render.ClickGUI.dragFactor.getValue();
        rotation += Math.sin(animProgInter * Math.PI * 2) * 10;
        stack.multiply(new Quaternion(new Vec3f(0, 0, 1), (float) (rotation), true));
        PositionD v = new PositionD(lastRenderX - getMargin() - getPaddingX(), lastRenderY - getMargin(), rotation);
        if (!recordedPositions.contains(v)) recordedPositions.add(v);
        else recordedPositions.add(null);
        while (recordedPositions.size() > me.zeroX150.atomic.feature.module.impl.render.ClickGUI.tailSize.getValue())
            recordedPositions.remove(0);
        double val = 0;
        double incr = 1d / recordedPositions.stream().filter(Objects::nonNull).count();
        if (me.zeroX150.atomic.feature.module.impl.render.ClickGUI.enableTails.getValue())
            for (PositionD recordedPosition : recordedPositions) {
                if (recordedPosition == null) continue;
                MatrixStack ms = new MatrixStack();
                ms.translate(recordedPosition.x(), recordedPosition.y(), -100);
                ms.translate(aProgI * Atomic.client.getWindow().getScaledWidth(), 0, 0);
                ms.multiply(new Quaternion(new Vec3f(0, 0, 1), (float) (recordedPosition.rot()), true));
                Color c = Renderer.modify(new Color(Color.HSBtoRGB((float) val, 0.6f, 0.6f)), -1, -1, -1, 30);
                Renderer.fill(ms, c, -getPaddingX() * 2, 0, width + getMargin() + getPaddingX() * 2 + 4, 9 + getMargin() * 2);
                val += incr;
            }
        if (this.animProg != 0) {
            double yOffset = 9 + getMargin() * 2;
            for (Clickable child : children) {
                double px = this.dragged || animProgInter != 1 ? -1 : lastRenderX;
                double py = this.dragged || animProgInter != 1 ? -1 : lastRenderY + (yOffset * animProgInter);
                child.render(getMargin(), getMargin() + (yOffset * animProgInter), stack, animProgInter, px, py, delta);
                yOffset += 9 + getMargin() * 2;
            }
        }
        DrawableHelper.fill(stack, (int) -(getPaddingX() * 2), 0, (int) (width + getMargin() + 4 + getPaddingX() * 2), (int) (9 + getMargin() * 2), Renderer.lerp(ClickGUI.currentActiveTheme.h_exp(), ClickGUI.currentActiveTheme.h_ret(), animProgInter).getRGB());
        if (!ClickGUI.currentActiveTheme.centerText())
            Atomic.fontRenderer.drawString(stack, title, getMargin(), getMargin() + 1, ClickGUI.currentActiveTheme.fontColor().getRGB());
        else
            Atomic.fontRenderer.drawCenteredString(stack, title, getMargin() + width / 2f, getMargin(), ClickGUI.currentActiveTheme.fontColor().getRGB());
        //DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, title, (int) (getMargin() + (width / 2)), (int) margin, 0xFFFFFF);
        stack.translate(-(lastRenderX - getMargin() - getPaddingX()), -(lastRenderY - getMargin()), 0);
    }

    public void mouseMove(double deltaX, double deltaY) {
        if (this.dragged) {
            this.posX += deltaX;
            this.posY += deltaY;
        }
    }

    record PositionD(double x, double y, double rot) {
    }
}
