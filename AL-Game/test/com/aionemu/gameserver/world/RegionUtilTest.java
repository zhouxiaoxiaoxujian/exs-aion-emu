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
package com.aionemu.gameserver.world;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.aionemu.gameserver.configs.main.WorldConfig;

/**
 * @author ATracer
 */
public class RegionUtilTest {

	@Before
	public void setup() {
		WorldConfig.WORLD_REGION_SIZE = 500;
	}

	@Test
	public void testGenerateReverse() {
		testRegionReverse(100, 100, 0f, 0f);
		testRegionReverse(600f, 600f, 500f, 500f);
		testRegionReverse(1000f, 1000f, 1000f, 1000f);
	}

	private void testRegionReverse(float x, float y, float expectedX, float expectedY) {
		int regionId = RegionUtil.get2dRegionId(x, y);
		float reversedX = RegionUtil.getXFrom2dRegionId(regionId);
		float reversedY = RegionUtil.getYFrom2dRegionId(regionId);
		Assert.assertEquals(expectedX, reversedX);
		Assert.assertEquals(expectedY, reversedY);
	}
}
