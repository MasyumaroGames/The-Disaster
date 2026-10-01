package com.github.masyu.disaster.entity.renderer;

import com.github.masyu.disaster.entity.normal_mobs.Dryad;
import com.github.masyu.disaster.entity.geo.GeoDryad;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DryadRenderer extends GeoEntityRenderer<Dryad> {
    public DryadRenderer(EntityRendererProvider.Context context) {
        super(context, new GeoDryad());
    }
}