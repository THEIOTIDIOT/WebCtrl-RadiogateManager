package getValue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controlj.green.addonsupport.access.ActionExecutionException;
import com.controlj.green.addonsupport.access.DirectAccess;
import com.controlj.green.addonsupport.access.FieldAccessFactory;
import com.controlj.green.addonsupport.access.Location;
import com.controlj.green.addonsupport.access.NoSuchAspectException;
import com.controlj.green.addonsupport.access.ReadAction;
import com.controlj.green.addonsupport.access.SystemAccess;
import com.controlj.green.addonsupport.access.SystemConnection;
import com.controlj.green.addonsupport.access.SystemException;
import com.controlj.green.addonsupport.access.SystemTree;
import com.controlj.green.addonsupport.access.UnresolvableException;
import com.controlj.green.addonsupport.access.WritableSystemAccess;
import com.controlj.green.addonsupport.access.WriteAction;
import com.controlj.green.addonsupport.access.aspect.PresentValue;
import com.controlj.green.addonsupport.access.value.FloatValue;
import com.controlj.green.addonsupport.access.value.InvalidValueException;
import getValue.Register;
import getValue.Meter;
import getValue.OFVIPoints;

@WebServlet(
		name = "OUServlet",
		urlPatterns = "/meter-manager")

public class OUServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "";
	static final String DB_URL = "jdbc:postgresql://localhost:5432/energy_meter_data";
	
	//  Database credentials
	static final String USER = "postgres";
	static final String PASS = "Es2_7176";
	
	public void setData(LinkedList<Register> regArray) {

		Connection con = null;
		Statement stmt = null;
		    
			try{
				
	        		//STEP 2: Register JDBC driver
					Class.forName("org.postgresql.Driver");
				
					//STEP 3: Open a connection
					con = DriverManager.getConnection(DB_URL, USER, PASS);
				
					//STEP 4: Execute a query
					stmt = con.createStatement();
					
					String sqlInsert = "INSERT INTO meterlog(refnumber, entrydate, entrytime, serialnumber, energya, energyb, energyc)\r\n";
					String sqlValues = "";
					sqlValues += "VALUES ";
					
					
					
					for(int i = 0; i < regArray.size(); i++) {
						sqlValues += "('" + regArray.get(i).reference + "', '" + regArray.get(i).dateStamp + "', '" + regArray.get(i).timeStamp + "', '" 
										+ regArray.get(i).serialNum + "', '" + regArray.get(i).phAEnergy + "', '" 
										+ regArray.get(i).phBEnergy + "', '" + regArray.get(i).phCEnergy + "')";
						
						if((i + 1) < regArray.size()){
							sqlValues += ", \r\n";
						}
					}
					printToFile(sqlInsert + sqlValues + ";", "SQL.txt");
					stmt.executeUpdate(sqlInsert + "\r\n" + sqlValues + ";");
					stmt.close();
					
					
					
		        }catch(SQLException se){
		        	sendErrorToLog(se.getLocalizedMessage(), se.toString());
				}catch(Exception ex) {
					sendErrorToLog(ex.getLocalizedMessage(), ex.toString());
				}finally {
					//finally block used to close resources
					try{
						if (stmt!=null)
							stmt.close();						
					}catch(SQLException se1) {
						sendErrorToLog(se1.getLocalizedMessage(), se1.toString());
					}
					
					try {
						if (con!=null)
							con.close();
					}catch(SQLException se2){
						sendErrorToLog(se2.getLocalizedMessage(), se2.toString());
					}
				}
	}
	
	public void setWebCtrlData(final LinkedList<Register> sortedMeterData) {
		
		SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
    	
    	try{
    		connection.runWriteAction(FieldAccessFactory.newFieldAccess(), "Updating the energy meter points", new WriteAction()
        {
           public void execute(WritableSystemAccess access) throws Exception
           {
        	   try{

        		   for(Register reg : sortedMeterData) {

        			   for(int i  = 0; i < 4; i++) {
        				   
        				   String reference = "#bldg_073_ofvi_collector/";
        				   StringBuilder sb = new StringBuilder(reg.getMeterID().toLowerCase());
        				   
            			   switch(i) {
            			   case 0 : sb.append("_serial");
            			   break;
            			   case 1 : sb.append("_kwh_a");
            			   break;
            			   case 2 : sb.append("_kwh_b");
            			   break;
            			   case 3 : sb.append("_kwh_c");
            			   break;
            			   }
            			   
            			   reference += sb.toString();
            			   
                   		  Location loc = access.getTree(SystemTree.Geographic).getRoot().getDescendant(reference);
                 		  PresentValue pv = loc.getAspect(PresentValue.class);
                 		  
                 		  switch(i) {
                 		  case 0 : ((FloatValue)pv.getValue()).set(reg.serialNum);
                 		  break;
                 		  case 1 : ((FloatValue)pv.getValue()).set(reg.phAEnergy);
                 		  break;
                 		  case 2 : ((FloatValue)pv.getValue()).set(reg.phBEnergy);
                 		  break;
                 		  case 3 : ((FloatValue)pv.getValue()).set(reg.phCEnergy);
                 		  
                 		  }
        			   }
        		   }
        	   }catch(Exception ex) {
        		   sendErrorToLog(ex.getMessage(), ex.getLocalizedMessage());
        	   };
           }
        } );}catch (Exception e) {
        	sendErrorToLog(e.getMessage(), e.getLocalizedMessage());
        };
	}

    public String getMeterID(int serNumMSW, int serNumLSW, LinkedList<Meter> listOfMeters){
    	
        int tempSerial = ((serNumMSW * 65536) + serNumLSW);
        String meterID = null;
        
        for(Meter m : listOfMeters) {
        	if(0 == m.getSerialNum().compareTo(Integer.valueOf(tempSerial))) {
        		return meterID = m.getMeterID();
        	}
        }
        
		return meterID;
    }

	public LinkedList<Register> sortData(LinkedList<Meter> meterList, LinkedList<Register> unsortedRegisterList) throws IOException, ActionExecutionException, SystemException{
		
		//comparing the unsorted list of registers and their serial numbers to the Used Meters list
		LinkedList<Register> sortedRegisterList = new LinkedList<Register>();
		try {
			
	        for(Meter m : meterList){
	        	
	            for(Register r : unsortedRegisterList){
	            	
	                if(0 == m.getSerialNum().compareTo(r.getSerial())){
	                  sortedRegisterList.add(r);
	                }
	            }
	        }
			
		}catch (Exception e) {
			
			sendErrorToLog(e.getLocalizedMessage(),e.toString());
		}
	
		//THis is where i am sorting the sorted register list to ensure there are not any duplicate values left		
		try {
			LinkedList<Register> sortedRegisterList1 = new LinkedList<Register>();
			LinkedList<Register> searchList = sortedRegisterList;
			
			int index1 = 0;

		        	for(Register reg1stList : sortedRegisterList) {
						
						int totalEnergy1st = reg1stList.getPhAEnergy().intValue();
						totalEnergy1st += reg1stList.getPhBEnergy().intValue();
						totalEnergy1st += reg1stList.getPhCEnergy().intValue();
						
						int index2 = 0;
						for(Register reg2ndList : searchList) {
							
							
							int totalEnergy2nd = reg2ndList.getPhAEnergy().intValue();
							totalEnergy2nd += reg2ndList.getPhBEnergy().intValue();
							totalEnergy2nd += reg2ndList.getPhCEnergy().intValue();
							
							if (0 == reg1stList.getSerial().compareTo(reg2ndList.getSerial())) {
		                        if (index1 != index2 && (totalEnergy1st >= totalEnergy2nd)){
		                            sortedRegisterList1.add(reg2ndList);
		                        }
							}
							index2 += 1;
		                        
							}
							index1 += 1;
						}

		        sortedRegisterList.removeAll(sortedRegisterList1);
			
		}catch(Exception ex){

			sendErrorToLog(ex.getLocalizedMessage(),ex.toString());
		}
		
		
		String s = "";
		for(Register r : sortedRegisterList) {
			
			s += r.getMeterID() + "	";
			s += r.getSerial() + "	";
			s += r.getPhAEnergy() + "	";
			s += r.getPhBEnergy() + "	";
			s += r.getPhCEnergy() + "	";
			s += r.getTotalEnergy() + "\r\n";
			
			
		}
		
		printToFile(s,"TEST9001.txt");
		
		//this section of code is actually verifying that the values being sent to webCtrl are larger than the current value coming from the radiogates
		
		WebCtrl webCtrl = new WebCtrl();
		webCtrl.getMeterValues();
		ArrayList<Register> webCtrlMeterList = webCtrl.meterList;
		
		for(Register reggie : sortedRegisterList) {
			
			for(Register reggie2 : webCtrlMeterList) {
				
				if (reggie2.serialNum == reggie.serialNum) {
					
					if(reggie2.getTotalEnergy().intValue() > reggie.getTotalEnergy().intValue()) {
						
						reggie.setPhAEnergy(reggie2.phAEnergy);
						reggie.setPhBEnergy(reggie2.phBEnergy);
						reggie.setPhCEnergy(reggie2.phCEnergy);
						
					}
				}
			}
		}
		
		return sortedRegisterList;
	}
	
//    public LinkedList<Meter> getListOfMeters() throws IOException, ActionExecutionException, SystemException {
//
//        LinkedList<Meter> meters = new LinkedList<Meter>();
//		Connection con = null;
//		Statement stmt = null;
//
//		        try{
//
//					Class.forName("org.postgresql.Driver");
//					
//					con = DriverManager.getConnection(DB_URL, USER, PASS);
//
//					stmt = con.createStatement();
//					String sqlQuery;
//					sqlQuery = "Select * From meter;";
//					stmt.executeQuery(sqlQuery);
//					ResultSet rs = stmt.executeQuery(sqlQuery);
//					
//					//Extract data from result set
//					while(rs.next()) {
//						//Retrieve the columns 
//						Integer serial = rs.getInt(5);
//						String id = rs.getString(2);
//						String desc = rs.getString(4);
//						Meter newMeter = new Meter(id, desc , serial);
//			            meters.add(newMeter);
//			            
//					}
//				}catch(SQLException se){
//					sendErrorToLog(se.getMessage(), se.getLocalizedMessage());
//				}catch(Exception ex) {
//					sendErrorToLog(ex.getMessage(), ex.getLocalizedMessage());
//				}finally {
//					//finally block used to close resources
//					try{
//						if (stmt!=null)
//							stmt.close();						
//					}catch(SQLException se1) {
//						sendErrorToLog(se1.getMessage(), se1.getLocalizedMessage());
//					}
//					
//					try {
//						if (con!=null)
//							con.close();
//					}catch(SQLException se2){
//						sendErrorToLog(se2.getMessage(), se2.getLocalizedMessage());
//					}
//				}
//		        
//
//       return meters;
//    }

	public void sendErrorToLog(String error, String description) {
		Connection con = null;
		Statement stmt = null;
		String date = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
		String time = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		    
			try{
				
	        		//STEP 2: Register JDBC driver
					Class.forName("org.postgresql.Driver");
				
					//STEP 3: Open a connection
					con = DriverManager.getConnection(DB_URL, USER, PASS);
				
					//STEP 4: Execute a query
					stmt = con.createStatement();
					
					String sqlInsert = "";
					
					sqlInsert = "INSERT INTO errorlog(error, errordescription, entrydate, entrytime)"
							+ " VALUES('" + error + "', '" + description + "', '" + date + "', '" + time +"')\n";
					
					stmt.executeUpdate(sqlInsert);
					stmt.close();
					
					
		        }catch(SQLException se){
		        	sendErrorToLog(se.getLocalizedMessage(), se.toString());
				}catch(Exception ex) {
					sendErrorToLog(ex.getLocalizedMessage(), ex.toString());
				}finally {

					try{
						if (stmt!=null)
							stmt.close();						
					}catch(SQLException se1) {
						sendErrorToLog(se1.getLocalizedMessage(), se1.toString());
					}
					
					try {
						if (con!=null)
							con.close();
					}catch(SQLException se2){
						sendErrorToLog(se2.getLocalizedMessage(), se2.toString());
					}
				}
			
	}
	
	public class ScheduledTask extends TimerTask {
		
		public void run() {
			
			LinkedList<Register> uncleanedMeterList;
			LinkedList<Meter> meterList;
			LinkedList<Register> cleanedMeterList;
			OFVIPoints op = new OFVIPoints();
			
			
			try {
				op.setMeterList();
			} catch (IOException e1) {
				sendErrorToLog(e1.getLocalizedMessage(), e1.toString());
			} catch (ActionExecutionException e1) {
				sendErrorToLog(e1.getLocalizedMessage(), e1.toString());
				e1.printStackTrace();
			} catch (SystemException e1) {
				sendErrorToLog(e1.getLocalizedMessage(), e1.toString());
			}

			
			int numberOfRadios = 31;
			//loops through all radiogates
			for(int j = 0; j < numberOfRadios; j++) {
					
	   				int radioGateNumber = j + 1;
					String path = "#eq_249998_" + Integer.toString(radioGateNumber);
					op.setRadiogateNumber(radioGateNumber);
					op.setRadioGatePath(path);

					try {
						op.getRadiogateValues();
					} catch(ActionExecutionException e2) {
						sendErrorToLog(e2.getLocalizedMessage(),e2.toString());
					}catch (SystemException e3) {
						sendErrorToLog(e3.getLocalizedMessage(),e3.toString());
					} catch (UnresolvableException e) {
						sendErrorToLog(e.getLocalizedMessage(),e.toString());
					} catch (NoSuchAspectException e) {
						sendErrorToLog(e.getLocalizedMessage(),e.toString());
					} catch (InvalidValueException e) {
						sendErrorToLog(e.getLocalizedMessage(),e.toString());
					}
			}
				
			try{
						
				meterList = getListOfMeters();
					
				uncleanedMeterList = op.getUnsortedRegisters();
					
				cleanedMeterList = sortData(meterList, uncleanedMeterList);
					
				setData(cleanedMeterList);
					
				setWebCtrlData(cleanedMeterList);
					
			}catch(Exception e){
					sendErrorToLog(e.getLocalizedMessage(), e.toString());
			}
		}
	}
	
	public void printToFile(final String content, final String fileName) {
		
		SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
    	
    	try{connection.runReadAction( FieldAccessFactory.newFieldAccess(), new ReadAction()
        {
           public void execute(SystemAccess access) throws Exception
           { 
        	   try{
        		   
        	        // writing file
        	        
        	        String fn = "D:\\" + fileName;
        			
        			try (BufferedWriter bw = new BufferedWriter(new FileWriter(fn))){
        				
        				bw.write(content);
        				
        			} catch (IOException e) {
        				sendErrorToLog(e.getLocalizedMessage(), e.toString());
        			}

        	   }catch(Exception ex) {
        		   sendErrorToLog(ex.getLocalizedMessage(), ex.toString());
        	   };
        	   
           }
        } );}catch (Exception e) {
        	sendErrorToLog(e.getLocalizedMessage(), e.toString());
        };
		
	}
	
	//Ben you suck at design patterns, get your shit together and decouple this shit.
	//Below is truly where the servlet begins
/*    private void processRequest(
    	      HttpServletRequest request, HttpServletResponse response) 
    	      throws ServletException, IOException, ActionExecutionException, SystemException {
    	
    	LinkedList<Meter> meterList = new LinkedList<Meter>();
    	meterList = getListOfMeters();
    	
    	LinkedList<String> serialList = new LinkedList<String>();
    	for(Meter m : meterList) {
    		serialList.add(Integer.toString(m.getSerialNum()));
    		
    	}
    	
    	request.setAttribute("meterList", serialList);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/meter-manager.jsp");
    	dispatcher.forward(request, response);
    }
    
    @Override
    protected void doGet(
      HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
 
        try {
			processRequest(request, response);
		} catch (ActionExecutionException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    @Override
    protected void doPost(
      HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
 
        try {
			processRequest(request, response);
		} catch (ActionExecutionException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }*/
	
}