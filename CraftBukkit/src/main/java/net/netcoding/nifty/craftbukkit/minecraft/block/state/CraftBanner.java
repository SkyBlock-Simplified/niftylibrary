package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.Banner;
import net.netcoding.nifty.core.api.color.DyeColor;
import net.netcoding.nifty.core.util.concurrent.Concurrent;

import java.util.List;

public final class CraftBanner extends CraftBlockState implements Banner {

	public CraftBanner(org.bukkit.block.Banner banner) {
		super(banner);
	}

	@Override
	public void addPattern(Pattern pattern) {
		this.getHandle().addPattern(this.createPattern(pattern));
	}

	private org.bukkit.block.banner.Pattern createPattern(Pattern pattern) {
		return new org.bukkit.block.banner.Pattern(
				org.bukkit.DyeColor.valueOf(pattern.getColor().name()),
				org.bukkit.block.banner.PatternType.getByIdentifier(pattern.getPattern().getIdentifier())
		);
	}

	@Override
	public DyeColor getBaseColor() {
		return DyeColor.valueOf(this.getHandle().getBaseColor().name());
	}

	@Override
	public org.bukkit.block.Banner getHandle() {
		return (org.bukkit.block.Banner)super.getHandle();
	}

	@Override
	public List<Pattern> getPatterns() {
		return this.getHandle().getPatterns().stream()
				.map(pattern ->new Pattern(
						DyeColor.valueOf(pattern.getColor().name()),
						PatternType.getByIdentifier(pattern.getPattern().getIdentifier())))
				.collect(Concurrent.toList());
	}

	@Override
	public Pattern removePattern(int index) {
		org.bukkit.block.banner.Pattern pattern = this.getHandle().removePattern(index);
		return new Pattern(DyeColor.valueOf(pattern.getColor().name()), PatternType.getByIdentifier(pattern.getPattern().getIdentifier()));
	}

	@Override
	public void setBaseColor(DyeColor color) {
		this.getHandle().setBaseColor(org.bukkit.DyeColor.valueOf(color.name()));
	}

	@Override
	public void setPattern(int index, Pattern pattern) {
		this.getHandle().setPattern(index, this.createPattern(pattern));
	}

	@Override
	public void setPatterns(List<Pattern> patterns) {
		this.getHandle().setPatterns(patterns.stream().map(this::createPattern).collect(Concurrent.toList()));
	}

}