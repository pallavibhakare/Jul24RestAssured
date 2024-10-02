package testdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserDataPojo {
	@JsonProperty("accountno")
	private String accountno;
	@JsonProperty("departmentno")
	private String departmentno;
	@JsonProperty("salary")
	private String salary;
	@JsonProperty("pincode")
	private String pincode;
	@JsonProperty("userid")
	private String userid;
	@JsonProperty("id")
	private String id;
	   
	public UpdateUserDataPojo() {
		//This will allow Jackson to create an instance of the class before populating its fields.
	}
	
	public UpdateUserDataPojo(String accountno, String departmentno, String salary, String pincode, String userid, String id) {
		
		this.accountno = accountno;
		this.departmentno = departmentno;
		this.salary = salary;
		this.pincode = pincode;
		this.userid = userid;
		this.id = id;
	}
	
	
	public String getAccountno() {
		return accountno;
	}
	
	public String getDepartmentno() {
		return departmentno;
	}
	
	public String getSalary() {
		return salary;
	}
	
	public String getPincode() {
		return pincode;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public String getId() {
		return id;
	}
}
