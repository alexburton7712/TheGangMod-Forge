package net.alexburton.gangmod.entity.ai;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
import net.alexburton.gangmod.entity.custom.FishyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class FishyLieOnBedGoal extends MoveToBlockGoal { //CatLieOnBed also extends to MoveToBlockGoals, so I copied most of it here
    private final FishyEntity fishy;
    private Player owner;
    private BlockPos bedPos;

    public FishyLieOnBedGoal(FishyEntity fishy, double pSpeedModifier, int pSearchRange) {
        super(fishy, pSpeedModifier, pSearchRange, 6);
        this.fishy = fishy;
        this.verticalSearchStart = -2;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canUse() {
         return !this.fishy.isLayingOnBack() && super.canUse();
    }

    public void start() {
        if (this.bedPos != null) {
            super.start();
            this.fishy.getNavigation().moveTo(this.bedPos.getX(), this.bedPos.getY(), this.bedPos.getZ(), 1.1D);
            this.fishy.setLayingOnBack(true);  // Set to laying on back
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.fishy.setLayingOnBack(false); // Reset laying on back state
        this.owner = null;
        this.bedPos = null;
    }

    protected int nextStartTick(PathfinderMob pCreature) {
        return 40;
    }

    public void tick() {
        super.tick();
        if (!this.isReachedTarget()) {
            this.fishy.setLayingOnBack(false);
        } else if (!this.fishy.isLayingOnBack()) {
            this.fishy.setLayingOnBack(true);
        }
    }

    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        return pLevel.isEmptyBlock(pPos.above()) && pLevel.getBlockState(pPos).is(BlockTags.BEDS);
    }

    // TODO: look if we want this
    //@Override
    //    public boolean canUse() {
    //        if (this.fishy.isTame() && !this.fishy.isOrderedToSit() && this.fishy.getOwner() != null) {
    //            this.owner = (Player) this.fishy.getOwner();
    //            if (this.owner != null && this.owner.isSleeping()) {
    //                BlockPos blockpos = this.owner.blockPosition();
    //                BlockState blockstate = this.fishy.level().getBlockState(blockpos);
    //                if (blockstate.getBlock() instanceof BedBlock && blockstate.getValue(BedBlock.OCCUPIED)) {
    //                    this.bedPos = blockpos;
    //                    return true;
    //                }
    //            }
    //        }
    //        return false;
    //    }

    //@Override
    //    public boolean canContinueToUse() {
    //        return this.owner != null && this.owner.isSleeping() && this.bedPos != null && this.fishy.distanceToSqr(this.bedPos) > 1.0D;
    //    }
    //
    //    @Override
    //    public void start() {
    //        if (this.bedPos != null) {
    //            this.fishy.getNavigation().moveTo(this.bedPos.getX(), this.bedPos.getY(), this.bedPos.getZ(), 1.1D);
    //        }
    //    }
    //
    //    @Override
    //    public void tick() {
    //        if (this.bedPos != null && this.fishy.distanceToSqr(this.bedPos) < 1.0D) {
    //            BlockState blockstate = this.fishy.level().getBlockState(this.bedPos);
    //            if (blockstate.getBlock() instanceof BedBlock) {
    //                Direction direction = blockstate.getValue(BedBlock.FACING);
    //                this.fishy.setBedPosition(this.bedPos, direction);
    //            }
    //        }
    //    }
    //
    //    @Override
    //    public void stop() {
    //        this.fishy.clearBedPosition();
    //        this.owner = null;
    //        this.bedPos = null;
    //    }
}
