package net.alexburton.gangmod.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
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

public class FishyEntity extends Cat {
    public FishyEntity(EntityType<? extends Cat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        //Fishy follows you when you have a cookie
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.2D, Ingredient.of(Items.COOKIE), false));
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
                .add(Attributes.MOVEMENT_SPEED, 0.75);
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
}
