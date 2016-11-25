package ru.simplgroupp.ejb;

import java.util.List;

import org.activiti.engine.ProcessEngineConfiguration;

public class ProcessConfiguration {
	private String deployURL;
	private ProcessEngineConfiguration engineConfiguration;
	private List<String> processConditions;
	public String getDeployURL() {
		return deployURL;
	}
	public void setDeployURL(String deployURL) {
		this.deployURL = deployURL;
	}
	public ProcessEngineConfiguration getEngineConfiguration() {
		return engineConfiguration;
	}
	public void setEngineConfiguration(
			ProcessEngineConfiguration engineConfiguration) {
		this.engineConfiguration = engineConfiguration;
	}
	public List<String> getProcessConditions() {
		return processConditions;
	}
	public void setProcessConditions(List<String> processConditions) {
		this.processConditions = processConditions;
	}
}
