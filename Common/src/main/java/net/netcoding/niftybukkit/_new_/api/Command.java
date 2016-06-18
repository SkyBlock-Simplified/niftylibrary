package net.netcoding.niftybukkit._new_.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

	boolean consoleOnly() default false;
	boolean playerOnly() default false;
	boolean checkPerms() default true;
	boolean bungeeOnly() default false;
	boolean helpCheck() default true;
	boolean playerTabComplete() default false;
	int playerTabCompleteIndex() default 0;
	int minimumArgs() default 1;
	int maximumArgs() default -1;
	String permission() default "";

}