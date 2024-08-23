package net.alexburton.gangmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FishyEntity extends Cat {
    public static final double TEMPT_SPEED_MOD = 0.2; // Slower speed for tempting
    public static final double WALK_SPEED_MOD = 0.05;  // Slower speed for walking
    public static final double SPRINT_SPEED_MOD = 0.1; // Slower speed for sprinting

    public FishyEntity(EntityType<? extends Cat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide){
            setupAnimationStates();
        }
    }
    private void setupAnimationStates(){
        if (this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = this.random.nextInt(20)+80;
            this.idleAnimationState.start(this.tickCount);
        }
        else{
            --this.idleAnimationTimeout;
        }
    }

    // Fishy walk
    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING){
            f = Math.min(pPartialTick * 6f, 1f);
        }
        else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        // Disable sprinting by overriding and doing nothing
        super.setSprinting(false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        //Fishy follows you when you have a cookie
        this.goalSelector.addGoal(1, new TemptGoal(this, TEMPT_SPEED_MOD, Ingredient.of(Items.COOKIE), false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        // FUTURE GOALS TO LOOK INTO
        this.goalSelector.addGoal(6, new CatLieOnBedGoal(this, 1.5D, 20));
//        CatEntity.SleepWithOwnerGoal
//                EscapeDangerGoal
//                LookAroundGoal
//        LookAtEntityGoal
//        PandaEntity.LieOnBackGoal
//        PandaEntity.PlayGoal
//        PandaEntity.SneezeGoal
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MOVEMENT_SPEED, WALK_SPEED_MOD); // Reduced movement speed
    }

    public void setTamed(boolean tamed) {
        // Do nothing or provide custom behavior
        // By default, do not change the tamed state
    }


    @Override
    public boolean isFood(ItemStack stack) {
        // Return false for all items to disable taming
        return false;
    }

    //TODO: custom sounds?
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return null;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }
}
