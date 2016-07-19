package net.netcoding.nifty.common.minecraft.inventory.item;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.nbt.NbtCompound;
import net.netcoding.nifty.common.minecraft.inventory.EquipmentSlot;
import net.netcoding.nifty.core.api.builder.BuilderCore;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Arrays;
import java.util.UUID;

public final class Attribute {

	private final NbtCompound compound = Nifty.getNbtFactory().createCompound();

	public Attribute() {
		this.setOperation(Operation.ADD_NUMBER);
		this.setUniqueId(UUID.randomUUID());
	}

	Attribute(NbtCompound compound) {
		this.compound.putAll(compound);
	}

	private Attribute(Builder builder) {
		this.setAmount(builder.amount);
		this.setOperation(builder.operation);
		this.setName(builder.name);
		this.setType(builder.type);
		this.setUniqueId(builder.uniqueId);
	}

	public static Builder builder() {
		return Nifty.getBuilderManager().createBuilder(Attribute.class);
	}

	public double getAmount() {
		return this.getNbt().get("Amount");
	}

	public Operation getOperation() {
		return Operation.getById(this.getNbt().get("Operation", 0));
	}

	public String getName() {
		return this.getNbt().get("Name");
	}

	public NbtCompound getNbt() {
		return this.compound;
	}

	public EquipmentSlot getSlot() {
		return EquipmentSlot.getByKey(this.getNbt().get("Slot"));
	}

	public Type getType() {
		return Type.getById(this.getNbt().get("AttributeName", null));
	}

	public UUID getUniqueId() {
		return new UUID(this.getNbt().get("UUIDMost", null), this.getNbt().get("UUIDLeast", null));
	}

	public void setAmount(double amount) {
		this.getNbt().put("Amount", amount);
	}

	public void setOperation(Operation operation) {
		Preconditions.checkArgument(operation != null, "Operation cannot be NULL!");
		this.getNbt().put("Operation", operation.getId());
	}

	public void setName(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");
		this.getNbt().put("Name", name);
	}

	public void setSlot(EquipmentSlot slot) {
		Preconditions.checkArgument(slot != null, "Slot cannot be NULL!");
		this.getNbt().put("Slot", slot.getKey());
	}

	public void setType(Type type) {
		Preconditions.checkArgument(type != null, "Type cannot be NULL!");
		this.getNbt().put("AttributeName", type.getMinecraftId());
	}

	public void setUniqueId(UUID uniqueId) {
		Preconditions.checkArgument(uniqueId != null, "UniqueId cannot be NULL!");
		this.getNbt().put("UUIDMost", uniqueId.getMostSignificantBits());
		this.getNbt().put("UUIDLeast", uniqueId.getLeastSignificantBits());
	}

	void update(Attribute attribute) {
		this.setAmount(attribute.getAmount());
		this.setOperation(attribute.getOperation());
		this.setName(attribute.getName());
		this.setUniqueId(attribute.getUniqueId());
	}

	public static class Builder implements BuilderCore<Attribute> {

		private double amount;
		private Operation operation = Operation.ADD_NUMBER;
		private Type type;
		private String name;
		private UUID uniqueId = UUID.randomUUID();

		private Builder() { }

		public Builder amount(double amount) {
			this.amount = amount;
			return this;
		}

		@Override
		public Attribute build() {
			return new Attribute(this);
		}

		public Builder operation(Operation operation) {
			this.operation = operation;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder type(Type type) {
			this.type = type;
			return this;
		}

		public Builder uniqueId(UUID uniqueId) {
			this.uniqueId = uniqueId;
			return this;
		}

	}

	public enum Operation {

		ADD_NUMBER(0),
		MULTIPLY_PERCENTAGE(1),
		ADD_PERCENTAGE(2);

		private static final ConcurrentMap<Integer, Operation> BY_ID = Concurrent.newMap();
		private final int id;

		static {
			Arrays.stream(values()).forEach(operation -> BY_ID.put(operation.getId(), operation));
		}

		Operation(int id) {
			this.id = id;
		}

		public static Operation getById(int id) {
			return BY_ID.containsKey(id) ? BY_ID.get(id) : ADD_NUMBER;
		}

		public int getId() {
			return this.id;
		}

	}

	public static class Type {

		private static final ConcurrentMap<String, Type> LOOKUP = Concurrent.newMap();
		public static final Type GENERIC_MAX_HEALTH = new Type("generic.maxHealth").register();
		public static final Type GENERIC_FOLLOW_RANGE = new Type("generic.followRange").register();
		public static final Type GENERIC_ATTACK_DAMAGE = new Type("generic.attackDamage").register();
		public static final Type GENERIC_MOVEMENT_SPEED = new Type("generic.movementSpeed").register();
		public static final Type GENERIC_KNOCKBACK_RESISTANCE = new Type("generic.knockbackResistance").register();
		private final String minecraftId;

		public Type(String minecraftId) {
			this.minecraftId = minecraftId;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			else if (!(obj instanceof Type))
				return false;
			else {
				Type other = (Type)obj;
				return this.getMinecraftId().equalsIgnoreCase(other.getMinecraftId());
			}
		}

		public static Type getById(String minecraftId) {
			return LOOKUP.get(minecraftId);
		}

		public String getMinecraftId() {
			return this.minecraftId;
		}

		@Override
		public int hashCode() {
			return this.getMinecraftId().hashCode();
		}

		public Type register() {
			Type old = LOOKUP.putIfAbsent(this.getMinecraftId(), this);
			return old != null ? old : this;
		}

		public static Iterable<Type> values() {
			return LOOKUP.values();
		}

	}

}