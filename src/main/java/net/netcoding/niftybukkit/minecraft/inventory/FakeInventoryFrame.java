package net.netcoding.niftybukkit.minecraft.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftycore.util.ByteUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
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
	private String title = "";

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

	protected int calculateTotalSlots(int value) {
		return value >= 9 ? (value % 9 == 0 ? value : ((int)Math.ceil(value / 9.0) * 9)) : 9;
	}

	public final void clearItems() {
		this.items.clear();
	}

	public final void clearMetadata() {
		this.metadata.clear();
	}

	protected final void createSignature() {
		byte[] signatureBytes = new byte[] {};

		if (this.privateKey != null && this.publicKey != null) {
			try {
				Signature signature = Signature.getInstance("SHA256withRSA");
				signature.initSign(this.privateKey);
				JsonArray json = new JsonArray();

				for (ItemData itemData : this.items.values()) {
					Map<String, Object> serialized = itemData.serialize();
					JsonObject itemJson = new JsonObject();

					for (String key : serialized.keySet())
						itemJson.addProperty(key, serialized.get(key).toString());

					json.add(itemJson);
				}

				signature.update(ByteUtil.toByteArray(json.toString()));
				signatureBytes = signature.sign();
			} catch (Exception ignore) { }
		}

		this.putMetadata(FakeInventory.SIGNATURE_KEY, signatureBytes);
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

	@SuppressWarnings("unchecked")
	public final <T> T getMetadata(String key) {
		return (T)this.metadata.get(key);
	}

	public String getTitle() {
		return this.title;
	}

	public int getTotalSlots() {
		return this.totalSlots >= this.getItems().size() ? this.totalSlots : this.calculateTotalSlots(this.getItems().size());
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

	public final Object putMetadata(String key, Object obj) {
		return this.metadata.put(key, obj);
	}

	public void setTitle(String value) {
		this.title = value;
	}

	public void setTotalSlots(int value) {
		this.totalSlots = this.calculateTotalSlots(value);
	}

	public void setTradingEnabled() {
		this.setTradingEnabled(true);
	}

	public void setTradingEnabled(boolean value) {
		this.tradingEnabled = value;
	}

	void update(FakeInventoryFrame frame) {
		this.items.putAll(frame.items);
		this.metadata.putAll(frame.metadata);
		this.totalSlots = frame.totalSlots;
		this.centered = frame.centered;
		this.allowEmpty = frame.allowEmpty;
		this.tradingEnabled = frame.tradingEnabled;
		this.title = frame.title;
	}

	protected final boolean verifySignature(ItemStack[] items) {
		boolean verified = false;
		byte[] signatureBytes = this.getMetadata(FakeInventory.SIGNATURE_KEY);

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

				signature.update(ByteUtil.toByteArray(json.toString()));
				verified = signature.verify(signatureBytes);
			} catch (Exception ignore) { }
		}

		return verified;
	}

}