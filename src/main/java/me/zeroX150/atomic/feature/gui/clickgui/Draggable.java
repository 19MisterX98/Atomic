package me.zeroX150.atomic.feature.gui.clickgui;

import me.zeroX150.atomic.Atomic;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

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
double n1 = 7.5625;
double d1 = 2.75;

        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            return n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            return n1 * (x -= 2.625 / d1) * x + 0.984375;
        }

    }

    public void mouseReleased() {
        this.dragged = false;
    }

    public void render(MatrixStack stack) {
        if (this.expanded) animProg += 0.006;
        else animProg -= 0.006;
        animProg = MathHelper.clamp(animProg,0,1);
        double animProgInter = easeOutBounce(animProg);
        if (lastRenderX == -1) lastRenderX = posX;
        if (lastRenderY == -1) lastRenderY = posY;
        float xDiff = (float) (lastRenderX - posX);
        float yDiff = (float) (lastRenderY - posY);
        lastRenderX -= (xDiff / ClickGUI.SMOOTH_DIV);
        lastRenderY -= (yDiff / ClickGUI.SMOOTH_DIV);

        if (this.animProg != 0) {
            double yOffset = 9 + margin * 2;
            for (Clickable child : children) {
                child.render(lastRenderX, lastRenderY + (yOffset*animProgInter), stack);
                yOffset += 9 + margin * 2;
            }
        }
        DrawableHelper.fill(stack, (int) (lastRenderX - margin - paddingX), (int) (lastRenderY - margin), (int) (lastRenderX + width + margin + paddingX), (int) (lastRenderY + 9 + margin), this.expanded ? ClickGUI.HEADER_EXP.getRGB() : ClickGUI.HEADER_RET.getRGB());
        DrawableHelper.drawCenteredText(stack, Atomic.client.textRenderer, title, (int) (lastRenderX + (width / 2)), (int) lastRenderY, 0xFFFFFF);
    }

    public void mouseMove(double deltaX, double deltaY) {
        if (this.dragged) {
            this.posX += deltaX;
            this.posY += deltaY;
        }
    }
}
