package com.google.code.sig_1337;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/location")
public class LocationService {
	@Context
	ServletContext context;

	@GET
	@Path("building/{lat}/{lon}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_HTML })
	public String getNearestBuiding(@PathParam("lat") String latitude,
			@PathParam("lon") String longitude) {
		System.out.println("getNearestBuiding(" + latitude + ", " + longitude
				+ ")");
		return "{}";
	}

	@GET
	@Path("direction/{fromlat}/{fromlon}/{tolat}/{tolon}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_HTML })
	public String getDirection(@PathParam("fromlat") String srcLatitude,
			@PathParam("fromlon") String srcLongitude,
			@PathParam("tolat") String destLatitude,
			@PathParam("tolon") String destLongitude) {
		System.out.println("getDirection(" + srcLatitude + ", " + srcLongitude
				+ " -> " + destLatitude + ", " + destLongitude + ")");
		return "{}";
	}
}
