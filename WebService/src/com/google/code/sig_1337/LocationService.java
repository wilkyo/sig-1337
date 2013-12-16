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
	@Path("direction/{idDepart}/{idArrive}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			MediaType.TEXT_HTML })
	public String getDirection(@PathParam("idDepart") long idDepart,
			@PathParam("idArrive") long idArrive) {
		System.out.println("/WebService/service/location/direction/"
				+ idDepart + "/" + idArrive);
		return LocationHelper.getDirection(idDepart, idArrive);
	}
}
