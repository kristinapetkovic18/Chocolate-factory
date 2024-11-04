package dto;

import java.time.LocalDate;
import enums.Gender;
import enums.Role;

public class RegistrationDto {

	private String firstName;
	private String lastName;
	private int gender;
	private LocalDate dateOfBirth;
	private String fabrikaCokoladeId;

	public String getFabrikaCokoladeId() {
		return fabrikaCokoladeId;
	}

	public void setFabrikaCokoladeId(String fabrikaCokoladeId) {
		this.fabrikaCokoladeId = fabrikaCokoladeId;
	}
	
	

	public RegistrationDto(String firstName, String lastName, int gender, LocalDate dateOfBirth,
			String fabrikaCokoladeId, String username, String password, int role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.fabrikaCokoladeId = fabrikaCokoladeId;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	private String username;
	private String password;
	private int role;

	public RegistrationDto(String firstName, String lastName, int gender, LocalDate dateOfBirth, String username,
			String password, int role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.username = username;
		this.password = password;
		this.role = role;
	}


	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RegistrationDto(String firstName, String lastName, int gender, LocalDate dateOfBirth, String username,
			String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.username = username;
		this.password = password;
	}

	public RegistrationDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
