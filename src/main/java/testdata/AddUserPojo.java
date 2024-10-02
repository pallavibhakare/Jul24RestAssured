package testdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddUserPojo {
	//serialization
	
	private String accountno;
	private String departmentno;
	private String salary;
	private String pincode;
	
	public AddUserPojo(String accountno, String departmentno, String salary, String pincode) {
		
		this.accountno = accountno;
		this.departmentno = departmentno;
		this.salary = salary;
		this.pincode = pincode;
		
	}
	
	@JsonProperty("")
	public String getAccountno() {
		return accountno;
	}
	@JsonProperty("")
	public String getDepartmentno() {
		return departmentno;
	}
	@JsonProperty("")
	public String getSalary() {
		return salary;
	}
	@JsonProperty("")
	public String getPincode() {
		return pincode;
	}
}
