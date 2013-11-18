package tests.xml.personne;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PersonneHandler extends DefaultHandler {
	// résultats de notre parsing
	private List<Personne> annuaire;
	private Personne personne;
	// flags nous indiquant la position du parseur
	private boolean inAnnuaire, inPersonne, inNom, inPrenom, inAdresse;
	// buffer nous permettant de récupérer les données
	private StringBuffer buffer;

	// simple constructeur
	public PersonneHandler() {
		super();
	}

	// détection d'ouverture de balise
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("annuaire")) {
			annuaire = new LinkedList<Personne>();
			inAnnuaire = true;
		} else if (qName.equals("personne")) {
			personne = new Personne();
			try {
				int id = Integer.parseInt(attributes.getValue("id"));
				personne.setId(id);
			} catch (Exception e) {
				// erreur, le contenu de id n'est pas un entier
				throw new SAXException(e);
			}
			inPersonne = true;
		} else {
			buffer = new StringBuffer();
			if (qName.equals("nom")) {
				inNom = true;
			} else if (qName.equals("prenom")) {
				inPrenom = true;
			} else if (qName.equals("adresse")) {
				inAdresse = true;
			} else {
				// erreur, on peut lever une exception
				throw new SAXException("Balise " + qName + " inconnue.");
			}
		}
	}

	// détection fin de balise
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("annuaire")) {
			inAnnuaire = false;
		} else if (qName.equals("personne")) {
			annuaire.add(personne);
			personne = null;
			inPersonne = false;
		} else if (qName.equals("nom")) {
			personne.setNom(buffer.toString());
			buffer = null;
			inNom = false;
		} else if (qName.equals("prenom")) {
			personne.setPrenom(buffer.toString());
			buffer = null;
			inPrenom = false;
		} else if (qName.equals("adresse")) {
			personne.setAdresse(buffer.toString());
			buffer = null;
			inAdresse = false;
		} else {
			// erreur, on peut lever une exception
			throw new SAXException("Balise " + qName + " inconnue.");
		}
	}

	// détection de caractères
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String lecture = new String(ch, start, length);
		if (buffer != null)
			buffer.append(lecture);
	}

	// début du parsing
	public void startDocument() throws SAXException {
		System.out.println("Début du parsing");
	}

	// fin du parsing
	public void endDocument() throws SAXException {
		System.out.println("Fin du parsing");
		System.out.println("Resultats du parsing");
		for (Personne p : annuaire) {
			System.out.println(p);
		}
	}
}
