package com.robert.common.cfglog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
	private static final PropertyManager propertyManager=new PropertyManager();
	private PropertyManager(){
		
	}
	public static PropertyManager getInstance(){
		return propertyManager;
	}
	
	public String getProperty(String FilePropertieName,String Propertie) {
		try{
			InputStream inputStream =  new FileInputStream(new File(FilePropertieName));
			Properties p=new Properties();
			try{
				p.load(inputStream);
			}catch(Exception e){
				e.printStackTrace();
			}
			return p.getProperty(Propertie);
		}catch(Exception e){
			
		}
		return null;
	}

}
