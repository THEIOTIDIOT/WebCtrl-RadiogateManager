package getValue;

public class RadioGate {
	
	//private int reg3, reg4, reg5, reg6, reg7, reg8, reg9, reg10, reg11, reg12, reg13, reg14, reg15, reg16, reg17, reg18, reg19, reg20, reg21, reg22, reg23, reg24;
	int serialNum, totalEnergyA, totalEnergyB, totalEnergyC, radiogate_ID, volts_A, volts_B, volts_C, current_A, current_B, current_C, sigStrength;
	float angle_A, angle_B, angle_C;
	
	public RadioGate(String dateTime, int ser, int reg3, int reg4, int reg5, int reg6, int reg7, int reg8, int reg9, int reg10, int reg11, int reg12, int reg13,
					int reg14, int reg15, int reg16, int reg17, int reg18, int reg19, int reg20, int reg21, int reg22, int reg23, int reg24){
		
		this.serialNum = ser;
//		this.reg3 = reg3;
//		this.reg4 = reg4;
//		this.reg5 = reg5;
//		this.reg6 = reg6;
//		this.reg7 = reg7;
//		this.reg8 = reg8;
//		this.reg9 = reg9;
//		this.reg10 = reg10;
//		this.reg11 = reg11;
//		this.reg12 = reg12;
//		this.reg13 = reg13;
//		this.reg14 = reg14;
//		this.reg15 = reg15;
//		this.reg16 = reg16;
//		this.reg17 = reg17;
//		this.reg18 = reg18;
//		this.reg19 = reg19;
//		this.reg20 = reg20;
//		this.reg21 = reg21;
//		this.reg22 = reg22;
//		this.reg23 = reg23;
//		this.reg24 = reg24;
	}
	
	
	public int getSerialNum() {
		return this.serialNum;
	}
	
	public int getTotalEnergyA() {
		return this.totalEnergyA;
	}
	
	public int getTotalEnergyB() {
		return this.totalEnergyB;
	}
	
	public int getTotalEnergyC() {
		return this.totalEnergyC;
	}
	
	

}
