package getValue;

import java.util.ArrayList;
import java.util.LinkedList;

import com.controlj.green.addonsupport.access.ActionExecutionException;
import com.controlj.green.addonsupport.access.DirectAccess;
import com.controlj.green.addonsupport.access.FieldAccessFactory;
import com.controlj.green.addonsupport.access.Location;
import com.controlj.green.addonsupport.access.ReadAction;
import com.controlj.green.addonsupport.access.SystemAccess;
import com.controlj.green.addonsupport.access.SystemConnection;
import com.controlj.green.addonsupport.access.SystemException;
import com.controlj.green.addonsupport.access.SystemTree;
import com.controlj.green.addonsupport.access.aspect.PresentValue;
import com.controlj.green.addonsupport.access.value.FloatValue;

public class WebCtrl extends OUServlet {
	
	private static final long serialVersionUID = 1L;
	ArrayList<Register> meterList = new ArrayList<Register>();
	
	public ArrayList<Register> getMeterValues() throws ActionExecutionException, SystemException {
		
		SystemConnection connection = DirectAccess.getDirectAccess().getRootSystemConnection();
		connection.runReadAction( FieldAccessFactory.newFieldAccess(), new ReadAction()
	    {
	       public void execute(SystemAccess access) throws Exception
	       { 
	    	   OUServlet ous = new OUServlet();
	    	   LinkedList<Meter> meterSearchList = ous.getListOfMeters();
	    	   
	    	   for(Meter m : meterSearchList) {
	    		   
	    		   
	    		   String meterID = m.getMeterID().toLowerCase(); 
	    		   
	    		   String[] stringArr = new String[4]; 
	    		   
	    		   
	    		   //this is really scary, please change so that the logic will look at the actual available meters
	    		   stringArr[0] = "#bldg_073_ofvi_collector/" + meterID + "_serial";
	    		   stringArr[1] = "#bldg_073_ofvi_collector/" + meterID + "_kwh_a";
	    		   stringArr[2] = "#bldg_073_ofvi_collector/" + meterID + "_kwh_b";
	    		   stringArr[3] = "#bldg_073_ofvi_collector/" + meterID + "_kwh_c";
	    		   
	    		   int serial = 0;
	    		   int phAEn = 0;
	    		   int phBEn = 0;
	    		   int phCEn = 0;
	    		   
	    		   for(int i = 0; i < stringArr.length; i++) {
	    			   
	    			   String fullRef = stringArr[i]; 
	          		   Location loc = access.getTree(SystemTree.Geographic).getRoot().getDescendant(fullRef);
	          		   PresentValue pv = loc.getAspect(PresentValue.class);
	          		   FloatValue val = (FloatValue) pv.getValue();
	          		   float fl = val.getValue();
	          		   int flToInt = (int) fl;
	          		   
	          		   
	          		   switch(i) {
	          		   case 0 : serial = flToInt;
	          		   break;
	          		   case 1 : phAEn = flToInt;
	          		   break;
	          		   case 2 : phBEn = flToInt;
	          		   break;
	          		   case 3 : phCEn = flToInt;
	          		   break;
	          		   }
	          		   
	          		   
	    		   }
	    		   
	    		   Register reggie = new Register(serial, phAEn, phBEn, phCEn);
	    		   //ous.sendErrorToLog(Integer.toString(reggie.serialNum), reggie.meterID);
	    		   meterList.add(reggie);
	    	   }   
	       }
	    });
		
		return meterList;
	}

}
