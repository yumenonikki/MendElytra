package com.github.yumenonikki.mendable;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * 修繕プラグインのメインクラスです。
 * @author SHC
 *
 */
public class Plugin extends JavaPlugin implements Listener{

	private MaterialConfig config = null;

	/**
	 *
	 * 起動時に実行。
	 */
	@Override
	public void onEnable() {

		//コンフィグを取得
		this.config = new MaterialConfig(this);

		//イベント登録
		getServer().getPluginManager().registerEvents(this, this);
	}

	/**
	 *
	 * イベントリスナ
	 * 経験値を取得したときに実行。
	 * @param event
	 */
	@EventHandler
	public void onExpChange(PlayerExpChangeEvent event) {

		Map<Integer, ItemStack> md = new HashMap<>();

		//オフハンドとアーマーを取得
		ItemStack off = event.getPlayer().getInventory().getItemInOffHand();
		ItemStack[] amr = event.getPlayer().getInventory().getArmorContents();

		//オフハンドをチェック
		if(config.isMendable(off.getType())) {
			md.put(40, off);
		}

		//アーマーをチェック
		for(int i = 0; i < amr.length; i++) {
			if(amr[i] == null) {
				continue;
			}

			if(config.isMendable(amr[i].getType())) {
				md.put(i + 36, amr[i]);
			}
		}

		if(md.isEmpty()) {
			return;
		}

		int exp = event.getAmount();

		//修繕する
		for(int slot :md.keySet()) {
			ItemStack item = md.get(slot);
			ItemMeta meta = item.getItemMeta();

			int damage = ((Damageable) meta).getDamage();

			if(damage == 0) {
				continue;
			}

			int mend = damage - exp;
			exp = 0;

			if(mend < 0) {
				exp = mend * -1;
				mend = 0;
			}

			((Damageable) meta).setDamage(mend);
			item.setItemMeta(meta);
		}
	}
}
