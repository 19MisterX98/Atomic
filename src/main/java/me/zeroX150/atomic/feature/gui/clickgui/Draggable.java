package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class Draggable {
    double margin = 4;
    double width = 100;
    double posX;
    double posY;
    double lastRenderX = -1;
    double lastRenderY = -1;
    boolean expanded;
    boolean dragged = false;
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
                return true;
            } else this.expanded = !this.expanded;
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

    public void mouseReleased() {
        this.dragged = false;
    }

    public void render(MatrixStack stack) {
        if (lastRenderX == -1) lastRenderX = posX;
        if (lastRenderY == -1) lastRenderY = posY;
        float xDiff = (float) (lastRenderX - posX);
        float yDiff = (float) (lastRenderY - posY);
        lastRenderX -= (xDiff / ClickGUI.SMOOTH_DIV);
        lastRenderY -= (yDiff / ClickGUI.SMOOTH_DIV);
        DrawableHelper.fill(stack, (int) (lastRenderX - margin), (int) (lastRenderY - margin), (int) (lastRenderX + width + margin), (int) (lastRenderY + 9 + margin), this.expanded ? ClickGUI.HEADER_EXP.getRGB() : ClickGUI.HEADER_RET.getRGB());
        //Atomic.client.textRenderer.draw(new MatrixStack(),title,(float)posX,(float)posY,0xFFFFFF);
        DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, title, (int) (lastRenderX + (width / 2)), (int) lastRenderY, 0xFFFFFF);
        //Renderer.fill(Color.CYAN,posX-margin,posY-margin,posX+Atomic.client.textRenderer.getWidth(this.title)+margin,posY+10+margin);

        if (this.expanded) {
            double yOffset = 9 + margin * 2;
            for (Clickable child : children) {
                child.render(lastRenderX, lastRenderY + yOffset, stack);
                yOffset += 9 + margin * 2;
            }
        }
        /*lastRenderY = posY;
        lastRenderX = posX;*/
    }

    public void mouseMove(double deltaX, double deltaY) {
        if (this.dragged) {
            this.posX += deltaX;
            this.posY += deltaY;
        }
    }
}
