package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.block.state.Banner;
import net.netcoding.nifty.common.minecraft.inventory.item.meta.BannerMeta;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.api.color.DyeColor;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.craftbukkit.util.CraftConverter;

import java.util.List;

public final class CraftBannerMeta extends CraftItemMeta implements BannerMeta {

	public CraftBannerMeta(org.bukkit.inventory.meta.BannerMeta bannerMeta) {
		super(bannerMeta);
	}

	@Override
	public void addPattern(Banner.Pattern pattern) {
		this.getHandle().addPattern(CraftConverter.toBukkitPattern(pattern));
	}

	@Override
	public BannerMeta clone() {
		BannerMeta bannerMeta = (BannerMeta)Nifty.getItemFactory().getItemMeta(Material.BANNER);
		bannerMeta.addItemFlags(this.getItemFlags());
		bannerMeta.setBaseColor(this.getBaseColor());
		bannerMeta.setDisplayName(this.getDisplayName());
		this.getEnchants().forEach(bannerMeta::addEnchant);
		bannerMeta.setLore(this.getLore());
		bannerMeta.setPatterns(this.getPatterns());
		return bannerMeta;
	}

	@Override
	public DyeColor getBaseColor() {
		return DyeColor.valueOf(this.getHandle().getBaseColor().name());
	}

	@Override
	public org.bukkit.inventory.meta.BannerMeta getHandle() {
		return (org.bukkit.inventory.meta.BannerMeta)super.getHandle();
	}

	@Override
	public List<Banner.Pattern> getPatterns() {
		return this.getHandle().getPatterns().stream().map(CraftConverter::fromBukkitPattern).collect(Concurrent.toList());
	}

	@Override
	public Banner.Pattern removePattern(int index) {
		return CraftConverter.fromBukkitPattern(this.getHandle().removePattern(index));
	}

	@Override
	public void setBaseColor(DyeColor color) {
		this.getHandle().setBaseColor(org.bukkit.DyeColor.valueOf(color.name()));
	}

	@Override
	public void setPattern(int index, Banner.Pattern pattern) {
		this.getHandle().setPattern(index, CraftConverter.toBukkitPattern(pattern));
	}

	@Override
	public void setPatterns(List<Banner.Pattern> patterns) {
		this.getHandle().setPatterns(patterns.stream().map(CraftConverter::toBukkitPattern).collect(Concurrent.toList()));
	}

}