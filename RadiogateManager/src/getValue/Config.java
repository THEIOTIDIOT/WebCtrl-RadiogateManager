package getValue;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class Config extends OUServlet implements ServletContextListener {
	private static final long serialVersionUID = 1L;
	
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