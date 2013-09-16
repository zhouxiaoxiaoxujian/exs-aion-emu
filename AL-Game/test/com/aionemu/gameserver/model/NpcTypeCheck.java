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

import static ch.lambdaj.collection.LambdaCollections.with;

import java.io.File;
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

import ch.lambdaj.function.convert.Converter;

import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.NpcData;
import com.aionemu.gameserver.dataholders.NpcSkillData;
import com.aionemu.gameserver.dataholders.TribeRelationsData;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

/**
 * @author Rolandas
 */
public class NpcTypeCheck {

	static Player player;
	static List<SniffedNpc> sniffedNpcs;
	static HashMap<Integer, Byte> npcTypes = new HashMap<Integer, Byte>();
	static int npcCount = 0;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File xml = new File("./data/static_data/tribe/tribe_relations.xml");
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		TribeRelationsData tribeRelations = null;
		NpcData npcTemplates = null;

		try {
			schema = sf.newSchema(new File("./data/static_data/tribe/tribe_relations.xsd"));
			JAXBContext jc = JAXBContext.newInstance(TribeRelationsData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			tribeRelations = (TribeRelationsData) unmarshaller.unmarshal(xml);

			xml = new File("./data/static_data/npcs/npc_templates.xml");
			schema = sf.newSchema(new File("./data/static_data/npcs/npcs.xsd"));
			jc = JAXBContext.newInstance(NpcData.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			npcTemplates = (NpcData) unmarshaller.unmarshal(xml);

			xml = new File("./test/data/npc_data.xml");
			jc = JAXBContext.newInstance(NpcStats.class);
			unmarshaller = jc.createUnmarshaller();
			sniffedNpcs = ((NpcStats) unmarshaller.unmarshal(xml)).getNpcStat();
		}
		catch (SAXException e1) {
			System.out.println(e1.getMessage());
		}
		catch (JAXBException e2) {
			System.out.println(e2.getMessage());
		}

		// Not interesting
		DataManager.NPC_SKILL_DATA = new NpcSkillData();
		DataManager.NPC_DATA = npcTemplates;
		DataManager.TRIBE_RELATIONS_DATA = tribeRelations;
		AI2Engine.getInstance().load(null);

		player = DummyPlayer.createAsmodian();
	}

	@Test
	public void test() {
		final List<Npc> npcs = with(sniffedNpcs).convert(new Converter<SniffedNpc, Npc>() {

			@Override
			public Npc convert(SniffedNpc fromSniff) {
				NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(fromSniff.getNpcId());
				if (template == null)
					return null;
				SpawnTemplate spawn = SpawnEngine.addNewSpawn(DummyWorldMapData.DEFAULT_MAP, template.getTemplateId(), 0f, 0f, 0f, (byte) 0, 0);
				npcCount++;
				Npc npc = new Npc(1000000 + npcCount, new NpcController(), spawn, template);
				npcTypes.put(fromSniff.getNpcId(), fromSniff.getNpcType());
				return npc;
			}
		});

		HashMap<TribeClass, CreatureType> invalidTypes = new HashMap<TribeClass, CreatureType>();

		for (Npc npc : npcs) {
			if (npc == null)
				continue;
			int ourNpcType = npc.getType(player);
			byte sniffedNpcType = npcTypes.get(npc.getNpcId());
			if (ourNpcType != sniffedNpcType) {
				if (invalidTypes.containsKey(npc.getTribe())) {
					CreatureType check1 = invalidTypes.get(npc.getTribe());
					CreatureType check2 = CreatureType.getCreatureType(sniffedNpcType);
					if (check1 != check2) {
						System.out.println("Tribe " + npc.getTribe() + " has different types: " + check1 + " and " + check2);
					}
				}
				else {
					CreatureType validType = CreatureType.getCreatureType(sniffedNpcType);
					invalidTypes.put(npc.getTribe(), validType);
					System.out.println("Tribe " + npc.getTribe() + " should be of type " + validType);
					System.out.println("\tNpcId: " + npc.getNpcId() + "; Type: " + npc.getObjectTemplate().getNpcTemplateType() + ", AbyssType: " + npc.getObjectTemplate().getAbyssNpcType());
				}
			}
		}
	}

}
