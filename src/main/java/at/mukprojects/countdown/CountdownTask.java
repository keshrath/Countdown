package at.mukprojects.countdown;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the countdown task. It extends the Java TimerTask and
 * manages the CountdownTime.
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
public class CountdownTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(CountdownTask.class);
    
    private CountdownTime timer;
    private int delay;

    /**
     * Constructs a new CountdownTask form an CountdownTime object and a
     * specific delay.
     * 
     * @param timer
     *            The CountdownTime object.
     * @param delay
     *            The delay as an int value in milliseconds.
     */
    public CountdownTask(CountdownTime timer, int delay) {
	this.timer = timer;
	this.delay = delay;
    }

    @Override
    public void run() {
	timer.getAndAdd(delay * -1);
    }
}
