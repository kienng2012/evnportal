package Icom.Listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import Icom.API.ResourceEntity;
import Icom.Utils.Config;
import Icom.Utils.Constants;
import Icom.Utils.DBPool;
import Icom.Utils.Utils;


public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		ResourceEntity.loadConfig();
		ResourceEntity.initResource();
		
		Utils.logger.info(".........WebService EVN Bat dau chay ung dung..................");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		ResourceEntity.saveResource();
		
		Utils.logger.info(".........WebService EVN Shutdown ung dung...................");
		
	}

}
