package net.sf.cindy.decoder;

import net.sf.cindy.Packet;
import net.sf.cindy.PacketDecoder;
import net.sf.cindy.Session;

public class IODecryptDecoder implements PacketDecoder {
	public IODecryptDecoder() {
	}
	
	@Override
	public Object decode(Session session, Packet packet) throws Exception {
		return packet;
	}

}
