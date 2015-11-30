package mc.test;

import mc.impl.file.FilePacket;
import mc.impl.file.FileRequest;

public class Tester {
	public static void main(String[] args) throws Exception {
		byte[] ary = new byte[] { 0x01, 0x02, 0x03, 0x04 };
		FileRequest req = new FileRequest(new FilePacket().data(ary));
		req.autobind();
		System.out.println(req.preview());
	}
}
