/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.fail;
import static ch.lambdaj.Lambda.*;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.ItemData;
import com.aionemu.gameserver.dataholders.ItemRandomBonusData;
import com.aionemu.gameserver.dataholders.ItemSetData;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatCapUtil;
import com.aionemu.gameserver.model.stats.calc.functions.PlayerStatFunctions;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.itemset.ItemSetTemplate;
import com.aionemu.gameserver.network.aion.iteminfo.ItemBlobEntry;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;

/**
 * @author Rolandas
 */
public class ItemStatsTest {

	static List<ItemTemplate> itemsWithRndBonus;
	static Player player;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File xml = new File("./data/static_data/items/item_templates.xml");
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		ItemData items = null;
		ItemRandomBonusData rndBonuses = null;

		try {
			schema = sf.newSchema(new File("./data/static_data/static_data.xsd"));
			JAXBContext jc = JAXBContext.newInstance(ItemData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			items = (ItemData) unmarshaller.unmarshal(xml);

			xml = new File("./data/static_data/items/item_random_bonuses.xml");
			schema = sf.newSchema(new File("./data/static_data/items/item_random_bonuses.xsd"));
			jc = JAXBContext.newInstance(ItemRandomBonusData.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			rndBonuses = (ItemRandomBonusData) unmarshaller.unmarshal(xml);
		}
		catch (SAXException e1) {
			System.out.println(e1.getMessage());
		}
		catch (JAXBException e2) {
			System.out.println(e2.getMessage());
		}

		DataManager.ITEM_DATA = items;
		DataManager.ITEM_RANDOM_BONUSES = rndBonuses;
		Field f = items.getClass().getDeclaredField("items");
		f.setAccessible(true);
		TIntObjectHashMap<ItemTemplate> allTemplates = (TIntObjectHashMap<ItemTemplate>) f.get(items);
		itemsWithRndBonus = filter(having(on(ItemTemplate.class).getRandomBonusId(), greaterThan(0)), allTemplates.values(new ItemTemplate[0]));

		DataManager.ITEM_SET_DATA = new ItemSetData();
		f = ItemSetData.class.getDeclaredField("setItems");
		f.setAccessible(true);
		f.set(DataManager.ITEM_SET_DATA, new TIntObjectHashMap<ItemSetTemplate>());
		
		player = DummyPlayer.createElyo();
		player.getCommonData().setLevel(60);
	}

	@Test
	public void test() {
		PlayerStatFunctions.addPredefinedStatFunctions(player);
		List<ItemInfoBlob> items = new ArrayList<ItemInfoBlob>();
		int objId = 1;
		for (ItemTemplate template : itemsWithRndBonus) {
			Item newItem = new Item(objId++, template, 1, false, 2);
			ItemInfoBlob itemInfoBlob = ItemInfoBlob.getFullBlob(player, newItem);
			int position = 2;
			ByteBuffer buffer = ByteBuffer.allocate(itemInfoBlob.size() + 2);
			buffer.putShort((short) itemInfoBlob.size());
			for (ItemBlobEntry entry : itemInfoBlob.getBlobEntries()) {
				entry.writeThisBlob(buffer);
				position += entry.getSize();
				if (position != buffer.position())
					fail("Blob " + entry.getClass().getSimpleName() + " has invalid size!");
			}
			ItemEquipmentListener.onItemEquipment(newItem, player);
			HashMap<StatEnum, Stat2> allStats = new HashMap<StatEnum, Stat2>();
			for (StatFunction func : newItem.getCurrentModifiers()) {
				allStats.put(func.getName(), player.getGameStats().getStat(func.getName(), 0));
			}
			String itemInfo = "Item Id: " + template.getTemplateId() + ", Rnd: " + template.getRandomBonusId() + ", Bonus: "
				+ newItem.getBonusNumber();
			StatCapUtil.dumpWrongStats(itemInfo, allStats.values().toArray(new Stat2[0]));
			ItemEquipmentListener.onItemUnequipment(newItem, player);
			items.add(itemInfoBlob);
		}
	}

}
