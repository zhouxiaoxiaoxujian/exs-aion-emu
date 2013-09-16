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

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.collection.LambdaCollections.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.lambdaj.function.convert.Converter;
import ch.lambdaj.group.Group;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.handler.CreatureEventHandler;
import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.ThreadConfig;
import com.aionemu.gameserver.controllers.*;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.dataholders.*;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.DuelService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.taskmanager.tasks.MoveTaskManager;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
import com.aionemu.gameserver.taskmanager.tasks.PlayerMoveTaskManager;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.*;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

/**
 * @author Rolandas
 */
public class TribeRelationCheck {

	static Player asmo;
	static Player ely;
	static World world;
	static WorldPosition asmoPosition;
	static WorldPosition elyPosition;
	static int npcCount = 0;
	static volatile boolean attacked = false;
	static volatile long time;
	static final Semaphore lock = new Semaphore(1);

	@BeforeClass
	public static void init() throws Exception {
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
		DataManager.ZONE_DATA = new DummyZoneData();
		DataManager.WORLD_MAPS_DATA = new DummyWorldMapData();

		Config.load();
		// AIConfig.ONCREATE_DEBUG = true;
		AIConfig.EVENT_DEBUG = true;
		ThreadConfig.THREAD_POOL_SIZE = 20;
		ThreadPoolManager.getInstance();

		AI2Engine.getInstance().load(null);

		/**
		 * Comment out these lines in DAOManager.registerDAO() if not using DB: <tt> 
		 * if (!dao.supports(getDatabaseName(),
		 * 		getDatabaseMajorVersion(), getDatabaseMinorVersion())) { return; } 
		 * </tt>
		 */
		DAOManager.init();

		world = World.getInstance();
		asmo = DummyPlayer.createAsmodian();
		asmoPosition = World.getInstance().createPosition(DummyWorldMapData.DEFAULT_MAP, 100f, 100f, 0f, (byte) 0, 0);

		MapRegion asmoRegion = asmoPosition.getWorldMapInstance().getRegion(100f, 100f, 0);
		asmoRegion.getObjects().put(asmo.getObjectId(), asmo);
		asmoRegion.activate();
		asmo.setPosition(asmoPosition);

		ely = DummyPlayer.createElyo();
		elyPosition = World.getInstance().createPosition(DummyWorldMapData.DEFAULT_MAP, 200f, 200f, 0f, (byte) 0, 0);
		MapRegion elyRegion = elyPosition.getWorldMapInstance().getRegion(200f, 200f, 0);
		elyRegion.getObjects().put(ely.getObjectId(), ely);
		elyRegion.activate();
		ely.setPosition(elyPosition);

		PacketBroadcaster.getInstance();
		DuelService.getInstance();
		PlayerMoveTaskManager.getInstance();
		MoveTaskManager.getInstance();
	}

	static void setPositionAsSpawned(WorldPosition position) {
		try {
			Method method = WorldPosition.class.getDeclaredMethod("setIsSpawned", new Class<?>[] { boolean.class });
			method.setAccessible(true);
			method.invoke(position, new Object[] { true });
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	void checkAttack(final Npc npc, final Player player) {
		final Semaphore mainLock = lock;
		try {
			mainLock.acquire();
		}
		catch (InterruptedException e1) {
		}

		time = System.currentTimeMillis();
		npc.getObserveController().clear();
		npc.getObserveController().addObserver(new ActionObserver(ObserverType.ATTACK) {

			@Override
			public void attack(Creature creature) {
				System.out.println("\t" + npc.getObjectTemplate().getName() + " attacked " + player.getCommonData().getName()
					+ " - " + (System.currentTimeMillis() - time) + " ms");
				attacked = true;
			}
		});
		ThreadPoolManager.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				((NpcAI2) npc.getAi2()).setStateIfNot(AIState.IDLE);
				setPositionAsSpawned(player.getPosition());
				npc.setPosition(player.getPosition());

				player.unsetState(CreatureState.DEAD);
				player.getLifeStats().setCurrentHpPercent(100);

				npc.getKnownList().doUpdate();
				CreatureEventHandler.onCreatureSee((NpcAI2) npc.getAi2(), player);
			}
		});
	}

	void waitAttackResult(Player player) {
		final Semaphore mainLock = lock;
		try {
			int retries = 60; // Default attack delay is 500 milliseconds
			while (retries > 0 && !mainLock.tryAcquire(10, TimeUnit.MILLISECONDS)) {
				retries--;
				Thread.sleep(10);
			}
		}
		catch (InterruptedException e) {
		}
		finally {
			player.clearAttackedCount();
			mainLock.release();
			if (!attacked)
				System.out.println("\tCreature didn't attack " + player.getCommonData().getName());
		}
	}

	@Test
	public void test() {

		Group<NpcTemplate> npcsByTribe = group(DataManager.NPC_DATA.getNpcData().valueCollection(),
			by(on(NpcTemplate.class).getTribe()));
		for (String npcGroup : npcsByTribe.keySet()) {
			if (StringUtils.isEmpty(npcGroup) || npcGroup.equals(TribeClass.NONE.toString()))
				continue;

			final List<Npc> npcs = with(npcsByTribe.find(npcGroup)).convert(new Converter<NpcTemplate, Npc>() {

				@Override
				public Npc convert(NpcTemplate from) {
					SpawnTemplate spawn = SpawnEngine.addNewSpawn(DummyWorldMapData.DEFAULT_MAP, from.getTemplateId(), 0f, 0f,
						0f, (byte) 0, 0);
					npcCount++;
					Npc npc = new Npc(1000000 + npcCount, new NpcController(), spawn, from);
					return npc;
				}
			});

			System.out.println("Testing tribe: " + npcGroup);
			checkTribe(npcs);
		}
	}

	void checkTribe(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc.getName().indexOf('_') != -1) // NC test npcs
				continue;

			if (!(npc.getAi2() instanceof NpcAI2))
				continue;

			// if (npc.getNpcId() != 282796) continue;

			NpcAI2 ai = (NpcAI2) npc.getAi2();
			if (!"aggressive".equals(ai.getName()))
				continue;

			System.out.println("  Testing NPC: " + npc.getName() + " (" + npc.getNpcId() + "); ai=" + ai.getName());

			npc.setEffectController(new EffectController(npc));
			npc.setKnownlist(new PlayerAwareKnownList(npc));

			attacked = false;
			npc.setPosition(asmoPosition);
			System.out.println("\tNpc is enemy to asmo: " + asmo.isEnemy(npc));
			System.out.println("\tAsmo is enemy to npc: " + npc.isEnemy(asmo));

			if (npc.isEnemy(asmo)) {
				checkAttack(npc, asmo);
				waitAttackResult(asmo);
			}

			attacked = false;
			npc.setPosition(elyPosition);
			System.out.println("\tNpc is enemy to ely: " + ely.isEnemy(npc));
			System.out.println("\tEly is enemy to npc: " + npc.isEnemy(ely));

			if (npc.isEnemy(ely)) {
				checkAttack(npc, ely);
				waitAttackResult(ely);
			}

			npc.getController().delete();
		}
		npcs.clear();
		try {
			Thread.sleep(10);
		}
		catch (InterruptedException e) {
		}
	}

}
