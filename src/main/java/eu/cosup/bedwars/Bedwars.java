package eu.cosup.bedwars;

import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private static Bedwars instance;

    public static Bedwars getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
