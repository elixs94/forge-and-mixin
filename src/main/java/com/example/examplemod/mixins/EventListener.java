package com.example.examplemod.mixins;

import com.example.examplemod.BlockData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

import static net.minecraft.client.renderer.RenderGlobal.drawSelectionBoundingBox;

@Mixin(RenderGlobal.class)
public class EventListener {
    private RenderManager renderManager;
    private Minecraft mc = Minecraft.getMinecraft();



    public EventListener() {
        this.renderManager = this.mc.getRenderManager();
    }



    @Inject(method = "renderEntities", at = @At("HEAD"), cancellable = true)
    protected void onTick(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        ArrayList<BlockData> testCopy = new ArrayList(MixinLoader.blockDataArrayList);
        for(BlockData blockData : testCopy) {

            if(mc.thePlayer.getDistanceSq(blockData.getX(), blockData.getY(), blockData.getZ()) < 15) {
                if(blockData.getIntValue() == 1) {
                    //dann platziert = green
                    renderTag(blockData.getX(), blockData.getY(), blockData.getZ(), blockData.getName(), 65280);
                    drawSelectionBox(new BlockPos(blockData.getX(), blockData.getY(), blockData.getZ()), partialTicks, 0, 255, 0);
                } else {
                    //dann abgebaut = red

                    renderTag(blockData.getX(), blockData.getY(), blockData.getZ(), blockData.getName(), 16711680);
                    drawSelectionBox(new BlockPos(blockData.getX(), blockData.getY(), blockData.getZ()), partialTicks, 255, 0, 0);
                }
            }
        }
    }

    protected void renderTag(double x, double y, double z, String string, int color) {
        FontRenderer fontrenderer = this.mc.fontRendererObj;
        float f = 0.6F;
        float f1 = 0.016666668F * f;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPushMatrix();

        double renderPosX = x + 0.5 - renderManager.viewerPosX;
        double renderPosY = y - 0.5 - renderManager.viewerPosY;
        double renderPosZ = z + 0.5 - renderManager.viewerPosZ;

        GL11.glTranslatef((float) renderPosX, (float) renderPosY, (float) renderPosZ);

        GL11.glNormal3f(1, 1, 1);

        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);


        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int i = fontrenderer.getStringWidth(string) / 2;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) (-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos((double) (i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontrenderer.drawString(string, -fontrenderer.getStringWidth(string) / 2, 0, color);
        GlStateManager.depthMask(true);
        fontrenderer.drawString(string, -fontrenderer.getStringWidth(string) / 2, 0, color);


        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GL11.glColor4f(1, 1, 1, 1.0F);
        GlStateManager.popMatrix();

        GL11.glEnable(GL11.GL_DEPTH_TEST);

    }

    public void drawSelectionBox(BlockPos blockpos, float partialTicks, float red, float green, float blue) {


        blockpos = blockpos.add(0, -1, 0);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, 0.4F);
        GL11.glLineWidth(6.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        float f = 0.002F;
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock();


        block.setBlockBoundsBasedOnState(Minecraft.getMinecraft().theWorld, blockpos);
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        drawSelectionBoundingBox(block.getSelectedBoundingBox(Minecraft.getMinecraft().theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));


        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }

}
