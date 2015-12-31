package at.mukprojects.countdown;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the countdown time object, it holds the time of the
 * countdown. It's implemented to be thread safe.
 * 
 * This code is copyright (c) Mathias Markl 2015
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Mathias Markl
 */
public class CountdownTime {

    /**
     * Mode is set if the CountdownTime object was created with a timer.
     */
    public static final int MODE_TIMER = 1;

    /**
     * Mode is set if the CountdownTime object was created with the system time.
     */
    public static final int MODE_SYSTEM_TIME = 2;

    /**
     * Mode is set if the CountdownTime object was created with an atomic time.
     */
    public static final int MODE_ATOMIC_TIME = 3;

    private static final Logger logger = LoggerFactory.getLogger(CountdownTime.class);

    private AtomicLong timer;
    private int mode;

    /**
     * Constructs a new CountdownTime from an given long value.
     * 
     * @param timer
     *            The time as a long value.
     * @param mode
     *            The creation mode.
     */
    public CountdownTime(long timer, int mode) {
	this.timer = new AtomicLong(timer);
	this.mode = mode;
    }

    /**
     * Atomically adds the given value to the current value.
     * 
     * @param add
     *            The value to add.
     * @return The updated value.
     */
    public long addAndGet(int add) {
	return timer.addAndGet(add);
    }

    /**
     * Atomically adds the given value to the current value.
     * 
     * @param add
     *            The value to add.
     * @return The updated value.
     */
    public long getAndAdd(int add) {
	return timer.getAndAdd(add);
    }

    /**
     * Sets time to the given value.
     * 
     * @param add
     *            The new value.
     */
    public void set(long timer) {
	this.timer.set(timer);
    }

    /**
     * Gets the current value.
     * 
     * @return The current value.
     */
    public long get() {
	return timer.get();
    }

    /**
     * Return the creation mode.
     * 
     * @return The mode.
     */
    public int getMode() {
	return mode;
    }
}