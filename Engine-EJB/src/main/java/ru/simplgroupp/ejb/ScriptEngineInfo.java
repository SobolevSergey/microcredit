package ru.simplgroupp.ejb;

import javax.script.ScriptEngine;

public class ScriptEngineInfo {

	protected ScriptEngine scriptEngine;
	protected String jsPreface;
	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}
	public String getJsPreface() {
		return jsPreface;
	}
}
