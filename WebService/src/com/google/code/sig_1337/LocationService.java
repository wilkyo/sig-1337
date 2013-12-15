package com.google.code.sig_1337;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import data.sql.LocationHelper;

@Path("/location")
public class LocationService {
	@Context
	ServletContext context;

	@GET
	@Path("building/{lat}/{lon}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_HTML })
	public String getNearestBuiding(@PathParam("lat") double latitude,
			@PathParam("lon") double longitude) {
		System.out.println("/WebService/service/location/building/" + latitude
				+ "/" + longitude);
		return LocationHelper.getNearestBuilding(latitude, longitude, false);
	}

	@GET
	@Path("direction/{fromlat}/{fromlon}/{tolat}/{tolon}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_HTML })
	public String getDirection(@PathParam("fromlat") double srcLatitude,
			@PathParam("fromlon") double srcLongitude,
			@PathParam("tolat") double destLatitude,
			@PathParam("tolon") double destLongitude) {
		System.out.println("/WebService/service/location/direction/"
				+ srcLatitude + "/" + srcLongitude + "/" + destLatitude + "/"
				+ destLongitude);
		return LocationHelper.getDirection(srcLatitude, srcLongitude,
				destLatitude, destLongitude);
	}
}
