package net.alexburton.gangmod.entity.client;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class fishy_model<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart fishy;
	private final ModelPart face;
	private final ModelPart top;
	private final ModelPart mainbody;
	private final ModelPart fins;

	public fishy_model(ModelPart root) {
		this.fishy = root.getChild("fishy");
		this.face = fishy.getChild("face");
		this.top = fishy.getChild("top");
		this.mainbody = fishy.getChild("mainbody");
		this.fins = fishy.getChild("fins");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fishy = partdefinition.addOrReplaceChild("fishy", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 18.0F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition face = fishy.addOrReplaceChild("face", CubeListBuilder.create().texOffs(0, 10).addBox(3.0F, -5.0F, -3.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

		PartDefinition top = fishy.addOrReplaceChild("top", CubeListBuilder.create().texOffs(12, 14).addBox(-2.0F, -7.0F, -3.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

		PartDefinition mainbody = fishy.addOrReplaceChild("mainbody", CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, -5.0F, -3.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(2, 20).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.5F));

		PartDefinition fins = fishy.addOrReplaceChild("fins", CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -2.0F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-6.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-1.0F, 3.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(25, 22).addBox(-9.0F, 2.0F, 1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(-1.0F, 3.0F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -1.5F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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