package dev.krysztal.torch;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import dev.krysztal.torch.event.ProjectileEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TorchThereMain extends JavaPlugin {

    public static final Logger LOGGER = PaperPluginLogger.getLogger("TorchThere");

    @Override
    public void onEnable() {
        LOGGER.info("Registering events.");
        Bukkit.getPluginManager().registerEvents(new ProjectileEvent(), this);

        LOGGER.info("Enabled " + this.getName());
    }
}
