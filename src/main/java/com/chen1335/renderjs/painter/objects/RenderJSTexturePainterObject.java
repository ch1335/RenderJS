package com.chen1335.renderjs.painter.objects;

import com.chen1335.renderjs.painter.RenderJSPainterObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;


public class RenderJSTexturePainterObject extends RenderJSPainterObject {
    private Unit color;
    private Unit uOffset;
    private Unit vOffset;
    private Unit uWidth;
    private Unit vHeight;
    private Unit rotation;
    private Unit rotationCenterX = FixedNumberUnit.ZERO;
    private Unit rotationCenterY = FixedNumberUnit.ZERO;
    private Unit scaleX = FixedNumberUnit.ONE;
    private Unit scaleY = FixedNumberUnit.ONE;

    public ResourceLocation texture = MissingTextureAtlasSprite.getLocation();
    private TextureAtlas textureAtlas;

    public RenderJSTexturePainterObject(Painter painter) {

    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        this.color = properties.getColor("color", this.color);
        this.texture = properties.getResourceLocation("texture", texture);
        this.uOffset = properties.getUnit("uOffset", this.uOffset);
        this.vOffset = properties.getUnit("vOffset", this.vOffset);
        this.uWidth = properties.getUnit("uWidth", this.uWidth);
        this.vHeight = properties.getUnit("vHeight", this.vHeight);
        this.rotation = properties.getUnit("rotation", this.rotation);
        if (properties.tag.contains("rotationCenter")) {
            PainterObjectProperties rotationCenterProperties = new PainterObjectProperties(properties.tag.getCompound("rotationCenter"));
            rotationCenterX = rotationCenterProperties.getUnit("x", rotationCenterX);
            rotationCenterY = rotationCenterProperties.getUnit("y", rotationCenterY);
        }
        if (properties.tag.contains("scale")) {
            PainterObjectProperties scaleProperties = new PainterObjectProperties(properties.tag.getCompound("scale"));
            scaleX = scaleProperties.getUnit("x", scaleX);
            scaleY = scaleProperties.getUnit("y", scaleY);
        }
    }

    @Override
    public void draw(PoseStack poseStack) {
        if (texture == null) {
            return;
        }

        if (eventJS == null) {
            return;
        }
        float aw = this.w.getFloat(eventJS);
        float ah = this.h.getFloat(eventJS);
        float ax = eventJS.alignX(this.x.getFloat(eventJS), aw, this.alignX);
        float ay = eventJS.alignY(this.y.getFloat(eventJS), ah, this.alignY);
        float az = this.z.getFloat(eventJS);
        if (this.textureAtlas == null) {
            this.textureAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
        }

        TextureAtlasSprite sprite = this.textureAtlas.getSprite(this.texture);
        int width = sprite.getWidth();
        int height = sprite.getHeight();
        poseStack.pushPose();
        poseStack.translate(ax, ay, az);
        poseStack.scale(scaleX.getFloat(eventJS), scaleY.getFloat(eventJS), 0);
        if (rotation != null) {
            poseStack.translate(rotationCenterX.getFloat(eventJS), rotationCenterY.getFloat(eventJS), 0);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotation.getFloat(eventJS)));
            poseStack.translate(-rotationCenterX.getFloat(eventJS), -rotationCenterY.getFloat(eventJS), 0);
        }
        if (color != null) {
            int col = color.getInt(eventJS);
            RenderSystem.setShaderColor(col >> 16 & 255, col >> 8 & 255, col & 255, col >> 24 & 255);
        }

        float uOffset = this.uOffset == null ? 0 : this.uOffset.getFloat(eventJS);
        float vOffset = this.vOffset == null ? 0 : this.vOffset.getFloat(eventJS);
        int uWidth = this.uWidth == null ? w.getInt(eventJS) : this.uWidth.getInt(eventJS);
        int vHeight = this.vHeight == null ? h.getInt(eventJS) : this.vHeight.getInt(eventJS);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        GuiComponent.blit(poseStack, 0, 0, 90, uOffset, vOffset, uWidth, vHeight, height, width);
        poseStack.popPose();
    }

}
