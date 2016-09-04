/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class SysConfig {

	private static Logger logger = LoggerFactory.getLogger(SysConfig.class);
	private static final Properties properties = new Properties();
	public static String CONFIG_FILE = "/config.properties";

	static {
		loadConfig();
	}

	private SysConfig() {
	}

	private static void loadConfig() {
		try (InputStream input = SysConfig.class.getResourceAsStream(CONFIG_FILE)) {
			properties.load(input);
		} catch (IOException ex) {
			logger.error("load config error [{}]", CONFIG_FILE, ex);
		}
	}

	public static Properties getConfig() {
		return new Properties(properties);
	}

	public static String getConfig(String key, String def) {
		return (String) properties.getProperty(key, def);
	}

}
