package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Base {
	
	public static Properties property;
	static {
		try {
		property= new Properties();
		String path= "src/test/resources/apiConfigs.properties";
		File file= new File(path);
		FileInputStream fis= new FileInputStream(file);
		property.load(fis);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
