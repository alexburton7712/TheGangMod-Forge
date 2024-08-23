package net.alexburton.gangmod.entity;

import net.alexburton.gangmod.GangMod;
import net.alexburton.gangmod.entity.custom.FishyEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GangMod.MOD_ID);

    // TODO: For each gang member, add RegistryObject
    public static final RegistryObject<EntityType<FishyEntity>> FISHY =
            ENTITY_TYPES.register("fishy", () -> EntityType.Builder.of(FishyEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.5f).build("fishy"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
