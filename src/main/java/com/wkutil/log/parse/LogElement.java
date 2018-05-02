package com.wkutil.log.parse;

public class LogElement {
	private String cmd;
	private String dateTime;
	private String version;
	private String exceptionMsg;
	private String logLevel;


	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public String toString() {
		return cmd + "\t" + dateTime + "\t" + version + "\t" + logLevel + "\t" + exceptionMsg;
	}
}
