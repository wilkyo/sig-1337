package com.google.code.sig_1337;

import java.io.File;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import data.sql.SQLToXml;

@Path("/map")
public class MapService {
	@Context
	ServletContext context;

	// This method is called if XML is requested
	@GET
	@Produces({ MediaType.APPLICATION_XML })
	public String getXML() {
		System.out.println("XML");
		return SQLToXml.process(
				new File(context.getRealPath("/WEB-INF/files/Universite.osm"))
						.getAbsolutePath(), true);
	}

	// This method is called if JSON is requested
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getJSON() {
		System.out.println("JSON");
		return SQLToXml.process(
				new File(context.getRealPath("/WEB-INF/files/Universite.osm"))
						.getAbsolutePath(), true);
	}

	// This can be used to test the integration with the browser
	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String getHTML() {
		System.out.println("HTML");
		return SQLToXml.process(
				new File(context.getRealPath("/WEB-INF/files/Universite.osm"))
						.getAbsolutePath(), true);
	}

}
