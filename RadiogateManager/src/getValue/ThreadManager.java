package getValue;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import getValue.ScheduledTask;

public class ThreadManager implements ServletContextListener {

	
	ScheduledTask st = new ScheduledTask(); 
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		st.cancel();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		Timer time = new Timer();
		time.scheduleAtFixedRate(st, 0, 900000);
		
	}

}
