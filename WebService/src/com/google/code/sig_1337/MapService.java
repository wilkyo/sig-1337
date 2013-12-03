package com.google.code.sig_1337;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.sql.SQLToXml;

@Path("/map")
public class MapService {
	// This method is called if XML is requested
	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public String getXML() {
		System.out.println("XML");
		return SQLToXml.process("files/Universite.osm");
	}

	// This method is called if JSON is requested
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getJSON() {
		System.out.println("JSON");
		return SQLToXml.process("files/Universite.osm");
	}

	// This can be used to test the integration with the browser
	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String getHTML() {
		System.out.println("HTML");
		return SQLToXml.process("files/Universite.osm");
	}

}
