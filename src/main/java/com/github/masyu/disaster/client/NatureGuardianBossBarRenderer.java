package com.github.masyu.disaster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "disaster",
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class NatureGuardianBossBarRenderer {

    /*
     * =========================================================
     * テクスチャ
     * =========================================================
     */

    // BossBarの背景
    private static final ResourceLocation BOSSBAR_BACKGROUND =
            new ResourceLocation(
                    "disaster",
                    "textures/gui/nature_guardian_bossbar.png"
            );

    // BossBarのHPゲージ
    private static final ResourceLocation BOSSBAR_PROGRESS =
            new ResourceLocation(
                    "disaster",
                    "textures/gui/nature_guardian_bossbar_progress.png"
            );


    /*
     * =========================================================
     * バニラBossBarのサイズ
     * =========================================================
     *
     * Forge 1.20.1 / Minecraft 1.20.1
     * Vanilla BossHealthOverlay準拠
     */

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;


    /*
     * =========================================================
     * BossBar描画
     * =========================================================
     */

    @SubscribeEvent
    public static void onBossBarRender(
            CustomizeGuiOverlayEvent.BossEventProgress event) {

        LerpingBossEvent bossEvent = event.getBossEvent();

        /*
         * -----------------------------------------------------
         * NatureGuardianかどうかをBossBarの名前で判定
         * -----------------------------------------------------
         *
         * 現在NatureGuardianでは
         *
         * Component.translatable(
         *     "boss.disaster.nature_guardian"
         * )
         *
         * を使用しているので、
         * その翻訳キーを比較する。
         */

        Component name = bossEvent.getName();

        if (!isNatureGuardian(name)) {
            return;
        }


        /*
         * -----------------------------------------------------
         * バニラBossBarをキャンセル
         * -----------------------------------------------------
         *
         * 名前・HP・表示位置などはそのまま利用し、
         * テクスチャだけ自前で描画する。
         */

        event.setCanceled(true);


        GuiGraphics guiGraphics = event.getGuiGraphics();


        /*
         * -----------------------------------------------------
         * バニラが計算した座標をそのまま使用
         * -----------------------------------------------------
         */

        int x = event.getX();
        int y = event.getY();


        /*
         * -----------------------------------------------------
         * HP割合
         * -----------------------------------------------------
         */

        float progress = bossEvent.getProgress();

        progress = Mth.clamp(progress, 0.0F, 1.0F);

        int progressWidth =
                (int) (BAR_WIDTH * progress);


        /*
         * -----------------------------------------------------
         * 背景
         * -----------------------------------------------------
         */

        guiGraphics.blit(
                BOSSBAR_BACKGROUND,
                x,
                y,
                0,
                0,
                BAR_WIDTH,
                BAR_HEIGHT,
                BAR_WIDTH,
                BAR_HEIGHT
        );


        /*
         * -----------------------------------------------------
         * HPゲージ
         * -----------------------------------------------------
         */

        if (progressWidth > 0) {

            guiGraphics.blit(
                    BOSSBAR_PROGRESS,
                    x,
                    y,
                    0,
                    0,
                    progressWidth,
                    BAR_HEIGHT,
                    BAR_WIDTH,
                    BAR_HEIGHT
            );
        }


        /*
         * -----------------------------------------------------
         * ボス名
         * -----------------------------------------------------
         *
         * バニラと同じ位置・同じ描画方法で表示。
         *
         * BossBarの名前自体はServerBossEventから
         * 同期されたものをそのまま使用する。
         */

        Font font = Minecraft.getInstance().font;

        int textWidth = font.width(name);

        int textX =
                x + (BAR_WIDTH - textWidth) / 2;

        int textY =
                y - 9;

        guiGraphics.drawString(
                font,
                name,
                textX,
                textY,
                0xFFFFFF
        );
    }


    /*
     * =========================================================
     * NatureGuardian判定
     * =========================================================
     */

    private static boolean isNatureGuardian(Component name) {

        /*
         * 翻訳キーそのものを取得。
         *
         * translatable componentなら
         * getContents()からキーを判定できる。
         */

        if (name.getContents()
                instanceof net.minecraft.network.chat.contents.TranslatableContents translatable) {

            return translatable.getKey().equals(
                    "boss.disaster.nature_guardian"
            );
        }

        return false;
    }
}