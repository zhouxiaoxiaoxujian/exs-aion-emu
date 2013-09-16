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
package com.aionemu.gameserver;

import org.junit.Test;

import com.aionemu.gameserver.dataholders.NpcDropData;
import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropGroup;
import com.aionemu.gameserver.model.drop.NpcDrop;


/**
 * @author MrPoke
 *
 */
public class DropTest {

	@Test
	public void test() {
		NpcDropData dropData = NpcDropData.load();
		for (NpcDrop npcDrop : dropData.getNpcDrop()){
			if (npcDrop.getNpcId() == 210186){
				for (DropGroup dropGroup : npcDrop.getDropGroup()){
					System.out.println("DropGroup"+dropGroup.getGroupName()+", "+ dropGroup.getRace());
					for (Drop drop : dropGroup.getDrop()){
						System.out.println(drop.toString());
					}
				}
			}
		}
	}

}
