package com.github.masyu.disaster.entity.geo;

import com.github.masyu.disaster.entity.boss.NatureGuardian;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeoNatureGuardian extends GeoModel<NatureGuardian> {

    @Override
    public ResourceLocation getModelResource(NatureGuardian animatable) {
        return new ResourceLocation("disaster", "geo/nature_guardian.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NatureGuardian animatable) {
        return new ResourceLocation("disaster", "textures/entity/nature_guardian.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NatureGuardian animatable) {
        return new ResourceLocation("disaster", "animations/nature_guardian.animation.json");
    }
}