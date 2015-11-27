package mc.implement;

import mc.Packet;

public class FilePacket extends Packet {

	@Override
	public byte[] data() {
		return this.source();
	}

	@Override
	public Packet data(byte[] ary) {
		this.source = ary;
		return this;
	}
}
