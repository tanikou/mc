package mc.test;

import mc.util.AsClient;

public class Tester {
	public static void main(String[] args) throws Exception {
		doUpdateFile();
	}

	public static void doUpdateFile() throws Exception {
		new AsClient("127.0.0.1", 1202).send(new byte[] { 0x01, 0x02 }).call();
	}
}
