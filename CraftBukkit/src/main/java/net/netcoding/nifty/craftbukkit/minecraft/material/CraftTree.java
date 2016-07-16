package net.netcoding.nifty.craftbukkit.minecraft.material;

import net.netcoding.nifty.common.minecraft.material.Tree;

public final class CraftTree extends CraftWood implements Tree {

	public CraftTree(org.bukkit.material.Tree tree) {
		super(tree);
	}

	@Override
	public Tree clone() {
		return (Tree)super.clone();
	}

}