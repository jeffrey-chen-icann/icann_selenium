package com.icann._contentrecordtypes;

import java.util.Arrays;
import java.util.List;

public class RegistryAgreement extends _ContentRecord{
	public String sGtld;
	public String sAgreementType = "";
	public String sOperator = "";
	public String sAgreementStatus = "";
	public String sAgreementDate = "";
	
	//constructors
	public RegistryAgreement(String sNewGtld, String sNewAgreementType, String sNewOperator, String sNewAgreementStatus, String sNewAgreementDate) {
		this.sGtld = sNewGtld;
		this.sAgreementType = sNewAgreementType;
		this.sOperator = sNewOperator;
		this.sAgreementStatus = sNewAgreementStatus;
		this.sAgreementDate = sNewAgreementDate;
	}
	public RegistryAgreement(String sNewGtld) {
		this(sNewGtld, "unset", "unset", "unset", "unset");
	}
	public RegistryAgreement() {
		this("unset", "unset", "unset", "unset", "unset");
	}


	public String toString() {
		return this.toList().toString();
	}
	
	public List<String> toList() {
		return Arrays.asList(this.sGtld, this.sAgreementType, this.sOperator, this.sAgreementStatus, this.sAgreementDate);
	}

}
