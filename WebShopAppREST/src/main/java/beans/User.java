package beans;

import java.io.Serializable;
import java.time.LocalDate;

import dto.LoginDTO;
import enums.CustomerTypeName;
import enums.Gender;
import enums.Role;

public class User implements Serializable {

	
	private String id;
	private boolean deleted;

	public User(String id, boolean deleted, String firstName, String lastName, int gender, String username,
			String password, int role, String fabrikaCokoladeId, CustomerType tipKupca, int bodovi, LocalDate date) {
		super();
		this.id = id;
		this.deleted = deleted;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.username = username;
		this.password = password;
		this.role = role;
		this.fabrikaCokoladeId = fabrikaCokoladeId;
		this.tipKupca = tipKupca;
		this.bodovi = bodovi;
		this.dateOfBirth = date;
	}

	private String firstName;
	private String lastName;
	private int gender;
	
	private LocalDate dateOfBirth;

	private String username;
	private String password;
	private int role;

	private String fabrikaCokoladeId; // ako je menadzer
	private CustomerType tipKupca;
	private int bodovi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getFabrikaCokoladeId() {
		return fabrikaCokoladeId;
	}

	public void setFabrikaCokoladeId(String fabrikaCokoladeId) {
		this.fabrikaCokoladeId = fabrikaCokoladeId;
	}

	public CustomerType getTipKupca() {
		return tipKupca;
	}

	public void setTipKupca(CustomerType tipKupca) {
		this.tipKupca = tipKupca;
	}

	public int getBodovi() {
		return bodovi;
	}

	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public User() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;

		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	

	@Override
	public String toString() {
		return "User [id=" + id + ", deleted=" + deleted + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", username=" + username + ", password="
				+ password + ", role=" + role + ", fabrikaCokoladeId=" + fabrikaCokoladeId + ", tipKupca=" + tipKupca.toString()
				+ ", bodovi=" + bodovi + "]";
	}

	/*public boolean passwordMatches(LoginDTO dto) {
		return password.equals(hashPassword(dto.getPassword()));
	}*/



	public static String hashPassword(String password) {
		return Integer.toString(password.hashCode());
	}

	private static final long serialVersionUID = 6640936480584723344L;

	public boolean roleMatches(int role2) {
		boolean b = this.role == role2;
		return b;

	}


	public boolean passwordMatches(LoginDTO dto) {
	    boolean matches = this.password.equals(dto.getPassword());
	    System.out.println("Password match for user " + this.username + ": " + matches);
	    return matches;
	}


	public User(String id, boolean deleted, String firstName, String lastName, int gender, LocalDate dateOfBirth,
			String username, String password, int role, String fabrikaCokoladeId, CustomerType tipKupca, int bodovi) {
		super();
		this.id = id;
		this.deleted = deleted;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.username = username;
		this.password = password;
		this.role = role;
		this.fabrikaCokoladeId = fabrikaCokoladeId;
		this.tipKupca = tipKupca;
		this.bodovi = bodovi;
	}
	

	public User(String id, boolean deleted, String firstName, String lastName, int gender, LocalDate dateOfBirth,
			String username, String password, int role, String fabrikaCokoladeId, CustomerTypeName name, double discount,int pointsRequired, int bodovi) {
		super();
		this.id = id;
		this.deleted = deleted;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.username = username;
		this.password = password;
		this.role = role;
		this.fabrikaCokoladeId = fabrikaCokoladeId;
		this.tipKupca = new CustomerType(name, discount, pointsRequired);
		this.bodovi = bodovi;
	}
	
	
	public User(String firstName, String lastName, int gender, LocalDate dateOfBirth,
			String username, String password) {
		super();
		this.deleted = false;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.username = username;
		this.password = password;
		this.tipKupca = new CustomerType(CustomerTypeName.NONE, 0, 100);
		this.bodovi = 0;
	}
	


	public User(String id, boolean deleted, String firstName, String lastName, int gender, LocalDate dateOfBirth,
			String username, String password, int role, String fabrikaCokoladeId, int bodovi) {
		super();
		this.id = id;
		this.deleted = deleted;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.username = username;
		this.password = password;
		this.role = role;
		this.fabrikaCokoladeId = fabrikaCokoladeId;
		this.bodovi = bodovi;
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
}
