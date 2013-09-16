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

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.ItemData;
import com.aionemu.gameserver.dataholders.ItemGroupsData;
import com.aionemu.gameserver.dataholders.QuestsData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestCategory;
import com.aionemu.gameserver.services.reward.BonusService;
import com.aionemu.gameserver.model.templates.quest.QuestItems;

/**
 * @author Rolandas
 */
public class BonusServiceTest {

	static ItemGroupsData itemGroups;
	static QuestsData questData;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void oneTimeSetUp() {
		unmarshallData();
	}

	private static void unmarshallData() {
		File xml = new File("./data/static_data/items/item_groups.xml");
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			schema = sf.newSchema(new File("./data/static_data/items/item_groups.xsd"));
			JAXBContext jc = JAXBContext.newInstance(ItemGroupsData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			itemGroups = (ItemGroupsData) unmarshaller.unmarshal(xml);
		}
		catch (SAXException e1) {
			System.out.println(e1.getMessage());
		}
		catch (JAXBException e2) {
			System.out.println(e2.getMessage());
		}

		xml = new File("./data/static_data/quest_data/quest_data.xml");
		try {
			schema = sf.newSchema(new File("./data/static_data/quest_data/quest_data.xsd"));
			JAXBContext jc = JAXBContext.newInstance(QuestsData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			questData = (QuestsData) unmarshaller.unmarshal(xml);
		}
		catch (SAXException e1) {
			System.out.println(e1.getMessage());
		}
		catch (JAXBException e2) {
			System.out.println(e2.getMessage());
		}
		
		xml = new File("./data/static_data/items/item_templates.xml");
		try {
			schema = sf.newSchema(new File("./data/static_data/static_data.xsd"));
			JAXBContext jc = JAXBContext.newInstance(ItemData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			DataManager.ITEM_DATA = (ItemData) unmarshaller.unmarshal(xml);
		}
		catch (SAXException e1) {
			System.out.println(e1.getMessage());
		}
		catch (JAXBException e2) {
			System.out.println(e2.getMessage());
		}
		
		Config.load();
		/* Comment out these lines in DAOManager.registerDAO() if not using DB:
		if (!dao.supports(getDatabaseName(), getDatabaseMajorVersion(), getDatabaseMinorVersion())) {
			return;
		}
		*/
		DAOManager.init();
	}

	@Test
	public final void testGetCraftBonus() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Constructor<Player> c = Player.class.getDeclaredConstructor(PlayerCommonData.class);
    c.setAccessible(true);
		PlayerCommonData asmoCommon = new PlayerCommonData(0);
		asmoCommon.setRace(Race.ASMODIANS);
    Player asmo = c.newInstance(new Object[] { asmoCommon });
    
		PlayerCommonData elyCommon = new PlayerCommonData(0);
		elyCommon.setRace(Race.ELYOS);
    Player ely = c.newInstance(new Object[] { elyCommon });

		for (QuestTemplate template : questData.getQuestsData()) {
			if (template.getCategory() != QuestCategory.TASK)
				continue;
			QuestItems item = null;
			if (template.getRacePermitted() == Race.PC_ALL) {
				item = BonusService.getInstance(itemGroups).getQuestBonus(asmo, template);
				if (item == null) {
					failTest(template.getCombineSkill(), template.getCombineSkillPoint());
					return;
				}
				item = BonusService.getInstance(itemGroups).getQuestBonus(ely, template);
				if (item == null) {
					failTest(template.getCombineSkill(), template.getCombineSkillPoint());
					return;
				}
			}
			else if (template.getRacePermitted() == Race.ASMODIANS) {
				item = BonusService.getInstance(itemGroups).getQuestBonus(asmo, template);
			}
			else if (template.getRacePermitted() == Race.ELYOS) {
				item = BonusService.getInstance(itemGroups).getQuestBonus(ely, template);
			}
			if (item == null) {
				failTest(template.getCombineSkill(), template.getCombineSkillPoint());
				return;
			}
		}
	}

	void failTest(Integer skill, Integer skillpoints) {
		fail("No reward for skill " + skill + " with points " + skillpoints);
	}

	@Test
	@Ignore
	public final void testGetManastoneBonus() {
		fail("Not yet implemented"); // TODO
	}

}
