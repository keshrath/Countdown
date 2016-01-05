package at.mukprojects.countdown;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountdownTest {

    private static final Logger logger = LoggerFactory.getLogger(CountdownTest.class);

    private CountdownTimer timerTask;

    @Before
    public void setUp() {
	timerTask = new CountdownTimer();
    }

    @Test
    public void testCountdownWithLong() {
	logger.info("Test (TestCountdownWithLong) is starting...");

	CountdownTime countdown = timerTask.start(5000, 10);

	long takeTimeBefore = 0;
	long takeTimeAfter = 0;

	boolean working = true;
	boolean firstGet = true;

	while (working) {
	    if (countdown.get() <= 0) {
		takeTimeAfter = System.currentTimeMillis();
		logger.debug("Last count: " + countdown.get());
		working = false;
	    } else if (firstGet && countdown.get() > 0) {
		takeTimeBefore = System.currentTimeMillis();
		logger.debug("First count: " + countdown.get());
		firstGet = false;
	    }
	}

	long diff = takeTimeAfter - takeTimeBefore;

	logger.debug("TakeTimeBefore: " + takeTimeBefore + ", TakeTimeAfter:" + takeTimeAfter + ", Difference:" + diff);
	assertTrue(4500 < diff && diff < 5500);

	logger.info("Test (TestCountdownWithLong) has finished.");
    }

    @Test
    public void testCountdownWithDate() {
	logger.info("Test (testCountdownWithDate) is starting...");

	Date date = new Date(System.currentTimeMillis() + 50005);

	CountdownTime countdown = timerTask.start(date, 10);

	long takeTimeBefore = 0;
	long takeTimeAfter = 0;
	long timer = 0;

	boolean working = true;
	boolean firstGet = true;

	while (working) {
	    if (countdown.get() <= 0) {
		takeTimeAfter = System.currentTimeMillis();
		logger.debug("Last count: " + countdown.get());
		working = false;
	    } else if (firstGet && countdown.get() > 0) {
		takeTimeBefore = System.currentTimeMillis();
		logger.debug("First count: " + countdown.get());
		timer = countdown.get();
		firstGet = false;
	    }
	}

	long diff = takeTimeAfter - takeTimeBefore;

	logger.debug("TakeTimeBefore: " + takeTimeBefore + ", TakeTimeAfter:" + takeTimeAfter + ", Difference:" + diff
		+ ", Timer:" + timer);
	assertTrue((timer - 2500) < diff && diff < (timer + 2500));

	logger.info("Test (testCountdownWithDate) has finished.");
    }

    @Test
    public void testCountdownTimeParser() {
	logger.info("Test (testCountdownTimeParser) is starting...");
	
	assertTrue(timerTask.parseInteger("1000") == 1000);
	assertTrue(timerTask.parseInteger("text") == null);
	assertTrue(timerTask.parseInteger("1000.05") == 1000);
	
	logger.info("Test (testCountdownTimeParser) has finished.");
    }
    
    @Test
    public void testCountdownDateParser() {
	logger.info("Test (testCountdownDateParser) is starting...");
	
	assertEquals(timerTask.parseDate("10.10.2015").toString(), "Sat Oct 10 00:00:00 CEST 2015");
	assertTrue(timerTask.parseDate("text") == null);
	assertEquals(timerTask.parseDate("10/10/2015").toString(), "Sat Oct 10 00:00:00 CEST 2015");
	
	logger.info("Test (testCountdownDateParser) has finished.");
    }
    
    @After
    public void tearDown() {
	timerTask.stop();
    }

}
