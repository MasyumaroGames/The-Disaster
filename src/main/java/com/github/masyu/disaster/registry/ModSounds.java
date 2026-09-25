package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Disaster.MODID);

    public static final RegistryObject<SoundEvent> NATURE_GUARDIAN_BOSS =
            SOUND_EVENTS.register(
                    "nature_guardian_boss",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(Disaster.MODID, "nature_guardian_boss")
                    ));
    public static final RegistryObject<SoundEvent> NATURE_GUARDIAN_DEATH =
            SOUND_EVENTS.register("nature_guardian_death",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation("disaster", "nature_guardian_death")
                    ));
    public static final RegistryObject<SoundEvent> NATURE_GUARDIAN_HURT =
            SOUND_EVENTS.register("nature_guardian_hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation("disaster", "nature_guardian_hurt")
                    ));
}