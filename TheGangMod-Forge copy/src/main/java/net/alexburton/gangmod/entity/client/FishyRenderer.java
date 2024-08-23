package net.alexburton.gangmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.alexburton.gangmod.GangMod;
import net.alexburton.gangmod.entity.custom.FishyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FishyRenderer extends MobRenderer<FishyEntity, fishy_model<FishyEntity>> {

    public FishyRenderer(EntityRendererProvider.Context pContext){
        super(pContext, new fishy_model<>(pContext.bakeLayer(ModModelLayers.FISHY_LAYER)), 2f);

    }

    @Override
    public ResourceLocation getTextureLocation(FishyEntity fishyEntity) {
        return new ResourceLocation(GangMod.MOD_ID, "textures/entity/fishy_texture.png");
    }

    @Override
    public void render(FishyEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
