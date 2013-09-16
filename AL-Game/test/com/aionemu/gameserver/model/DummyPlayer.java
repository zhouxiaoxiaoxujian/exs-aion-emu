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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.controllers.movement.PlayerMoveController;
import com.aionemu.gameserver.dataholders.AbsoluteStatsData;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayerExperienceTable;
import com.aionemu.gameserver.dataholders.PlayerStatsData;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.storage.PlayerStorage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.stats.container.PlayerGameStats;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.world.knownlist.KnownList;

/**
 * @author Rolandas
 */
public final class DummyPlayer {

	private static PlayerStatsData dummyStats;
	private static AbsoluteStatsData dummyAbsStats;
	private static PlayerExperienceTable expTable;
	private static String DEFAULT_STATS = "assassin-templates.xml";
	private static PlayerClass DEFAULT_CLASS = PlayerClass.ASSASSIN;

	static {
		File xml = new File("./data/static_data/stats/player/" + DEFAULT_STATS);
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema;
		try {
			schema = sf.newSchema(new File("./data/static_data/stats/stats.xsd"));
			JAXBContext jc;
			jc = JAXBContext.newInstance(PlayerStatsData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			dummyStats = (PlayerStatsData) unmarshaller.unmarshal(xml);
			
			xml = new File("./data/static_data/stats/absolute_stats.xml");
			schema = sf.newSchema(new File("./data/static_data/stats/absolute_stats.xsd"));
			jc = JAXBContext.newInstance(AbsoluteStatsData.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			dummyAbsStats = (AbsoluteStatsData) unmarshaller.unmarshal(xml);
			
			xml = new File("./data/static_data/player_experience_table.xml");
			schema = sf.newSchema(new File("./data/static_data/player_experience_table.xsd"));
			jc = JAXBContext.newInstance(PlayerExperienceTable.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			expTable = (PlayerExperienceTable) unmarshaller.unmarshal(xml);
		}
		catch (SAXException e) {
			e.printStackTrace();
		}
		catch (JAXBException e) {
			e.printStackTrace();
		}
		DataManager.PLAYER_STATS_DATA = dummyStats;
		DataManager.ABSOLUTE_STATS_DATA = dummyAbsStats;
		DataManager.PLAYER_EXPERIENCE_TABLE = expTable;
	}

	public static Player createAsmodian() {
		return create(Race.ASMODIANS, DEFAULT_CLASS, "asmo");
	}

	public static Player createElyo() {
		return create(Race.ELYOS, DEFAULT_CLASS, "ely");
	}

	private static Player create(Race race, PlayerClass playerClass, String name) {
		Player player = null;
		try {
			Constructor<Player> c = Player.class.getDeclaredConstructor(PlayerCommonData.class);
			c.setAccessible(true);
			PlayerCommonData asmoCommon = new PlayerCommonData(1);
			asmoCommon.setRace(race);
			asmoCommon.setGender(Gender.MALE);
			asmoCommon.setPlayerClass(playerClass);
			asmoCommon.setBoundingRadius(new BoundRadius(0.5f, 0.5f, 2));
			asmoCommon.setName(name);
			player = c.newInstance(new Object[] { asmoCommon });
			player.setGameStats(new PlayerGameStats(player));
			player.setLifeStats(new PlayerLifeStats(player));
			player.setKnownlist(new KnownList(player));
			player.getController().setOwner(player);
			Field mv = Player.class.getSuperclass().getDeclaredField("moveController");
			mv.setAccessible(true);
			mv.set(player, new PlayerMoveController(player));
			player.setEquipment(new Equipment(player));
			player.getPlayerAccount().setName("Dummy");
			player.setEffectController(new PlayerEffectController(player));
			player.setStorage(new PlayerStorage(StorageType.CUBE), StorageType.CUBE);
		}
		catch (Exception e) {
		}
		return player;
	}

}
