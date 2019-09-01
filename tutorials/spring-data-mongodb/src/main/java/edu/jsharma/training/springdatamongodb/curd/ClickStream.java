package edu.jsharma.training.springdatamongodb.curd;

import java.util.List;

public class ClickStream {
	public ClickStream() {

	}

	@com.fasterxml.jackson.annotation.JsonProperty("Id")
	private String Id;
	@com.fasterxml.jackson.annotation.JsonProperty("Name")
	private String Name;
	@com.fasterxml.jackson.annotation.JsonProperty("Clasdifier")
	private List<String> Clasdifier;
	@com.fasterxml.jackson.annotation.JsonProperty("Timeout")
	private String Timeout;
	@com.fasterxml.jackson.annotation.JsonProperty("Url")
	private String Url;
	@com.fasterxml.jackson.annotation.JsonProperty("Enabled")
	private String Enabled;
	@com.fasterxml.jackson.annotation.JsonProperty("Class")
	private String category;
	
	@com.fasterxml.jackson.annotation.JsonProperty("Monitorhostname")
	private String Monitorhostname;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<String> getClasdifier() {
		return Clasdifier;
	}

	public void setClasdifier(List<String> clasdifier) {
		Clasdifier = clasdifier;
	}

	public String getTimeout() {
		return Timeout;
	}

	public void setTimeout(String timeout) {
		Timeout = timeout;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getEnabled() {
		return Enabled;
	}

	public void setEnabled(String enabled) {
		Enabled = enabled;
	}

	public String getMonitorhostname() {
		return Monitorhostname;
	}

	public void setMonitorhostname(String monitorhostname) {
		Monitorhostname = monitorhostname;
	}

}
