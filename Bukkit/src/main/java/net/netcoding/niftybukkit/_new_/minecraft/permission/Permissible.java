package net.netcoding.niftybukkit._new_.minecraft.permission;

public interface Permissible extends ServerOperator {

	boolean isPermissionSet(String permission);

	boolean hasPermission(String permission);

}