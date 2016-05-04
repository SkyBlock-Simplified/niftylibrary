package net.netcoding.niftybukkit.minecraft.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftycore.util.ByteUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public abstract class FakeInventoryFrame extends BukkitListener implements Iterable<ItemData> {

	private final ConcurrentMap<Integer, ItemData> items = new ConcurrentMap<>();
	private final ConcurrentMap<String, Object> metadata = new ConcurrentMap<>();
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private int totalSlots = -1;
	private boolean allowEmpty = false;
	private boolean centered = false;
	private boolean tradingEnabled = false;
	private ItemData pageLeft;
	private int pageLeftSlot = -1;
	private ItemData pageRight;
	private int pageRightSlot = -1;
	private int currentPage = 1;
	private String title = "";
	// http://www.planetminecraft.com/banner/arrow-pointing-right/
	// http://www.planetminecraft.com/banner/arrow-pointing-left-35059/

	FakeInventoryFrame(FakeInventoryFrame frame) {
		this(frame.getPlugin());
		this.update(frame);
	}

	FakeInventoryFrame(JavaPlugin plugin) {
		super(plugin);

		PrivateKey privateKey = null;
		PublicKey publicKey = null;

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		} catch (Exception ignore) { }

		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	private int getMax() {
		return this.items.isEmpty() ? 0 : Collections.max(this.items.keySet()) + 1;
	}

	public void add(int index, ItemData itemData) {
		this.items.put(index, itemData.clone());
	}

	public void add(int index, ItemStack itemStack) {
		this.add(index, new ItemData(itemStack.clone()));
	}

	public void add(ItemData itemData) {
		this.items.put(getMax(), itemData.clone());
	}

	public void add(ItemStack itemStack) {
		this.add(new ItemData(itemStack.clone()));
	}

	public <T extends ItemStack> void addAll(T[] items) {
		for (ItemStack itemStack : items) {
			if (itemStack == null) continue;
			this.items.put(getMax(), new ItemData(itemStack.clone()));
		}
	}

	public void addAll(Collection<? extends ItemStack> items) {
		for (ItemStack itemStack : items) {
			if (itemStack == null) continue;
			this.items.put(getMax(), new ItemData(itemStack.clone()));
		}
	}

	public <T extends ItemStack> void addAll(int index, T[] items) {
		this.addAll(index, Arrays.asList(items));
	}

	public void addAll(int index, Collection<? extends ItemStack> items) {
		int i = index;

		for (ItemStack itemStack : items)
			this.items.put(i++, new ItemData(itemStack.clone()));
	}

	public static int calculateTotalSlots(int value) {
		int calc = (value >= 9 ? (value % 9 == 0 ? value : ((int)Math.ceil(value / 9.0) * 9)) : 9);
		return calc > 54 ? 54 : calc;
	}

	public final void clearItems() {
		this.items.clear();
	}

	public final void clearMetadata() {
		this.metadata.clear();
	}

	protected final void generateSignature(ItemStack[] items) {
		byte[] signatureBytes = new byte[] {};

		if (this.privateKey != null && this.publicKey != null) {
			try {
				Signature signature = Signature.getInstance("SHA256withRSA");
				signature.initSign(this.privateKey);
				JsonArray json = new JsonArray();

				for (ItemStack itemStack : items) {
					if (itemStack == null) continue;
					Map<String, Object> serialized = itemStack.serialize();
					JsonObject itemJson = new JsonObject();

					for (String key : serialized.keySet())
						itemJson.addProperty(key, serialized.get(key).toString());

					json.add(itemJson);
				}

				byte[] bytes = ByteUtil.toByteArray(json.toString());
				signature.update(bytes);
				signatureBytes = signature.sign();
			} catch (Exception ignore) { }
		}


		this.putMetadata(NbtKeys.SIGNATURE.getKey(), signatureBytes);
	}

	public final Map<String, Object> getAllMetadata() {
		ConcurrentMap<String, Object> metadata = new ConcurrentMap<>();
		metadata.putAll(this.metadata);
		return Collections.unmodifiableMap(metadata);
	}

	public final Map<Integer, ItemData> getItems() {
		ConcurrentMap<Integer, ItemData> items = new ConcurrentMap<>();
		items.putAll(this.items);
		return Collections.unmodifiableMap(items);
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	final <T> T getMetadata(NbtKeys key) {
		return this.getMetadata(key.getKey());
	}

	@SuppressWarnings("unchecked")
	public final <T> T getMetadata(String key) {
		return (T)this.metadata.get(key);
	}

	public ItemData getPageLeft() {
		return this.pageLeft;
	}

	public int getPageLeftSlot() {
		return this.pageLeftSlot;
	}

	public ItemData getPageRight() {
		return this.pageRight;
	}

	public int getPageRightSlot() {
		return this.pageRightSlot;
	}

	public String getTitle() {
		return this.title;
	}

	public int getTotalSlots() {
		return this.totalSlots > 0 ? this.totalSlots : calculateTotalSlots(this.getItems().size());
	}

	final boolean hasMetadata(NbtKeys key) {
		return this.hasMetadata(key.getKey());
	}

	public final boolean hasMetadata(String key) {
		return this.metadata.containsKey(key);
	}

	public boolean isAutoCentered() {
		return this.centered;
	}

	public boolean isAllowEmpty() {
		return this.allowEmpty;
	}

	public boolean isTradingEnabled() {
		return this.tradingEnabled;
	}

	@Override
	public Iterator<ItemData> iterator() {
		return Collections.unmodifiableCollection(this.items.values()).iterator();
	}

	public void putAll(Map<Integer, ItemData> items) {
		this.items.putAll(items);
	}

	final Object putMetadata(NbtKeys key, Object obj) {
		return this.putMetadata(key.getKey(), obj);
	}

	public final Object putMetadata(String key, Object obj) {
		return this.metadata.put(key, obj);
	}

	final void removeMetadata(NbtKeys key) {
		this.removeMetadata(key.getKey());
	}

	public final void removeMetadata(String key) {
		this.metadata.remove(key);
	}

	public void setAllowEmpty() {
		this.setAllowEmpty(true);
	}

	public void setAllowEmpty(boolean value) {
		this.allowEmpty = value;
	}

	public void setAutoCenter() {
		this.setAutoCenter(true);
	}

	public void setAutoCenter(boolean value) {
		this.centered = value;
	}

	void setCurrentPage(int value) {
		this.currentPage = value;

		if (this.pageLeft != null && this.pageRight != null) {
			this.pageLeft.putNbt(NbtKeys.PAGING.getKey(), value - 1);
			this.pageLeft.putNbt(NbtKeys.PAGING.getKey(), value + 1);
		}
	}

	public void setPaging(ItemData pageLeft, ItemData pageRight) {
		this.setPaging(-1, pageLeft, -1, pageRight);
	}

	public void setPaging(int pageLeftIndex, ItemData pageLeft, int pageRightIndex, ItemData pageRight) {
		if (pageLeft != null && Material.AIR != pageLeft.getType() && pageRight != null && Material.AIR != pageRight.getType()) {
			this.pageLeftSlot = pageLeftIndex;
			this.pageRightSlot = pageRightIndex;
			this.pageLeft = pageLeft.clone();
			this.pageRight = pageRight.clone();
			this.setCurrentPage(this.getCurrentPage());
		}
	}

	public void setTitle(String value) {
		this.title = value;
	}

	public void setTotalSlots(int value) {
		this.totalSlots = calculateTotalSlots(value);
	}

	public void setTradingEnabled() {
		this.setTradingEnabled(true);
	}

	public void setTradingEnabled(boolean value) {
		this.tradingEnabled = value;
	}

	void update(FakeInventoryFrame frame) {
		this.items.clear();
		this.putAll(frame.items);
		this.metadata.clear();
		this.metadata.putAll(frame.metadata);
		this.setTotalSlots(frame.totalSlots);
		this.setAllowEmpty(frame.allowEmpty);
		this.setAutoCenter(frame.centered);
		this.setTradingEnabled(frame.tradingEnabled);
		this.setTitle(frame.title);
		this.currentPage = frame.currentPage;

		if (frame.pageLeft != null && frame.pageRight != null)
			this.setPaging(frame.pageLeftSlot, frame.pageLeft, frame.pageRightSlot, frame.pageRight);
	}

	protected final boolean verifySignature(ItemStack[] items) {
		boolean verified = false;
		byte[] signatureBytes = this.getMetadata(NbtKeys.SIGNATURE.getKey());

		if (this.privateKey != null && this.publicKey != null) {
			try {
				Signature signature = Signature.getInstance("SHA256withRSA");
				signature.initVerify(this.publicKey);
				JsonArray json = new JsonArray();

				for (ItemStack itemStack : items) {
					if (itemStack == null) continue;
					Map<String, Object> serialized = itemStack.serialize();
					JsonObject itemJson = new JsonObject();

					for (String key : serialized.keySet())
						itemJson.addProperty(key, serialized.get(key).toString());

					json.add(itemJson);
				}

				byte[] bytes = ByteUtil.toByteArray(json.toString());
				signature.update(bytes);
				verified = signature.verify(signatureBytes);
			} catch (Exception ignore) { }
		}

		return verified;
	}

}