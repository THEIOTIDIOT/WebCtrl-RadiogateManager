package getValue;

import java.sql.*;
import java.util.LinkedList;


public class VisionMeterDB {
	private OUServlet ous = new OUServlet();
	
	
	public LinkedList<RadioGate> getLastValue(){
		LinkedList<RadioGate> rgList = new LinkedList<RadioGate>();
		Connection conn = null;

		try {
			
			String dbURL = "jdbc:sqlserver://10.255.0.29\\SQLEXPRESS";
			String user = "sa";
			String pass = "password1234";
			conn = DriverManager.getConnection(dbURL, user, pass);
			ous.sendErrorToLog("Hello", "Connection");
		} catch (SQLException e) {
			ous.sendErrorToLog("Hello", "SQL 1");
			ous.sendErrorToLog(e.getLocalizedMessage(), e.toString());
			
		}finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
            	ous.sendErrorToLog("Hello", "SQL 2");
            	ous.sendErrorToLog(ex.getLocalizedMessage(), ex.toString());
            	
            }
        }
		
		return rgList;
	}
	
}
