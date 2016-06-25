package net.netcoding.nifty.common.minecraft.permission;

public interface Permissible extends ServerOperator {

	boolean isPermissionSet(String permission);

	boolean hasPermission(String permission);

}