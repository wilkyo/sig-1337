package tests.xml;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.io.*;

public class ExempleSAX {
	public static void main(String[] args) {
		try {
			// création d'une fabrique de parseurs SAX
			SAXParserFactory fabrique = SAXParserFactory.newInstance();

			// création d'un parseur SAX
			SAXParser parseur = fabrique.newSAXParser();

			// lecture d'un fichier XML avec un DefaultHandler
			File fichier = new File("./files/Universite.osm");
			DefaultHandler gestionnaire = new DefaultHandler();
			parseur.parse(fichier, gestionnaire);
			System.out.println("It's all good");

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