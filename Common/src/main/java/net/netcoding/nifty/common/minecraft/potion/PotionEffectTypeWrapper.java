package net.netcoding.nifty.common.minecraft.potion;

class PotionEffectTypeWrapper extends PotionEffectType {

	PotionEffectTypeWrapper(int id) {
		super(id);
	}

	@Override
	public double getDurationModifier() {
		return this.getType().getDurationModifier();
	}

	@Override
	public String getName() {
		return this.getType().getName();
	}

	private PotionEffectType getType() {
		return PotionEffectType.getById(this.getId());
	}

	@Override
	public boolean isInstant() {
		return this.getType().isInstant();
	}

}