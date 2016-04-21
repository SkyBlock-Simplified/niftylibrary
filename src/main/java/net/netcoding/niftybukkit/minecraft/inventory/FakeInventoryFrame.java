package net.netcoding.niftybukkit.minecraft.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftycore.util.ByteUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public abstract class FakeInventoryFrame extends BukkitListener implements Iterable<ItemData> {

	private ConcurrentList<ItemData> items = new ConcurrentList<>();
	private ConcurrentMap<String, Object> metadata = new ConcurrentMap<>();
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private int totalSlots = -1;
	private boolean centered = false;
	private boolean autoCancel = false;
	private boolean tradingEnabled = false;
	private String title = "";

	FakeInventoryFrame(FakeInventoryFrame frame) {
		this(frame.getPlugin());
		this.items = frame.items;
		this.metadata = frame.metadata;
		this.totalSlots = frame.totalSlots;
		this.centered = frame.centered;
		this.autoCancel = frame.autoCancel;
		this.tradingEnabled = frame.tradingEnabled;
		this.title = frame.title;
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

	public void add(int index, ItemData item) {
		this.items.add(index, item);
	}

	public void add(int index, ItemStack item) {
		this.add(index, new ItemData(item));
	}

	public void add(ItemData item) {
		this.items.add(item);
	}

	public void add(ItemStack item) {
		this.add(new ItemData(item));
	}

	public void addAll(Collection<? extends ItemData> collection) {
		this.items.addAll(collection);
	}

	public void addAll(int index, Collection<? extends ItemData> collection) {
		this.items.addAll(index, collection);
	}

	protected int calculateTotalSlots(int value) {
		return value >= 9 ? (value % 9 == 0 ? value : ((int)Math.ceil(value / 9.0) * 9)) : 9;
	}

	public void clearMetadata() {
		this.metadata.clear();
	}

	protected final void createSignature() {
		byte[] signatureBytes = new byte[] {};

		if (this.privateKey != null && this.publicKey != null) {
			try {
				Signature signature = Signature.getInstance("SHA256withRSA");
				signature.initSign(this.privateKey);
				JsonArray json = new JsonArray();

				for (ItemData itemData : this.items) {
					Map<String, Object> serialized = itemData.asCraftCopy().serialize();
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

	ConcurrentList<ItemData> getItems() {
		return this.items;
	}

	@SuppressWarnings("unchecked")
	public <T> T getMetadata(String key) {
		return (T)this.metadata.get(key);
	}

	public String getTitle() {
		return this.title;
	}

	public int getTotalSlots() {
		return this.totalSlots >= this.getItems().size() ? this.totalSlots : this.calculateTotalSlots(this.getItems().size());
	}

	public boolean hasMetadata(String key) {
		return this.metadata.containsKey(key);
	}

	public boolean isAutoCancelled() {
		return this.autoCancel;
	}

	public boolean isAutoCentered() {
		return this.centered;
	}

	public boolean isTradingEnabled() {
		return this.tradingEnabled;
	}

	@Override
	public Iterator<ItemData> iterator() {
		return Collections.unmodifiableList(this.items).iterator();
	}

	public void removeMetadata(String key) {
		this.metadata.remove(key);
	}

	public void setAutoCancelled() {
		this.setAutoCancelled(true);
	}

	public void setAutoCancelled(boolean value) {
		this.autoCancel = value;
	}

	public void setAutoCenter() {
		this.setAutoCenter(true);
	}

	public void setAutoCenter(boolean value) {
		this.centered = value;
	}

	public Object putMetadata(String key, Object obj) {
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