package getValue;

import java.util.TimerTask;

public class ScheduledTask extends TimerTask{
	
	public void run() {
		//
		LinkedList<Register> uncleanedMeterList;
		LinkedList<Meter> meterList;
		LinkedList<Register> cleanedMeterList;
		
		OFVIPoints op = new OFVIPoints();
		
		
		

		
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

}
