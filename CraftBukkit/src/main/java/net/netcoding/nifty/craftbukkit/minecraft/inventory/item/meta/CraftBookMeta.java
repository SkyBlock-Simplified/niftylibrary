package net.netcoding.nifty.craftbukkit.minecraft.inventory.item.meta;

import net.netcoding.nifty.common.minecraft.inventory.item.meta.BookMeta;

import java.util.List;

public final class CraftBookMeta extends CraftItemMeta implements BookMeta {

	public CraftBookMeta(org.bukkit.inventory.meta.BookMeta bookMeta) {
		super(bookMeta);
	}

	@Override
	public void addPage(String... pages) {
		this.getHandle().addPage(pages);
	}

	@Override
	public BookMeta clone() {
		return new CraftBookMeta(this.getHandle().clone());
	}

	@Override
	public String getAuthor() {
		return this.getHandle().getAuthor();
	}

	@Override
	public Generation getGeneration() {
		return Generation.valueOf(this.getHandle().getGeneration().name());
	}

	@Override
	public org.bukkit.inventory.meta.BookMeta getHandle() {
		return (org.bukkit.inventory.meta.BookMeta)super.getHandle();
	}

	@Override
	public List<String> getPages() {
		return this.getHandle().getPages();
	}
	@Override
	public String getTitle() {
		return this.getHandle().getTitle();
	}

	@Override
	public void setAuthor(String author) {
		this.getHandle().setAuthor(author);
	}

	@Override
	public void setGeneration(Generation generation) {
		this.getHandle().setGeneration(org.bukkit.inventory.meta.BookMeta.Generation.valueOf(generation.name()));
	}

	@Override
	public void setPage(int page, String data) {
		this.getHandle().setPage(page, data);
	}

	@Override
	public void setPages(String... pages) {
		this.getHandle().setPages(pages);
	}

	@Override
	public boolean setTitle(String title) {
		return this.getHandle().setTitle(title);
	}

}