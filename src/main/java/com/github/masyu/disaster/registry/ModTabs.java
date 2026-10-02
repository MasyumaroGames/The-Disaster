package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;
import com.github.masyu.disaster.tab.MainTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {

    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Disaster.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = MOD_TABS.register("main_tab",
            ()->{return CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModBlocks.Blocks.ALTAR_OF_NATURE.get()))
                    .title(Component.translatable("itemGroup.main_tab"))
                    .displayItems((param, output) -> {
                        for (Item item : MainTab.items) {
                            output.accept(item);
                        }
                    })
                    .build();
            });

}
