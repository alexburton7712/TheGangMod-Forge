package net.alexburton.gangmod.entity.ai;

import net.alexburton.gangmod.entity.custom.FishyEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class FishyWaveGoal extends Goal {
    private final FishyEntity entity;
    private int waveDelay = 40;
    private int ticksUntilNextWave = 40;
    private boolean shouldCountTillNextWave = false;

    public FishyWaveGoal(FishyEntity entity) {
        this.entity = entity;
    }

    protected void checkAndPerformWave(double pDistToPlayer){
        if (this.ticksUntilNextWave <= waveDelay){
            shouldCountTillNextWave = true;
            entity.setWaving(true);
        }
        else{
            this.ticksUntilNextWave = this.adjustedTickDelay(waveDelay*2);
            shouldCountTillNextWave = false;
            entity.setWaving(false);
            entity.waveAnimationTimeout = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldCountTillNextWave){
            this.ticksUntilNextWave = Math.max(this.ticksUntilNextWave - 1, 0);
        }
    }

    @Override
    public void start() {
        super.start();
        waveDelay = 40;
        ticksUntilNextWave = 40;
    }

    @Override
    public void stop() {
        entity.setWaving(false);
        super.stop();
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
