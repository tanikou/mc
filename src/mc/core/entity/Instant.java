package mc.core.entity;

/**
 * 即时信息的状态值
 * 
 * @author VicTan@qq.com
 *
 */
public enum Instant {
	/** 正常状态 */
	Normal("正常空闲中"),
	/** 繁忙状态 */
	Busy("繁忙"),
	/** 锁定中 */
	Locked("锁定中"),
	/** 离线 */
	Offline("离线"),
	/** 下班中 */
	OffDuty("已下班");

	private String code;

	private Instant(String code) {
		this.code = code;
	}

	public String value() {
		return code;
	}
}
