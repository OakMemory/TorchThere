package dev.krysztal.torch.event;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public class ProjectileEvent implements Listener {
    private static final ConcurrentHashMap<Integer, Player> PROJECTILE_CACHE = new ConcurrentHashMap<>();


    @EventHandler
    public void onProjectileHitBlock(ProjectileHitEvent event) {
        if (event.getHitBlock() == null && !(event.getEntity() instanceof Arrow)) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                var opt = Optional.ofNullable(PROJECTILE_CACHE.get(event.getEntity().getEntityId()));
                if (opt.isEmpty()) return;
                var player = opt.get();

                for (ItemStack itemStack : player.getInventory()) {
                    if (event.getHitBlock() == null) break;
                    if (itemStack == null) continue;
                    if (itemStack.getType() == Material.TORCH) {
                        // Fix location
                        Location location = event.getHitBlock().getLocation();
                        location = location.add(0, 1, 0);

                        // Avoid break bedrock
                        if (location.getWorld().getType(location) != Material.AIR) break;

                        // Set type
                        player.getWorld().setType(
                                location,
                                Material.TORCH
                        );
                        spawnTorchPlaceParticle(location);
                        playTorchPlacedSoundAt(location);

                        // Decrease amount
                        if (player.getGameMode() != GameMode.SURVIVAL)
                            itemStack.setAmount(itemStack.getAmount() - 1);

                        break;
                    }
                }

                PROJECTILE_CACHE.remove(event.getEntity().getEntityId());
            }
        }.run();
    }

    @EventHandler
    public void onShootProjectile(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getProjectile() instanceof Arrow arrow)) return;

        PROJECTILE_CACHE.put(arrow.getEntityId(), player);
    }

    private static void spawnTorchPlaceParticle(Location location) {
        location.getWorld().spawnParticle(Particle.COMPOSTER, location, 32);
    }

    private static void playTorchPlacedSoundAt(Location location) {
        var random = new Random().nextInt(5);
        location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 2 + random, 1 + (float) random / 10);
    }
}
