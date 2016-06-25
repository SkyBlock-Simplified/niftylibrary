package net.netcoding.nifty.common._new_.minecraft.inventory.item.potion;

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