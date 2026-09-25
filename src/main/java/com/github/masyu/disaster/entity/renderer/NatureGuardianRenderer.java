package com.github.masyu.disaster.entity.renderer;

import com.github.masyu.disaster.entity.NatureGuardian;
import com.github.masyu.disaster.entity.geo.GeoNatureGuardian;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NatureGuardianRenderer extends GeoEntityRenderer<NatureGuardian> {

    public NatureGuardianRenderer(EntityRendererProvider.Context context) {
        // 第一引数に context、第二引数に新規作成したモデルのインスタンスを渡す
        super(context, new GeoNatureGuardian());

        // もし影のサイズ（シャドウ半径）を設定したい場合は、以下のようにフィールドに直接代入します
        this.shadowRadius = 0.5f;
    }
}