package net.netcoding.niftybukkit.yaml;

import java.io.File;

import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

public interface IConfig {
    public void save() throws InvalidConfigurationException;
    public void save(File file) throws InvalidConfigurationException;

    public void init() throws InvalidConfigurationException;
    public void init(File file) throws InvalidConfigurationException;

    public void reload() throws InvalidConfigurationException;

    public void load() throws InvalidConfigurationException;
    public void load(File file) throws InvalidConfigurationException;
}
