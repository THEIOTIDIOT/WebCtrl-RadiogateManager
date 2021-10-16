package getValue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

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
import com.controlj.green.addonsupport.access.aspect.PresentValue;
import com.controlj.green.addonsupport.access.value.FloatValue;
import com.controlj.green.addonsupport.access.value.InvalidValueException;

import getValue.Meter;
import getValue.Register;
import getValue.OUServlet;


public class OFVIPoints {
	
	private LinkedList<Meter> listOfMeters;
	private LinkedList<Register> unsortedRegisters = new LinkedList<Register>();
	private int radioGateNumber;
	private String path;
	
	public LinkedList<Register> getUnsortedRegisters() {
		return unsortedRegisters;
	}

	public void setRadiogateNumber(int radioGateNum) {
		this.radioGateNumber = radioGateNum;
	}
	
	public void setRadioGatePath(String path) {

		this.path = path;
	}
	
	private LinkedList<String> getReferences() {

		LinkedList<String> mbRefFull = new LinkedList<String>();
		String[] mbRefSub = {"_ph_a_eng",  "_ph_b_eng", "_ph_c_eng", "_serial"};
		String ref = "";
		
		for(int j = 1; j <= 100; j ++) {
			
	    	for(int i = 0; i < mbRefSub.length; i++) {
	    		
	    		ref = "m" + Integer.toString(j) + mbRefSub[i];
	    		mbRefFull.add(ref);
	    	}
			
		}
		return mbRefFull;
	}
	
	public void setMeterList() throws IOException, ActionExecutionException, SystemException {
		
		OUServlet ous = new OUServlet();
		
		this.listOfMeters = ous.getListOfMeters();;
	}
	
	public void iterateRadioGateRegisters() throws ActionExecutionException, SystemException, UnresolvableException, NoSuchAspectException, InvalidValueException {
		
		int numberOfRadios = 0;
		//loops through all radiogates
			for(int j = 0; j < numberOfRadios; j++) {
				
   				this.radioGateNumber = j + 1;
				
				this.path = "#eq_249998_" + Integer.toString(radioGateNumber);
				
				getRadiogateValues();
				
			}
	}
		
	public void getRadiogateValues() throws ActionExecutionException, SystemException, UnresolvableException, NoSuchAspectException, InvalidValueException{
	
		SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
		connection.runReadAction( FieldAccessFactory.newFieldAccess(), new ReadAction()
	    {
	       public void execute(SystemAccess access) throws Exception
	       { 
	    	   
	    	   LinkedList<String> mbRefFull = getReferences();

	    	   String ref;
					
					int regInt = 1;
					int refInt = 0;
					
					//loops through registers in radiogate
			    	for(int i = 0; i < 100; i++) {
			        	
			        		   String fullRef;
			        	 	    Register obj;
			        			String date = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
			        			String time = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
			        			int serNum; 
			        			int phAEn; 
			        			int phBEn; 
			        			int phCEn;
			        			int[] intArray = new int[8];
			        		   
			        		   
			        		 //loops through values in register
			        		   for(int k = 0; k < 8; k++) {
			        			   
			        			fullRef = path + "/" + mbRefFull.get(refInt);
			           		   	Location loc = access.getTree(SystemTree.Geographic).getRoot().getDescendant(fullRef);
			           		   	PresentValue pv = loc.getAspect(PresentValue.class);
			           		   	FloatValue val = (FloatValue) pv.getValue();
			           		   	float fl = val.getValue();
			           		   	int flToInt = (int) fl;

			        			switch(k) {
			        			case 0 : intArray[0] = flToInt;
			        			break;
			        			case 1 : intArray[1] = flToInt;
			        			break;
			        			case 2 : intArray[2] = flToInt;
			        			break;
			        			case 3 : intArray[3] = flToInt;
			        			break;
			        			}

			    		        phAEn = intArray[0];
			    		        phAEn = intArray[1];
			    		        phBEn = intArray[2];
			    		        phBEn = intArray[3];

			    		        
			    		        StringBuilder sb = new StringBuilder(path);
			    		        
			    		        int counter = 1;
			    		        
			    		        while(counter <= 11) {
			    		        	
			    		        	
			    		        	sb.deleteCharAt(0);
			    		        	counter += 1;
			    		        	
			    		        }
			    		        OUServlet ous = new OUServlet();
			    		        String meterID = ous.getMeterID(serNum, serNum, listOfMeters);
			    		        ref = sb.toString() + Integer.toString(i);
			    				obj = new Register(regInt, ref, serNum, serNum, phAEn, phAEn, phBEn, phBEn, phCEn, phCEn, meterID, time, date);
			    				sb.setLength(0);
			    				unsortedRegisters.add(obj);
			    				refInt += 1;
			        		}
		        			regInt += 1;
			    	}
	       }
	    });
	}
	
}
