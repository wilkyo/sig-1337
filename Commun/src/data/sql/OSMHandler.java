package data.sql;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OSMHandler extends DefaultHandler {
	// résultats de notre parsing
	private String bounds;

	// simple constructeur
	public OSMHandler() {
		super();
	}

	// détection d'ouverture de balise
	public void startElement(String uri, String localName, String qName,
			Attributes attr) throws SAXException {
		if (qName.equals("bounds")) {
			bounds = "<bounds minlat=\"" + attr.getValue("minlat")
					+ "\" minlon=\"" + attr.getValue("minlon") + "\" maxlat=\""
					+ attr.getValue("maxlat") + "\" maxlon=\""
					+ attr.getValue("maxlon") + "\" />";
		}
	}

	/**
	 * Gets the bounds element.
	 * 
	 * @return String of the bounds element.
	 */
	public String getBounds() {
		return bounds;
	}
}
