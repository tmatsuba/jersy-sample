package org.gside.jersy.sample.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * 設定ファイルを読み込むためのクラス
 * 
 * @author matsuba
 *
 */
public class ConfigProducer {

	private static Properties prop = new Properties();
	static {
		InputStream in = ConfigProducer.class.getClassLoader().getResourceAsStream("app.properties");
		try {
			prop.load(in);
			in.close();	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} 
		
	}
	
	/**
	 * 
	 * 設定ファイルを読み込み、StringクラスにInjectする 
	 * @param ip
	 * @return
	 */
	@Produces @Config
	public String getString(InjectionPoint ip) {
		Config configAnnotation = ip.getAnnotated().getAnnotation(Config.class);
		String configKey = configAnnotation.value();

		if ("".equals(configKey)) {
			throw new IllegalArgumentException();
		}

		String val = prop.getProperty(configKey);
		if (val == null) {
			throw new IllegalArgumentException();
		}

		return val;
	}
}


