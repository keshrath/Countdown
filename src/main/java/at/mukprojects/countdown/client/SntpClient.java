package at.mukprojects.countdown.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an SNTP client, which uses an NTP message to
 * communicate with the server. The local clock offset calculation is
 * implemented according to the SNTP algorithm specified in RFC 2030.
 * 
 * The code is based on the Java implementation of an SNTP client copyrighted
 * under the terms of the GPL by Adam Buckley in 2004.
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
public class SntpClient {

    /**
     * UDP port
     */
    private static final int PORT = 123;

    /**
     * NTP server
     */
    private static final String[] SERVERNAMES = new String[] { "us.pool.ntp.org", "de.pool.ntp.org", "at.pool.ntp.org",
	    "uk.pool.ntp.org", "au.pool.ntp.org" };

    /**
     * Socket timeout in milliseconds
     */
    private static final int TIMEOUT = 10000;

    private static final Logger logger = LoggerFactory.getLogger(SntpClient.class);

    /**
     * Returns the local time corrected by the received server time.
     * 
     * @return The corrected local time.
     * @throws IOException
     */
    public static long getTime() throws IOException {

	final DatagramSocket socket = new DatagramSocket();
	final byte[] buffer = new NtpMessage().toByteArray();

	logger.info("Timeout is set to " + TIMEOUT + " milliseconds.");
	socket.setSoTimeout(TIMEOUT);

	DatagramPacket packet = null;
	InetAddress address = null;

	address = InetAddress.getByName(SERVERNAMES[0]);
	logger.info("Trying to connect to NTP server: " + String.format("%s", address));
	packet = sendRequest(buffer, address, socket);

	boolean receivingData = true;
	int tries = 1;

	while (receivingData) {
	    try {
		packet = receiveResponse(buffer, socket);
		receivingData = false;
	    } catch (SocketTimeoutException e) {
		logger.warn("Timeout reached! Try: " + tries);

		address = InetAddress.getByName(SERVERNAMES[tries]);
		logger.info("Trying to connect to NTP server: " + String.format("%s", address));
		packet = sendRequest(buffer, address, socket);

		tries++;
		if (tries > 5) {
		    logger.error("Timeout reached! Server is unreachable.", e);
		    throw new IOException("Server unreachable!", e);
		}
	    }
	}

	logger.info("Recording the incoming timestamp...");
	final double destinationTimestamp = NtpMessage.now();

	logger.info("Retrieve the the NTP message.");
	final NtpMessage msg = new NtpMessage(packet.getData());

	socket.close();

	/*
	 * Formula for delay according to the RFC2030 errata.
	 */
	final double roundTripDelay = (destinationTimestamp - msg.originateTimestamp)
		- (msg.transmitTimestamp - msg.receiveTimestamp);

	/*
	 * The amount the server is ahead of the client.
	 */
	final double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp)
		+ (msg.transmitTimestamp - destinationTimestamp)) / 2;

	/*
	 * Display response.
	 */
	logger.debug("NTP server: " + String.format("%s", address));
	logger.debug("Round-trip delay: " + String.format("%+9.2f ms", 1000 * roundTripDelay));
	logger.debug("Local clock offset: " + String.format("%+9.2f ms", 1000 * localClockOffset));

	final long now = System.currentTimeMillis();
	final long cor = now + Math.round(1000.0 * localClockOffset);

	DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss:SS z");

	logger.info("Local time: " + formatter.format(new Date(now)));
	logger.info("Corrected time:" + formatter.format(new Date(cor)));

	return cor;
    }

    private static DatagramPacket sendRequest(final byte[] buffer, final InetAddress address,
	    final DatagramSocket socket) throws IOException {
	logger.info("Send server request...");
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
	socket.send(packet);
	logger.info("Server request was send.");
	return packet;
    }

    private static DatagramPacket receiveResponse(byte[] buffer, DatagramSocket socket) throws IOException {
	logger.info("Receive server response...");
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	socket.receive(packet);
	logger.info("Received server response.");
	return packet;
    }
}