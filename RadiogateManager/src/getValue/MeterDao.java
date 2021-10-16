package getValue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MeterDao implements PostgresInterface<Meter> {
	
	private String DB_URL = "jdbc:postgresql://localhost:5432/energy_meter_data";
	private String USER = "postgres";
	private String PASS = "Es2_7176";

	private Connection con = null;
	private Statement stmt = null;
	
	private String sqlInsert = "";
	private String sqlValues = "";
	private String sqlQuery = "";
	private String sqlUpdate = "";
	private String sqlDelete = "";
	
	
	
	public MeterDao(Meter meter){
	}
	
	@Override
	public Optional<Meter> get(long id) throws SQLException {
		
		LinkedList<Meter> nullMeterList = getAll(); 
		return Optional.ofNullable(nullMeterList.get((int) id));
		
	}
	
	//create
	@Override
	public void save(Meter meter) throws SQLException {
		
		
		con = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = con.createStatement();
		sqlInsert += "INSERT INTO meter(meterid, metername, description, serial, referenceName)\r\n";
		sqlValues += "VALUES ('" + meter.getMeterID() + "', '" + meter.getMeterName() + "', '" + meter.getDescription() + "', '" 
				+ Integer.toString(meter.getSerialNum()) + "', '" + meter.getReferenceName() + "')";
		stmt.executeUpdate(sqlInsert + sqlValues);
		stmt.close();
		con.close();


	}
	
	//read
	@Override
	public LinkedList<Meter> getAll() throws SQLException{
		
		LinkedList<Meter> meters = new LinkedList<Meter>();
		con = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = con.createStatement();
		sqlQuery = "Select * From meter;";
		ResultSet rs = stmt.executeQuery(sqlQuery);
		
		//Extract data from result set
		while(rs.next()) {
			
			//Retrieve the columns 
			Meter newMeter = new Meter(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getInt(4), rs.getString(3), rs.getString(5));
            meters.add(newMeter);
		}
		stmt.close();
		con.close();
		return meters;
	}
	
	//update
	@Override
	public void update(Meter meter, String[] params) throws SQLException {
		meter.setMeterID(Objects.requireNonNull(params[0], "Meter ID cannot be null"));
		meter.setMeterName(Objects.requireNonNull(params[1], "Meter Name cannot be null"));
		meter.setDescription(Objects.requireNonNull(params[2], "Description cannot be null"));
		meter.setSerialNum(Objects.requireNonNull(Integer.parseInt(params[3]), "Serial Number cannot be null"));
		meter.setReferenceName(Objects.requireNonNull(params[4], "Reference Name cannot be null"));
		con = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = con.createStatement();
		sqlUpdate += "UPDATE meter SET meterid = '" + meter.getMeterID() + "', metername = '" + meter.getMeterName() +
				"', description = '" + meter.getDescription() + "', serial = " + Integer.toString(meter.getSerialNum()) + 
				", referencename = '" + meter.getReferenceName() + "'\n\r WHERE id = " + meter.getPrimaryKey() + ";";
		stmt.executeQuery(sqlUpdate);
		stmt.close();
		con.close();
		
	}

	//delete
	@Override
	public void delete(Meter meter) throws SQLException {
		con = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = con.createStatement();
		sqlDelete += "DELETE FROM meter WHERE id = " + Integer.toString(meter.getPrimaryKey()) + ";";
		stmt.executeQuery(sqlDelete);
		stmt.close();
		con.close();
		
	}

}
