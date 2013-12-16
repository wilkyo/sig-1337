package com.google.code.sig_1337;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
	public String getXML( @Context HttpServletRequest req) {
		System.out.println("XML from:" + req.getRemoteAddr() + ":" + req.getRemoteHost());
		return SQLToXml.process(
				new File(context.getRealPath("/WEB-INF/files/Universite.osm"))
						.getAbsolutePath(), true);
	}

	// This method is called if JSON is requested
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String getJSON( @Context HttpServletRequest req) {
		System.out.println("JSON from:" + req.getRemoteAddr() + ":" + req.getRemoteHost());
		return SQLToXml.process(
				new File(context.getRealPath("/WEB-INF/files/Universite.osm"))
						.getAbsolutePath(), true);
	}

	// This can be used to test the integration with the browser
	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String getHTML( @Context HttpServletRequest req) {
		System.out.println("HTML from:" + req.getRemoteAddr() + ":" + req.getRemoteHost());
		return SQLToXml.process(
				new File(context.getRealPath("/WEB-INF/files/Universite.osm"))
						.getAbsolutePath(), true);
	}

}
