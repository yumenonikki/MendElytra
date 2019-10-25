package com.github.yumenonikki.mendable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * 設定してある修繕対象のアイテムマテリアルを読み込むクラスです。
 * @author SHC
 *
 */
public class MaterialConfig {

	private List<Material> materials = new ArrayList<>();

	/**
	 *
	 * コンストラクタ
	 * @param plg プラグイン
	 */
	public MaterialConfig(JavaPlugin plg) {

		plg.saveDefaultConfig();
		FileConfiguration config = plg.getConfig();

		//設定からアイテムマテリアルを取得
		ConfigurationSection section = config.getConfigurationSection("ITEMS");

		Set<String> keys = section.getKeys(false);
		for (String key : keys) {
			String name = config.getString("ITEMS." + key);
			Material material = Material.getMaterial(name.toUpperCase());

			//ロードしたアイテムをログに表示
			if(material == null) {
				plg.getLogger().warning("「"+name+"」は無効なアイテム名です。");
			} else if(materials.contains(material)){
				plg.getLogger().warning("「"+name+"」は重複しています。");
			} else {
				plg.getLogger().info("Loaded configration for item :"+name);
				this.materials.add(material);
			}
		}
	}

	/**
	 * 指定のアイテムが修繕対象かをチェックします。
	 * @param material アイテムマテリアル
	 * @return 修繕対象ならtrue
	 */
	public boolean isMendable(Material material) {

		return materials.contains(material);
	}

}
