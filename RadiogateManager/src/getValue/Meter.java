package getValue;

public class Meter {
	
	private String meterID, meterName, meterDescription, referenceName;
	private int serialNum, primaryKey;
	
	public Meter(int pK, String meterID, String meterDescription, int serialNum, String referenceName, String meterName) {
		this.meterDescription = meterDescription;
		this.meterID = meterID;
		this.serialNum = serialNum;
		this.referenceName = referenceName;
		this.meterName = meterName;
		this.primaryKey = pK;
	}
	
	public int getPrimaryKey() {
		return primaryKey;
	}
	
	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}
	public String getMeterID() {
		return meterID;
	}
	
	public void setDescription(String meterDesc) {
		this.meterDescription = meterDesc;
	}
	public String getDescription() {
		return meterDescription;
	}
	
	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}
	public int getSerialNum() {
		return serialNum;
	}
	
	public void setReferenceName(String ref) {
		this.referenceName = ref;
	}
	public String getReferenceName() {
		return referenceName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	public String getMeterName() {
		return meterName;
	}
	
	
	
}

