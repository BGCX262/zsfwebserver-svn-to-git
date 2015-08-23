package com.ttt.instance;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import net.sf.cindy.filter.PackageFlow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.util.DataFactory;

public class Decompose {
	private static final Log log = LogFactory.getLog(Decompose.class);
	// message queue
	private static final int lengthOffset = DataFactory.lengthOffset;
	private ConcurrentLinkedQueue<byte[]> messages;
	private ReentrantReadWriteLock lock;
	private PackageFlow packageFlow;
	private WriteLock writeLock;
	private boolean cut = false;
	private byte[] cutMessage;
	private int rearLength = 0;

	/**
	 * constructor of Decompose package. The decompose result saved at this
	 * list,order by add time;
	 * 
	 * @param list
	 */
	public Decompose(ConcurrentLinkedQueue<byte[]> list) {
		this.messages = list;
		lock = new ReentrantReadWriteLock();
		writeLock = lock.writeLock();
		packageFlow = new PackageFlow();
	}

	/**
	 * Add a message to decompose, you can get result to list which define this
	 * class constructor parameter. It is multi-thread safety .
	 * 
	 * @param gram
	 */
	public boolean addMessage(byte[] gram) {
		try {
			writeLock.lock();
			if (gram == null) {
				return true;
			}
			if (cutMessage != null && cutMessage.length < DataFactory.lengthOffset + 2) {
				gram = DataFactory.addByteArray(cutMessage, gram);
				cut = false;
				cutMessage = null;
				rearLength = 0;
			}
			gram = notCut(gram);
			gram = haveCut(gram);
		} catch (Exception e) {
			log.error(e, e);
			return false;
		} finally {
			writeLock.unlock();
		}
		return true;
	}

	private byte[] notCut(byte[] gram) throws IllegalStateException {
		while (!cut) {
			if (gram == null) {
				return null;
			} else if (gram.length < DataFactory.lengthOffset + 2) {
				cutMessage = gram;
				rearLength = 0;
				return gram;
			}
			// Haven't happen cut
			byte[] temp = aDatagram(gram);
			if (temp != null && temp.length == 0) {
				throw new IllegalStateException(" Package's length = 0,if it's not catched away,then process can't stop.");
			}
			if (temp == null) {
				// happen cut
				cut = true;
				cutMessage = gram;
				rearLength = cutFront(gram);
				return null;
			} else {
				// integrity message
				gram = DataFactory.get(gram, temp.length, gram.length - temp.length);
				messages.add(temp);
			}
		}
		return gram;
	}

	private byte[] haveCut(byte[] gram) throws IllegalStateException {
		if (gram == null) {
			return null;
		}
		while (cut) {
			if (gram == null) {
				return null;
			}
			// cut rear
			cutMessage = DataFactory.addByteArray(cutMessage, DataFactory.get(gram, 0, rearLength));
			if (rearLength <= gram.length) {
				// next contain enough rear
				messages.add(cutMessage);
				cutMessage = null;
				cut = false;
				gram = DataFactory.get(gram, rearLength, gram.length - rearLength);
				rearLength = 0;
				gram = notCut(gram);
			} else {
				// next contain a part of rear
				rearLength -= gram.length;
				return null;
			}
		}
		return gram;
	}

	/**
	 * @param message
	 * @return null is have cut datagram
	 */
	private byte[] aDatagram(byte[] message) {
		int length = DataFactory.readLength(lengthOffset, message);
		checkLengthReliance(length);
		if (length > message.length) {
			return null;
		}
		return DataFactory.get(message, 0, length);
	}

	private void checkLengthReliance(int length) throws IllegalStateException {
		if (!packageFlow.isReliance(length)) {
			throw new IllegalStateException("Package flow control disconnect " + length);
		}
	}

	/**
	 * deal with cut datagram front
	 * 
	 * @param message
	 * @return cut datagram rear length
	 * @throws NotCutDatagramException
	 */
	private int cutFront(final byte[] message) throws IllegalStateException {
		int length = DataFactory.readLength(lengthOffset, message);
		checkLengthReliance(length);
		if (length > message.length)
			return length - message.length;
		else {
			throw new IllegalStateException("NotCutDatagramException");
		}
	}

	public ConcurrentLinkedQueue<byte[]> getMessages() {
		return messages;
	}

	public void setMessages(ConcurrentLinkedQueue<byte[]> messages) {
		this.messages = messages;
	}

	public ReentrantReadWriteLock getLock() {
		return lock;
	}

	public void setLock(ReentrantReadWriteLock lock) {
		this.lock = lock;
	}

	public PackageFlow getPackageFlow() {
		return packageFlow;
	}

	public void setPackageFlow(PackageFlow packageFlow) {
		this.packageFlow = packageFlow;
	}

	public WriteLock getWriteLock() {
		return writeLock;
	}

	public void setWriteLock(WriteLock writeLock) {
		this.writeLock = writeLock;
	}

	public boolean isCut() {
		return cut;
	}

	public void setCut(boolean cut) {
		this.cut = cut;
	}

	public byte[] getCutMessage() {
		return cutMessage;
	}

	public void setCutMessage(byte[] cutMessage) {
		this.cutMessage = cutMessage;
	}

	public int getRearLength() {
		return rearLength;
	}

	public void setRearLength(int rearLength) {
		this.rearLength = rearLength;
	}

	public static int getLengthOffset() {
		return lengthOffset;
	}

}
