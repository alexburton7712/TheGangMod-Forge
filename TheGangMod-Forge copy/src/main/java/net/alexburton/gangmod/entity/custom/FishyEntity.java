package net.alexburton.gangmod.entity.custom;

import net.alexburton.gangmod.entity.ai.FishyLieOnBedGoal;
import net.alexburton.gangmod.entity.ai.FishyWaveGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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

import java.nio.file.FileSystem;
import java.util.Random;

public class FishyEntity extends Animal {
    private final Random random = new Random(); // Random generator

    public static final double TEMPT_SPEED_MOD = 0.5; // Slower speed for tempting
    public static final double WALK_SPEED_MOD = 0.2;  // Slower speed for walking

    private static final int MIN_WAVE_COOLDOWN_TICKS = 30; // 20 seconds in ticks
    private static final int MAX_WAVE_COOLDOWN_TICKS = 50; // 30 seconds in ticks

    private int idleAnimationTimeout = 0;
    public int waveAnimationTimeout = 0;
    private boolean isWaving = false; // Ensure wave animation triggers only once
    private boolean isLayingOnBack = false;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState waveAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    //private static final EntityDataAccessor<Boolean> WAVING = SynchedEntityData.defineId(FishyEntity.class, EntityDataSerializers.BOOLEAN);

    public FishyEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // <editor-fold desc="Tick related animations">
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
            this.idleAnimationTimeout = this.random.nextInt(20)+80;
            this.idleAnimationState.start(this.tickCount);
        }
        else{
            --this.idleAnimationTimeout;
            if (this.getDeltaMovement().lengthSqr() > 0) {
                this.walkAnimationState.start(this.tickCount);
            }
        }

        //new wave using FishyWaveGoal - does not work
//        if (this.isWaving() && waveAnimationTimeout <= 0){
//            waveAnimationTimeout = MIN_WAVE_COOLDOWN_TICKS + random.nextInt(MAX_WAVE_COOLDOWN_TICKS - MIN_WAVE_COOLDOWN_TICKS + 1); // Random cooldown between 20-30 seconds
//            this.waveAnimationState.start(this.tickCount);
//        }
//        else{
//            --this.waveAnimationTimeout;
//        }
//        if(!this.isWaving()){
//            waveAnimationState.stop();
//        }

        // Wave
        if (waveAnimationTimeout <= 0 && this.canBeSeenByAnyone() && this.idleAnimationTimeout > 0 && this.getDeltaMovement().lengthSqr() <= 0){
            //this.setWaving(true); //Lock
            this.setPose(Pose.STANDING);
            waveAnimationTimeout = MIN_WAVE_COOLDOWN_TICKS + random.nextInt(MAX_WAVE_COOLDOWN_TICKS - MIN_WAVE_COOLDOWN_TICKS + 1); // Random cooldown between 20-30 seconds
            this.waveAnimationState.start(this.tickCount);
            //this.setWaving(false); //Unlock
        }
        else{
            --this.waveAnimationTimeout;
        }
    }
    //</editor-fold>>

    @Override
    public void setPose(Pose pose) {
        // Prevent the entity from switching to other poses during the wave animation
        if (this.waveAnimationTimeout <= 0) {
            super.setPose(Pose.STANDING);
        } else {
            super.setPose(pose);
        }
    }

    // <editor-fold desc="Walking">
    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isWaving()) {
            this.setPose(Pose.STANDING);
        }

        // Trigger your custom walk animation here if moving
        if (this.getDeltaMovement().lengthSqr() > 0) {
            this.walkAnimationState.start(this.tickCount);
        }
    }

    // Override Walk
    // TODO: still not working
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

    // </editor-fold>

    // TODO: whole body is not moving
    // <editor-fold> desc="Waving">
    public boolean isWaving(){
        return this.isWaving;
    }
    public void setWaving(boolean isWaving) {
        this.isWaving = isWaving;
    }

//    public boolean isWaving(){
//        return this.entityData.get(WAVING);
//    }
//    public void setWaving(boolean isWaving) {
//        this.entityData.set(WAVING, isWaving);
//    }
//
//    @Override
//    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
//        super.defineSynchedData(pBuilder);
//        pBuilder.define(WAVING, false);
//        //this.entityData.define(WAVING, false); //this was in the video but doesnt work
//    }
    // </editor-fold>

    // TODO: not working yet
    // <editor-fold> desc="Laying on Back">
    public void setLayingOnBack(boolean layingOnBack) {
        this.isLayingOnBack = layingOnBack;
    }
    public boolean isLayingOnBack() {
        return this.isLayingOnBack;
    }
    // </editor-fold>


    // <editor-fold> desc="Movement Speed">
    @Override
    public float getSpeed() {
        return (float) this.WALK_SPEED_MOD;
    }
    @Override
    public void setSpeed(float pSpeed) {
        super.setSpeed((float) this.WALK_SPEED_MOD);
    }
    // </editor-fold>

    // <editor-fold> desc="Goals">
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        //Fishy follows you when you have a cookie
        this.goalSelector.addGoal(1, new TemptGoal(this, TEMPT_SPEED_MOD, Ingredient.of(Items.COOKIE), false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new FishyLieOnBedGoal(this, 1.1, 8));

//        this.goalSelector.addGoal(6, new FishyWaveGoal(this));
//        this.targetSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 3f));

        // FUTURE GOALS TO LOOK INTO
//        this.goalSelector.addGoal(6, new CatLieOnBedGoal(this, 1.5D, 20));
//        CatEntity.SleepWithOwnerGoal
//                EscapeDangerGoal
//                LookAroundGoal
//        LookAtEntityGoal
//        PandaEntity.LieOnBackGoal
//        PandaEntity.PlayGoal
//        PandaEntity.SneezeGoal
    }
    // </editor-fold>

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.FOLLOW_RANGE, 36)
                .add(Attributes.MOVEMENT_SPEED, WALK_SPEED_MOD); // Reduced movement speed
    }

    // <editor-fold desc="Other custom overrides">
    public void setTamed(boolean tamed) {
        // Do nothing or provide custom behavior
        // By default, do not change the tamed state
    }
    @Override
    public boolean canHoldItem(ItemStack pStack) {
       //TODO: make him hold cookie!
        return super.canHoldItem(pStack);
    }
    // </editor-fold>

    //TODO: custom sounds?
    // <editor-fold desc="Custom Sounds">
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
    // </editor-fold>

    // <editor-fold desc="Override rules">
    @Override
    public boolean isFood(ItemStack stack) { // Return false for all items to disable taming
        return false;
    }
    @Override
    public boolean canMate(Animal pOtherAnimal) { // No mating
        return false;
    }
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
    // </editor-fold>
}
