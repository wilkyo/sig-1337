package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import data.sql.SQLCreate;
import data.sql.SQLToXml;

public class DataGenerator {

	public static void main(String[] args) {
		SQLCreate.createDataBase();
		FileWriter out;
		try {
			out = new FileWriter(new File("files/map.xml"));
			out.write(SQLToXml.process("files/Universite.osm"));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
