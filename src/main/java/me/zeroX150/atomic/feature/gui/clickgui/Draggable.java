package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class Draggable {
    double animProg = 0;
    double margin = 4;
    double paddingX = 4;
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

    public Draggable(String title, boolean isExpanded) {
        this.title = title;
        this.expanded = isExpanded;
    }

    public void addChild(Clickable child) {
        this.children.add(child);
    }

    public boolean mouseClicked(boolean isLeft, double x, double y) {
        if (lastRenderX + width + margin > x && lastRenderX - margin < x && lastRenderY + 9 + margin > y && lastRenderY - margin < y) {
            if (isLeft) {
                this.dragged = true;
            } else this.expanded = !this.expanded;
            return true;
        } else if (this.expanded) {
            double yOffset = 9 + margin * 2;
            for (Clickable child : children) {
                double childPosY = lastRenderY + yOffset;
                double childPosX = lastRenderX;
                if (childPosX + width + margin > x && childPosX - margin < x && childPosY + 9 + margin > y && childPosY - margin < y) {
                    child.clicked(isLeft);
                    break;
                }
                yOffset += 9 + margin * 2;
            }
        }
        return false;
    }

    double easeOutBounce(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

    public void mouseReleased() {
        this.dragged = false;
    }

    public void render(MatrixStack stack) {
        if (this.expanded) animProg += (System.currentTimeMillis() - lastRender) / 300d;
        else animProg -= (System.currentTimeMillis() - lastRender) / 300d;
        if (System.currentTimeMillis() - lastRender > 1) lastRender = System.currentTimeMillis();
        animProg = MathHelper.clamp(animProg, 0, 1);
        double animProgInter = easeOutBounce(animProg);
        if (lastRenderX == -1) lastRenderX = posX;
        if (lastRenderY == -1) lastRenderY = posY;
        if (trackedLastRenderX == -1) trackedLastRenderX = lastRenderX;
        if (trackedLastRenderY == -1) trackedLastRenderY = lastRenderY;
        double lrXDiff = lastRenderX - trackedLastRenderX;
        trackedLastRenderX = lastRenderX;
        trackedLastRenderY = lastRenderY;
        float xDiff = (float) (lastRenderX - posX);
        float yDiff = (float) (lastRenderY - posY);
        double nxDiff = (xDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue());
        double nyDiff = (yDiff / me.zeroX150.atomic.feature.module.impl.render.ClickGUI.smooth.getValue());
        if (Math.abs(nxDiff) < 0.02) nxDiff = xDiff;
        if (Math.abs(nyDiff) < 0.02) nyDiff = yDiff;
        lastRenderX -= nxDiff;
        lastRenderY -= nyDiff;
        stack.translate(lastRenderX - margin - paddingX, lastRenderY - margin, 0);
        stack.multiply(new Quaternion(new Vec3f(0, 0, 1), (float) MathHelper.clamp(lrXDiff, -20, 20), true));
        //stack.multiply(new Quaternion(new Vec3f(0,1,0),(float) MathHelper.clamp(lrYDiff,-20,20),true));
        if (this.animProg != 0) {
            double yOffset = 9 + margin * 2;
            for (Clickable child : children) {
                child.render(margin, margin + (yOffset * animProgInter), stack, animProgInter, lastRenderX, lastRenderY + (yOffset * animProgInter));
                yOffset += 9 + margin * 2;
            }
        }
        //DrawableHelper.fill(stack, (int) (lastRenderX - margin - paddingX), (int) (lastRenderY - margin), (int) (lastRenderX + width + margin + paddingX), (int) (lastRenderY + 9 + margin), this.expanded ? ClickGUI.HEADER_EXP.getRGB() : ClickGUI.HEADER_RET.getRGB());
        //DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, title, (int) (lastRenderX + (width / 2)), (int) lastRenderY, 0xFFFFFF);
        DrawableHelper.fill(stack, (int) -paddingX, 0, (int) (width + margin + paddingX * 2), (int) (9 + margin * 2), this.expanded ? ClickGUI.HEADER_EXP.getRGB() : ClickGUI.HEADER_RET.getRGB());
        DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, title, (int) (margin + (width / 2)), (int) margin, 0xFFFFFF);
        stack.translate(-(lastRenderX - margin - paddingX), -(lastRenderY - margin), 0);
    }

    public void mouseMove(double deltaX, double deltaY) {
        if (this.dragged) {
            this.posX += deltaX;
            this.posY += deltaY;
        }
    }
}
