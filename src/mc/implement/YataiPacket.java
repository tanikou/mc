package mc.implement;

import mc.Packet;
import mc.util.Data;

public class YataiPacket implements Packet {
	private byte[] source;

	@Override
	public String preview() {
		return Data.hex(source);
	}

	@Override
	public byte[] source() {
		return source.clone();
	}

	@Override
	public Packet source(byte[] ary) {
		this.source = ary;
		return this;
	}

}
