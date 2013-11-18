package tests.xml.personne;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.io.*;

public class ExempleSAXPersonne {
	public static void main(String[] args) {
		try {
			SAXParserFactory fabrique = SAXParserFactory.newInstance();
			SAXParser parseur = fabrique.newSAXParser();

			File fichier = new File("./files/personnes.xml");
			DefaultHandler gestionnaire = new PersonneHandler();
			parseur.parse(fichier, gestionnaire);

		} catch (ParserConfigurationException pce) {
			System.out.println("Erreur de configuration du parseur");
			System.out.println("Lors de l'appel à newSAXParser()");
		} catch (SAXException se) {
			System.out.println("Erreur de parsing");
			System.out.println("Lors de l'appel à parse()");
		} catch (IOException ioe) {
			System.out.println("Erreur d'entrée/sortie");
			System.out.println("Lors de l'appel à parse()");
		}
	}
}