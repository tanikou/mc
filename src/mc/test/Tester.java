package mc.test;

import mc.Packet;
import mc.client.AsClient;
import mc.impl.file.FilePacket;
import mc.impl.file.FileRequest;

public class Tester {
	public static void main(String[] args) throws Exception {
		doUpdateFile();
	}

	public static void doUpdateFile() throws Exception {
		Packet packet = new FilePacket().data(new byte[] { 0x01, 0x02 });
		new AsClient("127.0.0.1", 1202).send(packet).run();
	}

	public static void bind() {
		byte[] ary = new byte[] { 0x01, 0x02, 0x03, 0x04 };
		FileRequest req = new FileRequest(new FilePacket().data(ary));
		req.autobind();
		System.out.println(req.preview());
	}
}
