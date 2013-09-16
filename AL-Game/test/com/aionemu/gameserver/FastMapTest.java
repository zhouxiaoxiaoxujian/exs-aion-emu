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

import javolution.util.FastMap;
import javolution.util.FastMap.Entry;
import junit.framework.Assert;

import org.junit.Test;

/**
 * @author VladimirZ
 */
public class FastMapTest {

	@Test
	public void removeTestShared() {
		FastMap<Integer, String> map = new FastMap<Integer, String>().shared();
		map.put(1, "test1");
		map.put(2, "test2");
		map.put(3, "test3");
		map.put(4, "test4");
		map.put(5, "test5");
		map.remove(5);
		Entry<Integer, String> entry = map.tail();

		// check for memory leak in shared fastmap
		Assert.assertEquals(entry.getNext().getPrevious().getValue(), null);
	}

	@Test
	public void removeTest() {
		FastMap<Integer, String> map = new FastMap<Integer, String>();
		map.put(1, "test1");
		map.put(2, "test2");
		map.put(3, "test3");
		map.put(4, "test4");
		map.put(5, "test5");
		map.remove(5);
		Entry<Integer, String> entry = map.tail();

		// check for memory leak in non-shared fastmap
		Assert.assertEquals(entry.getNext().getPrevious().getValue(), null);
	}
}
