package data;

import data.sql.SQLCreate;
import data.sql.SQLToXml;

public class DataGenerator {

	public static void main(String[] args) {
		SQLCreate.createDataBase();
		SQLToXml.process("files/Fac.osm");
	}
}
