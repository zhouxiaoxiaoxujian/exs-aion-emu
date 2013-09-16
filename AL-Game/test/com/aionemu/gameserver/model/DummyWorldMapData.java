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

import java.util.ArrayList;
import com.aionemu.gameserver.dataholders.WorldMapsData;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;

/**
 * @author Rolandas
 */
public class DummyWorldMapData extends WorldMapsData {

	public static final int DEFAULT_MAP = 10010000;

	public DummyWorldMapData() {
		super.worldMaps = new ArrayList<WorldMapTemplate>();
		worldMaps.add(new DummyWorldTemplate(DummyZoneData.DEFAULT_SIZE));
		super.afterUnmarshal(null, null);
	}

	public static class DummyWorldTemplate extends WorldMapTemplate {

		public DummyWorldTemplate(int size) {
			super.mapId = DEFAULT_MAP;
			super.worldSize = size;
		}
	}
}
