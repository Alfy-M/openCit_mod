package com.intel.mtwilson.trustagent.model;

import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="ima_list")
public class IMAList {
	private ArrayList<IMAvalue> ima_values;

	public IMAList () {
		ima_values = new ArrayList<IMAvalue>();
	}
	
	public ArrayList<IMAvalue> getImaValues () {
		return ima_values;
	}
	
	public void setImaValues (ArrayList<IMAvalue> ima_values) {
		this.ima_values=ima_values;
	}
}
