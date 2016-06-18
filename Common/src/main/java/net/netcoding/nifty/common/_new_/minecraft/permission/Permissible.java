package net.netcoding.nifty.common._new_.minecraft.permission;

public interface Permissible extends ServerOperator {

	boolean isPermissionSet(String permission);

	boolean hasPermission(String permission);

}