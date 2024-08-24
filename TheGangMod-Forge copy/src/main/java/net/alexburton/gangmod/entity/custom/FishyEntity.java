package net.alexburton.gangmod.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FishyEntity extends Cat {
    private final Random random = new Random(); // Random generator

    public static final double TEMPT_SPEED_MOD = 0.5; // Slower speed for tempting
    public static final double WALK_SPEED_MOD = 0.2;  // Slower speed for walking

    private static final int MIN_WAVE_COOLDOWN_TICKS = 30; // 20 seconds in ticks
    private static final int MAX_WAVE_COOLDOWN_TICKS = 50; // 30 seconds in ticks

    private int idleAnimationTimeout = 0;
    private int waveAnimationTimeout = 0;
    private boolean hasWaved = false; // Ensure wave animation triggers only once

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState waveAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    public FishyEntity(EntityType<? extends Cat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Tick related animations
    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide){
            setupAnimationStates();
        }
    }
    private void setupAnimationStates(){
        // Idle
        if (this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = this.random.nextInt(20)+200;
            this.idleAnimationState.start(this.tickCount);
        }
        else{
            --this.idleAnimationTimeout;
        }

        // Wave
        if (waveAnimationTimeout <= 0 && this.canBeSeenByAnyone()){
            this.setPose(Pose.STANDING);
            waveAnimationTimeout = 40; //Length in ticks (1.5 seconds * 20 = 30 ticks)
            // can make wave at random
            //waveAnimationTimeout = MIN_WAVE_COOLDOWN_TICKS + random.nextInt(MAX_WAVE_COOLDOWN_TICKS - MIN_WAVE_COOLDOWN_TICKS + 1); // Random cooldown between 20-30 seconds
            this.waveAnimationState.start(this.tickCount);
        }
        else{
            --this.waveAnimationTimeout;
        }
    }

    // Override Walk
    // TODO: still not working
//    @Override
//    protected void updateWalkAnimation(float pPartialTick) {
//        float f;
//        if(this.getPose() == Pose.STANDING){
//            f = Math.min(pPartialTick * 6f, 1f);
//        }
//        else {
//            f = 0f;
//        }
//        this.walkAnimation.update(f, 0.2f);
//    }
    @Override
    public void aiStep() {
        super.aiStep();

        // Trigger your custom walk animation here if moving
        if (this.getDeltaMovement().lengthSqr() > 0) {
            this.walkAnimationState.start(this.tickCount);
        }
    }

    // Movement Speed
    @Override
    public float getSpeed() {
        return (float) this.WALK_SPEED_MOD;
    }
    @Override
    public void setSpeed(float pSpeed) {
        super.setSpeed((float) this.WALK_SPEED_MOD);
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
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.FOLLOW_RANGE, 36)
                .add(Attributes.MOVEMENT_SPEED, WALK_SPEED_MOD); // Reduced movement speed
    }

    public void setTamed(boolean tamed) {
        // Do nothing or provide custom behavior
        // By default, do not change the tamed state
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        return false;
    }

    @Override
    public boolean canHoldItem(ItemStack pStack) {
       //TODO: make him hold cookie!
        return super.canHoldItem(pStack);
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
