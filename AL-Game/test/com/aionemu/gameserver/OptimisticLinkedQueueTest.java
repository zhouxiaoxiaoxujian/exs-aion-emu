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

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.utils.collections.OptimisticLinkedQueue;

/**
 * @author Rolandas
 */
public class OptimisticLinkedQueueTest {

	static OptimisticLinkedQueue<Integer> queue = new OptimisticLinkedQueue<Integer>();
	// ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
	static AtomicInteger totalAdded = new AtomicInteger();

	@Test
	public void test() {

		Thread[] threads = new Thread[1000];
		for (int i = 0; i < 1000; i++) {
			threads[i] = new Thread() {

				@Override
				public void run() {
					if (Rnd.get(100) < 50) {
						int addCount = Rnd.get(200);
						int addedCount = 0;
						for (int i = 0; i < addCount; i++) {
							Integer val = Integer.valueOf(Rnd.get(100));
							synchronized (queue) {
								if (queue.offer(val))
									addedCount++;
							}
						}
						totalAdded.addAndGet(addedCount);
						assertTrue(addCount == addedCount);
					}
					else {
						int removeCount = Rnd.get(200);
						int removed = 0;
						for (int i = 0; i < removeCount; i++) {
							synchronized (queue) {
								if (queue.poll() == null)
									continue;
								removed++;
							}
						}
						totalAdded.addAndGet(-removed);
					}
				}
			};
			threads[i].start();
		}

		for (int i = 0; i < 100; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
			}
		}

		queue.leaveTail();
		assertTrue(queue.size() == 1);

		queue.clear();
		assertTrue(queue.size() == 0);
	}

}
