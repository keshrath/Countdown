# Countdown

Countdown is a Java library to make it easy to create countdowns. 

There are two different options to create a countdown. The first one is to set a certain time and delay,
both as values in milliseconds. The second option is to set a date on which the countdown should end. In
this case the program will send a request to an NTP server to calculate the exact time.

To communicate with the NTP server the NtpMessage class is used. This class was written by Adam Buckley.
If the request fails, for any reason the program will try different NTP servers. In case the program can't
reach any NTP server, for example, cause there is no internet connection, it will calculate the timer with
the current system time.

#### Usage examples

```java
CountdownTimer timerTask = new CountdownTimer();

CountdownTime countdown = timerTask.start(5000, 10);

boolean working = true;

while (working) {
	if (countdown.get() <= 0) {
		timerTask.stop();
	} else {
		System.out.println("Timer: " + countdown.get());
	}
}
```

```java
CountdownTimer timerTask = new CountdownTimer();

Calendar cal = Calendar.getInstance();  
cal.add(Calendar.DATE, 1);
Date dt = cal.getTime();

CountdownTime countdown = timerTask.start(dt, 10);

boolean working = true;

while (working) {
	if (countdown.get() <= 0) {
		timerTask.stop();
	} else {
		System.out.println("Timer: " + countdown.get());
	}
}
```