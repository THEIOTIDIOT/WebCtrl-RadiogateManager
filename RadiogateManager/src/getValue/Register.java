package getValue;

public class Register {
	
	private int register;
	private String reference;
	private int serialNum, phAEnergy, phBEnergy, phCEnergy;
	private String timeStamp, dateStamp, meterID;
	
	// this is the constructor for the class
	// it is automatically created by default any time the class is used to create an object
	// if explicitly defined, the properties defined in it's property header can be accessed by the objects
	public Register( int reg, String ref, int serNum, int phAEn, int phBEn, int phCEn, String meterID, String time, String date) {
		this.register = reg;
		this.reference = ref;
		this.serialNum = serNum;
		this.phAEnergy = phAEn;
		this.phBEnergy = phBEn;
		this.phCEnergy = phCEn;
		this.meterID = meterID;
		this.timeStamp = time;
		this.dateStamp = date;
	}
	
	public Register(int serial, int phAEn, int phBEn, int phCEn) {
		this.serialNum = serial;
		this.phAEnergy = phAEn;
		this.phBEnergy = phBEn;
		this.phCEnergy = phCEn;
	}
	
	
	public Integer getSerial() {
		
		return serialNum;
	}
	
	public Integer getPhAEnergy() {
		
		return phAEnergy;
	}
	
	public Integer getPhBEnergy() {
		
		return phBEnergy;
	}
	
	public Integer getPhCEnergy() {
		return phCEnergy;
	}
	
	public String getMeterID() {
		return meterID;
	}
	
	public Integer getTotalEnergy() {
		
		return (this.phAEnergy + this.phBEnergy + this.phCEnergy);
	}
	
	public void setPhAEnergy(int phAEnergy) {
		this.phAEnergy = phAEnergy;
		
	}
	
	public void setPhBEnergy(int phBEnergy) {
		this.phBEnergy = phBEnergy;
		
	}
	
	public void setPhCEnergy(int phCEnergy) {
		this.phCEnergy = phCEnergy;
		
	}
}
