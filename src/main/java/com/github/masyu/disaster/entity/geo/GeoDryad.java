package com.github.masyu.disaster.entity.geo;

import com.github.masyu.disaster.entity.normal_mobs.Dryad;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeoDryad extends GeoModel<Dryad> {
    @Override
    public ResourceLocation getModelResource(Dryad animatable) {
        return new ResourceLocation("disaster", "geo/dryad.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Dryad animatable) {
        return new ResourceLocation("disaster", "textures/entity/dryad.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Dryad animatable) {
        return new ResourceLocation("disaster", "animations/dryad.animation.json");
    }
}
