package com.intel.mtwilson.trustagent.model;

public class IMAvalue {
	private String pcr;
	private String template_hash;
	private String format;
	private String filedata_hash;
	private String filename_hint;

	public String getPcr() {
		return pcr;
	}
	public String getTemplateHash() {
		return template_hash;
	}
	public String getFormat() {
		return format;
	}
	public String getFildedataHash() {
		return filedata_hash;
	}
	public String getFilenameHint() {
		return filename_hint;
	}
	public void setPcr(String pcr) {
		this.pcr=pcr;
	}
	public void setTemplateHash(String template_hash) {
		this.template_hash=template_hash;
	}
	public void setFormat(String format) {
		this.format=format;
	}
	public void setFiledataHash (String filedata_hash) {
		this.filedata_hash=filedata_hash;
	}
	public void setFilenameHint (String filename_hint) {
		this.filename_hint=filename_hint;
	}
}
