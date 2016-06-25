package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.api.MinecraftLogger;
import net.netcoding.nifty.core.api.plugin.PluginDescription;

public abstract class MinecraftPlugin implements IMinecraftPlugin {

	private final MinecraftLogger logger = new MinecraftLogger(this);

	public MinecraftPlugin() {
		// TODO: Register Plugin
		// TODO: loader1 = new PluginClassLoader(this, this.getClass().getClassLoader(), description, dataFolder, file);
		// TODO: Plugin Annotation
		/*
            this.name = this.rawName = map.get("name").toString();
            if(!this.name.matches("^[A-Za-z0-9 _.-]+$")) {
                throw new InvalidDescriptionException("name \'" + this.name + "\' contains invalid characters.");
            }

            this.name = this.name.replace(' ', '_');

            jar = new JarFile(file);
            JarEntry ex = jar.getJarEntry("plugin.yml");
            if(ex == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(ex);
            var5 = new PluginDescriptionFile(stream);
		 */
	}

	@Override
	public final MinecraftLogger getLog() {
		return this.logger;
	}

	@Override
	public final PluginDescription getPluginDescription() {
		return null; // TODO
	}

}