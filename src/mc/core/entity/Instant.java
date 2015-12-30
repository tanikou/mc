package mc.core.entity;

/**
 * 即时信息的状态值
 * 
 * @author VicTan@qq.com
 *
 */
public enum Instant {
	/**
	 * 正常空闲中
	 */
	Normal("正常空闲中"),

	/**
	 * 插卡中
	 */
	Card("插卡中"),

	/**
	 * 加油中
	 */
	Fuel("加油中"),
	/**
	 * 锁定中
	 */
	Locked("锁定中"),
	/**
	 * 离线
	 */
	Offline("离线"),

	/**
	 * 下班中
	 */
	OffDuty("下班中");

	private String code;

	private Instant(String code) {
		this.code = code;
	}

	public String value() {
		return code;
	}
}
