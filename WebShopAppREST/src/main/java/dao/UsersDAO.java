package dao;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import beans.User;
import dto.LoginDTO;
import dto.LoginReturnDTO;
import enums.CustomerTypeName;
import beans.CustomerType;

public class UsersDAO {

	private static final Logger logger = Logger.getLogger(UsersDAO.class.getName());
	private Map<String, User> users = new HashMap<>();
	private String filePath;

	private User loggedUser;

	public UsersDAO(String contextPath) {
		this.filePath = "C:/Users/user_name/Desktop/web/users.txt";
		loadUsers();
	}

	public Collection<User> findAll() {
		return users.values();
	}

	public User find(String username, String password) {
		if (!users.containsKey(username)) {
			return null;
		}
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}

	public User getByUsername(String u) {
		for (User searchUser : users.values()) {
			System.out.println("Checking username: " + searchUser.getUsername());
			if (searchUser.getUsername().equals(u)) {
				System.out.println("Found matching user: " + searchUser.getUsername());
				return searchUser;
			}
		}
		System.out.println("No user found with username: " + u);
		return null;
	}

	public User getById(String id) {
		return users.get(id);
	}

	public User save(User user) {
		user.setId(generateNewId());
		users.put(user.getId(), user);
		saveToFile(user);
		return user;
	}

	private String generateNewId() {
		return String.format("%010d", users.size() + 1);
	}

	private void saveToFile(User user) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.write(formatUserForFile(user));
			writer.newLine();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to save user to file: " + user, e);
		}
	}

	private void loadUsers() {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty() || line.startsWith("#"))
					continue;
				try {
					User user = parseUserFromLine(line);
					users.put(user.getId(), user);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Failed to parse user from line: " + line, e);
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to load users from file", e);
		}
	}

	public User update(String id, User updatedUser) {
		if (users.containsKey(id)) {
			updatedUser.setId(id);
			users.put(id, updatedUser);
			rewriteFile();
			this.loggedUser = updatedUser;
			return updatedUser;
		}
		return null;
	}

	public void delete(String id) {
		if (users.remove(id) != null) {
			rewriteFile();
		}
	}

	private void rewriteFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
			for (User user : users.values()) {
				writer.write(formatUserForFile(user));
				writer.newLine();
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to rewrite users file", e);
		}
	}


	private String formatUserForFile(User user) {
		return String.join(";", user.getId(), String.valueOf(user.isDeleted()), user.getFirstName(), user.getLastName(),
				String.valueOf(user.getGender()), user.getUsername(), user.getPassword(),
				String.valueOf(user.getRole()), user.getFabrikaCokoladeId() == null ? "" : user.getFabrikaCokoladeId(),
				formatCustomerType(user.getTipKupca()), String.valueOf(user.getBodovi()),
				user.getDateOfBirth() == null ? "" : user.getDateOfBirth().toString()

		);
	}

	private String formatCustomerType(CustomerType customerType) {
		if (customerType == null)
			return "";
		return String.join(";", customerType.getName().name(), String.valueOf(customerType.getDiscount()),
				String.valueOf(customerType.getPointsRequired()));
	}

	private User parseUserFromLine(String line) {
		String[] parts = line.split(";");
		if (parts.length < 13) {
			logger.log(Level.SEVERE, "Insufficient data to parse User from line: " + line);
			throw new IllegalArgumentException("Insufficient data to parse User from line: " + line);
		}

		LocalDate dateOfBirth = null;
		if (!parts[13].isEmpty()) {
			try {
				dateOfBirth = LocalDate.parse(parts[13]);
			} catch (DateTimeParseException e) {
				logger.log(Level.SEVERE, "Error parsing date from line: " + line, e);
				throw new IllegalArgumentException("Error parsing date from line: " + line, e);
			}
		}

		CustomerType customerType = parts.length > 10 && !parts[9].isEmpty()
				? parseCustomerType(parts[9], parts[10], parts[11])
				: null;

		return new User(parts[0], Boolean.parseBoolean(parts[1]), parts[2], parts[3], Integer.parseInt(parts[4]),
				parts[5], parts[6], Integer.parseInt(parts[7]), parts[8].isEmpty() ? null : parts[8], customerType,
				Integer.parseInt(parts[12]), dateOfBirth);
	}

	private CustomerType parseCustomerType(String name, String discount, String pointsRequired) {
		try {
			CustomerTypeName customerTypeName = CustomerTypeName.valueOf(name);
			double discountValue = Double.parseDouble(discount);
			int pointsRequiredValue = Integer.parseInt(pointsRequired);
			return new CustomerType(customerTypeName, discountValue, pointsRequiredValue);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE,
					"Error parsing customer type data: " + name + "," + discount + "," + pointsRequired, e);
			throw new IllegalArgumentException(
					"Error parsing customer type data: " + name + "," + discount + "," + pointsRequired, e);
		}
	}


	public boolean UsernameTaken(String username) {
		boolean usernameTaken = false;
		for (User searchUser : users.values()) {
			if (searchUser.getUsername().equals(username)) {
				usernameTaken = true;
			}
		}

		return usernameTaken;
	}

	public Collection<User> getAvailableManagers() {

		return users.values().stream().filter(user -> {
			return 2 == user.getRole() && "0".equals(user.getFabrikaCokoladeId());
		}).collect(Collectors.toList());

	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public ArrayList<User> getUsersByRole(int role) {

		ArrayList<User> returnList = new ArrayList<>();
		for (User searchUser : users.values()) {
			if (searchUser.roleMatches(role)) {
				returnList.add(searchUser);
			}
		}
		return returnList;
	}

	public LoginReturnDTO login(LoginDTO dto) {
		System.out
				.println("OVO JE PRIMLJENO U FUNKCIJI login u UsersDAO" + dto.getUsername() + " " + dto.getPassword());

		if (users == null) {
			System.out.println("USERI SU NULA");
		}
		User user = getByUsername(dto.getUsername());
		if (user != null) {
			System.out.println("pronadjeni user id:" + user.getId());
		}
		if (user == null || !user.passwordMatches(dto))
			return new LoginReturnDTO(null, -1, false);
		loggedUser = user;
		return new LoginReturnDTO(user.getUsername(), user.getId(), user.getRole(), true);
	}

	public static String hashPassword(String password) {
		return Integer.toString(password.hashCode());
	}

	public User logout() {
		User returnUser = getLoggedUser();
		this.loggedUser = null;
		return returnUser;
	}

	public List<User> combinedSearch(String firstName, String lastName, String username, int role,
			CustomerTypeName customerTypeName) {

		return users.values().stream().filter(
				user -> (firstName == null || user.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
						&& (lastName == null || user.getLastName().toLowerCase().contains(lastName.toLowerCase()))
						&& (username == null || user.getUsername().toLowerCase().contains(username.toLowerCase()))
						&& (role == -1 || user.getRole() == role) && (customerTypeName == null
								|| user.getTipKupca() == null || user.getTipKupca().getName() == customerTypeName))
				.collect(Collectors.toList());
	}

}
