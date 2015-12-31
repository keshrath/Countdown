package at.mukprojects.countdown;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.countdown.client.SntpClient;

/**
 * This class represents the countdown timer. The timer can be used to start and
 * stop different kinds of countdowns.
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
public class CountdownTimer {

    private static final Logger logger = LoggerFactory.getLogger(CountdownTimer.class);

    /**
     * Date format.
     */
    private static final DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss:SS z");

    private Timer timer;

    /**
     * Starts the countdown.
     * 
     * @param time
     *            The countdown time in milliseconds.
     * @param delay
     *            The delay of the timer.
     * @return The CountdownTime object, which is used to store the time.
     */
    public CountdownTime start(long time, int delay) {
	stop();

	logger.info("Starting a new timer with the time of " + time + " milliseconds and a delay of " + delay
		+ " milliseconds...");

	timer = new Timer();

	CountdownTime countdown = new CountdownTime(time, CountdownTime.MODE_TIMER);
	CountdownTask task = new CountdownTask(countdown, delay);

	timer.schedule(task, 0, delay);

	logger.info("Timer is running...");

	return countdown;
    }

    /**
     * Starts the countdown.
     * 
     * @param date
     *            The date on which the countdown should end.
     * @param delay
     *            The delay of the timer.
     * @return The CountdownTime object, which is used to store the time.
     */
    public CountdownTime start(Date date, int delay) {
	stop();

	logger.info("Starting a new timer with the date " + formatter.format(date) + " and a delay of " + delay
		+ " milliseconds...");

	timer = new Timer();

	long time;
	int mode;

	try {
	    time = SntpClient.getTime();
	    mode = CountdownTime.MODE_ATOMIC_TIME;
	} catch (IOException e) {
	    logger.error("An Exception occured during the server request! (" + e
		    + ") The timer uses the system time instead.", e);
	    time = System.currentTimeMillis();
	    mode = CountdownTime.MODE_SYSTEM_TIME;
	}

	long diff = date.getTime() - time;

	/*
	 * Display countdown time.
	 */
	long days = TimeUnit.MILLISECONDS.toDays(diff);
	long hours = TimeUnit.MILLISECONDS.toHours(diff) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diff));
	long minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
		- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff));
	long seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
		- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff));
	long milliseconds = diff - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(diff));

	logger.debug("The countdown starts with " + days + " days and a time of " + hours + ":" + minutes + ":"
		+ seconds + ":" + milliseconds + " on the clock.");

	CountdownTime countdown = new CountdownTime(diff, mode);
	CountdownTask task = new CountdownTask(countdown, delay);

	timer.schedule(task, 0, delay);

	logger.info("Timer is running...");

	return countdown;
    }

    public void stop() {
	if (timer != null) {
	    logger.info("Stoping the current timer...");
	    timer.cancel();
	    logger.info("The current timer has been stoped.");
	}
    }
}
