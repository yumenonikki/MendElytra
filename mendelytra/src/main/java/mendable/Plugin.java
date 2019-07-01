package mendable;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onExpChange(PlayerExpChangeEvent event) {
		ItemStack item = event.getPlayer().getInventory().getChestplate();
		if(item == null) {
			return;
		}
		if(item.getType() == Material.ELYTRA) {
			ItemMeta meta = item.getItemMeta();
			int damage = ((Damageable) meta).getDamage();

			if(damage > 0) {
				int mend = damage - event.getAmount();
				((Damageable) meta).setDamage(mend);
				item.setItemMeta(meta);
				event.getPlayer().getInventory().setChestplate(item);
			}
		}
	}
}
