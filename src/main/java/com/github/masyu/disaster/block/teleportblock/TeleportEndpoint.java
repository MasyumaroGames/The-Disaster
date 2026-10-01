package com.github.masyu.disaster.block.teleportblock;

import net.minecraft.util.StringRepresentable;

public enum TeleportEndpoint implements StringRepresentable {
    TREE("tree"),
    BOSS("boss");

    private final String name;

    TeleportEndpoint(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
