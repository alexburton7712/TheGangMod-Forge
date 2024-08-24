package net.alexburton.gangmod.entity.client;// Made with Blockbench 4.10.4

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.alexburton.gangmod.entity.animations.ModAnimationsDefinitions;
import net.alexburton.gangmod.entity.custom.FishyEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class FishyModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart fishy;
	private final ModelPart face;
	private final ModelPart top;
	private final ModelPart mainbody;
	private final ModelPart fins;
	private final ModelPart topfin;
	private final ModelPart leftfin;
	private final ModelPart rightfin;
	private final ModelPart nub;
	private final ModelPart backfin;

	public FishyModel(ModelPart root) {
		this.fishy = root.getChild("fishy");
		this.face = fishy.getChild("face");
		this.top = fishy.getChild("top");
		this.mainbody = fishy.getChild("mainbody");
		this.fins = fishy.getChild("fins");
		this.topfin = fins.getChild("topfin");
		this.leftfin = fins.getChild("leftfin");
		this.rightfin = fins.getChild("rightfin");
		this.nub = fins.getChild("nub");
		this.backfin = fins.getChild("backfin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fishy = partdefinition.addOrReplaceChild("fishy", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 18.0F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition face = fishy.addOrReplaceChild("face", CubeListBuilder.create().texOffs(0, 10).addBox(3.0F, -5.0F, -3.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

		PartDefinition top = fishy.addOrReplaceChild("top", CubeListBuilder.create().texOffs(12, 14).addBox(-2.0F, -7.0F, -3.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

		PartDefinition mainbody = fishy.addOrReplaceChild("mainbody", CubeListBuilder.create().texOffs(15, 5).addBox(-5.0F, -5.0F, -3.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(6, 0).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(2, 20).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

		PartDefinition fins = fishy.addOrReplaceChild("fins", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -1.5F));

		PartDefinition topfin = fins.addOrReplaceChild("topfin", CubeListBuilder.create().texOffs(3, 23).addBox(-2.0F, -2.0F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfin = fins.addOrReplaceChild("leftfin", CubeListBuilder.create().texOffs(25, 24).addBox(1.0F, -3.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.0F, 2.0F));

		PartDefinition rightfin = fins.addOrReplaceChild("rightfin", CubeListBuilder.create().texOffs(25, 24).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 4.0F, 5.0F));

		PartDefinition nub = fins.addOrReplaceChild("nub", CubeListBuilder.create().texOffs(25, 22).addBox(-6.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition backfin = fins.addOrReplaceChild("backfin", CubeListBuilder.create().texOffs(3, 23).addBox(-9.0F, 2.0F, 1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(((FishyEntity) entity).idleAnimationState, ModAnimationsDefinitions.FISHY_WALK, ageInTicks, 1f);
		this.animate(((FishyEntity) entity).walkAnimationState, ModAnimationsDefinitions.FISHY_WALK, ageInTicks, 1f);

		// TODO: how to get the wave animation in
		this.animate(((FishyEntity) entity).waveAnimationState, ModAnimationsDefinitions.FISHY_WAVE, ageInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fishy.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return fishy;
	}
}