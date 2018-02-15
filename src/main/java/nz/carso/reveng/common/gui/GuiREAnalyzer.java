package nz.carso.reveng.common.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import nz.carso.reveng.RevEng;
import nz.carso.reveng.common.block.TileEntityREAnalyzer;
import nz.carso.reveng.common.network.PacketAnalyzer;
import nz.carso.reveng.common.network.REPacketHandler;

import java.io.IOException;

public class GuiREAnalyzer extends GuiContainer {
    private TileEntityREAnalyzer te;
    private GuiTextField face, txt;
    private static final ResourceLocation texture = new ResourceLocation(RevEng.MODID, "textures/gui/re_analyzer.png");

    GuiREAnalyzer(Container container, TileEntityREAnalyzer te) {
        super(container);
        this.te = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GuiButton btn;
        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;

        for (int i = 0; i < 6; i++) {
            btn = this.buttonList.get(i);
            if (te.targetCap[i] != null) {
                btn.enabled = true;
                btn.displayString = "" + te.targetCap[i].getSlots();
            } else {
                btn.enabled = false;
                btn.displayString = "-1";
            }
            if (btn.isMouseOver())
                drawHoveringText(EnumFacing.getFront(i).getName2(), mouseX - guiX, mouseY - guiY);
        }
        btn = this.buttonList.get(6);
        if (te.targetCap[6] != null) {
            btn.enabled = true;
            btn.displayString = "" + te.targetCap[6].getSlots();
        } else {
            btn.enabled = false;
            btn.displayString = "-1";
        }
        if (btn.isMouseOver())
            drawHoveringText("generic", mouseX - guiX, mouseY - guiY);
        this.txt.setText("" + te.selectedSlot);
        this.txt.drawTextBox();

        if (te.selectedFace < 6)
            this.face.setText(EnumFacing.getFront(te.selectedFace).getName2() + "");
        else
            this.face.setText("generic");
        this.face.drawTextBox();


    }

    @Override
    public void initGui() {
        super.initGui();

        final int faceGroupX = 72;
        final int faceGroupY = 7;
        final int plusBtnX = 72;
        final int plusBtnY = 55;
        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;
        for (int i = 0; i < 6; i++) {
            int groupX = faceGroupX + (i / 2) * 30;
            int groupY = faceGroupY + (i % 2) * 24;
            if (te.targetCap[i] != null) {
                GuiButton btn = new GuiButton(i, groupX + guiX, groupY + guiY,
                        20, 20, "" + te.targetCap[i].getSlots());
                btn.enabled = true;

                this.buttonList.add(btn);
            } else {
                GuiButton btn = new GuiButton(i, groupX + guiX, groupY + guiY,
                        20, 20, "-1");
                btn.enabled = false;
                this.buttonList.add(btn);
            }
        }
        if (te.targetCap[6] != null) {
            GuiButton btn = new GuiButton(6, guiX + 43, guiY + 55, 20, 20,
                    "" + te.targetCap[6].getSlots());
            btn.enabled = true;
            this.buttonList.add(btn);
        } else {
            GuiButton btn = new GuiButton(6, guiX + 43, guiY + 55, 20, 20,
                    "-1");
            btn.enabled = false;
            this.buttonList.add(btn);
        }
        GuiButton btnP = new GuiButton(7, guiX + plusBtnX, guiY + plusBtnY,  12, 12, "+");
        this.buttonList.add(btnP);
        GuiButton btnM = new GuiButton(8, guiX + plusBtnX + 18, guiY + plusBtnY, 12, 12, "-");
        this.buttonList.add(btnM);


        GuiButton insert = new GuiButton(9, guiX + 125, guiY + 56, 35, 20, "insert");
        this.buttonList.add(insert);

        this.txt = new GuiTextField(10, fontRenderer, plusBtnX, plusBtnY + 16, 30, 10);
        this.txt.setText("-1");

        this.face = new GuiTextField(11, fontRenderer, 10, 11, 48, 10);
        this.face.setText("none");
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id <= 6) {
            te.setSelectedFace(button.id);
        } else if (button.id == 7) {
            int now = te.selectedSlot;
            if (te.targetCap[te.selectedFace] != null && now + 1 < te.targetCap[te.selectedFace].getSlots())
                te.setSelectedSlot(now + 1);
        } else if (button.id == 8) {
            int now = te.selectedSlot;
            if (te.targetCap[te.selectedFace] != null && now - 1 >= 0)
                te.setSelectedSlot(now - 1);
        } else if (button.id == 9) {
            BlockPos p = te.getPos();
            REPacketHandler.INSTANCE.sendToServer(new PacketAnalyzer(te.selectedFace, te.selectedSlot, te.selectedSlots,
                    p.getX(), p.getY(), p.getZ()));
        }
    }
}
