package mc.impl.file;

import mc.Packet;
import mc.Request;
import mc.annotation.Bind;
import mc.annotation.Preview;
import mc.annotation.Type;

public class FileRequest extends Request {
	private static final long serialVersionUID = 1L;

	public FileRequest(Packet packet) {
		super(packet);
	}

	@Preview(name = "属性ID号", order = 22)
	@Bind(begin = 0, length = 2, type = Type.HexString)
	public String id;

	@Preview(name = "属性长度", order = 33)
	@Bind(begin = 2, length = 2, type = Type.HexString)
	public String len;
}
