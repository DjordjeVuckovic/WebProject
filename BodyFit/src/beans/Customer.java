package beans;

import java.util.ArrayList;
import java.util.Date;

public class Customer extends User {
	private ArrayList<String> visitedFacilities;
	private CustomerType customerType;
	private double colletedPoints;
	private Membership membership;
	

	
	public Customer(String username, String password, String name, String surname, Date birthdate, Gender gender,ArrayList<String> visitedFacilities, CustomerType customerType, double colletedPoints,UserRole userRole) {
		super(username, password, name, surname, birthdate, gender,userRole);
		this.visitedFacilities = visitedFacilities;
		this.customerType = customerType;
		this.colletedPoints = colletedPoints;
	}
	public Customer(String username, String password, String name, String surname, Date birthdate, Gender gender,
			UserRole userRole, double colletedPoints) {
		super(username, password, name, surname, birthdate, gender, userRole);
		this.colletedPoints = colletedPoints;
		visitedFacilities = new ArrayList<String>();
		this.customerType = new CustomerType("BRONZE", 0, 0);
	}
	public Customer(String username, String password, String name, String surname, Date birthdate, Gender gender,
			UserRole userRole,Membership due) {
		super(username, password, name, surname, birthdate, gender, userRole);
		this.setUserRole(userRole);
		visitedFacilities = new ArrayList<String>();
		this.customerType = new CustomerType("BRONZE", 0, 0);
		this.colletedPoints = 0;
		this.membership = due;

	}
	
	public Customer(String username, String password, String name, String surname, Date birthdate, Gender gender,
			UserRole userRole) {
		super(username, password, name, surname, birthdate, gender, userRole);
		this.setUserRole(UserRole.CUSTOMER);
		this.colletedPoints = 0;
		visitedFacilities = new ArrayList<String>();
		this.customerType = new CustomerType("BRONZE", 0, 0);
	}
	
	public Customer() {
		super();
	}
	public ArrayList<String> getVisitedFacilities() {
		return visitedFacilities;
	}
	public void setVisitedFacilities(ArrayList<String> visitedFacilities) {
		this.visitedFacilities = visitedFacilities;
	}
	public CustomerType getCustomerType() {
		return customerType;
	}
	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}
	public double getColletedPoints() {
		return colletedPoints;
	}
	public void setColletedPoints(double colletedPoints) {
		this.colletedPoints = colletedPoints;
	}
	public Membership getMembership() {
		return membership;
	}
	public void setMembership(Membership due) {
		this.membership = due;
	}
	
	

}
