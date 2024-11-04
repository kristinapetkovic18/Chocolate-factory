package beans;

import java.io.Serializable;

import enums.CustomerTypeName;


public class CustomerType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomerTypeName name;
	private double discount;
	private int pointsRequired;
	//na svakih sto se updatuje ako je none 0 popusta, ako je silver 5 posto, 10 za bronze i 15 za gold
	
	public CustomerType(CustomerTypeName name, double discount, int pointsRequired) {
		super();
		this.name = name;
		this.discount = discount;
		this.pointsRequired = pointsRequired;
	}
	
	public CustomerType() {}

	public CustomerType(String string, double parseDouble, int parseInt) {
		// TODO Auto-generated constructor stub
	}

	public CustomerTypeName getName() {
		return name;
	}

	public void setName(CustomerTypeName name) {
		this.name = name;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getPointsRequired() {
		return pointsRequired;
	}

	public void setPointsRequired(int pointsRequired) {
		this.pointsRequired = pointsRequired;
	}


	
	
		
}

