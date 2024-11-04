package services;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Cokolada;
import beans.CustomerType;
import beans.User;
import dao.CokoladaDAO;
import dao.UsersDAO;
import dto.LoginDTO;
import dto.LoginReturnDTO;
import dto.RegistrationDto;
import enums.CustomerTypeName;

@Path("/users")
public class UserService {

	@Context
	ServletContext ctx;

	public UserService() {
	}


	@PostConstruct
	private void init() {

		if (ctx.getAttribute("usersDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("usersDAO", new UsersDAO(contextPath));
		}
	}

	private UsersDAO getUsersDAO() {
		return (UsersDAO) ctx.getAttribute("usersDAO");
	}

	@GET
	@Path("/loggedUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getLoggedUser() {
		return getUsersDAO().getLoggedUser();
	}

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getUsers() {
		return getUsersDAO().findAll();
	}

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserById(@PathParam("id") String id) {

		System.out.println(" ID: " + id);
		UsersDAO dao = (UsersDAO) ctx.getAttribute("usersDAO");
		return dao.getById(id);
	}

	@GET
	@Path("/available")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAvailableManagers() {
		return getUsersDAO().getAvailableManagers();
	}

	// boolean is username taken Korisničko ime (jedinstveno)

	@GET
	@Path("/taken")
	@Produces(MediaType.APPLICATION_JSON)
	public Response UsernameTaken(@QueryParam("username") String username) {
		boolean isTaken = getUsersDAO().UsernameTaken(username);
		return Response.ok(isTaken).build();
	}

	@POST
	@Path("/registration")
	@Produces(MediaType.APPLICATION_JSON)
	public User register(RegistrationDto dto) {

		System.out.println(dto.toString());
		// String firstName, String lastName, int gender, LocalDate dateOfBirth,
		// String username, String password
		User user = new User(dto.getFirstName(), dto.getLastName(), dto.getGender(), dto.getDateOfBirth(),
				dto.getUsername(), dto.getPassword());
		// ispravi u getRole i namesti da se prosledi integer ako niko nije ulogovan
		// role je 0, ostalo kako je izabrano
		user.setRole(0);
		user.setBodovi(0);
		user.setTipKupca(new CustomerType(CustomerTypeName.NONE, 0.0, 100));

		return getUsersDAO().save(user);
	}

	@POST
	@Path("/menadzer/{id}/fabrika/{fabrikaId}")
	@Produces(MediaType.APPLICATION_JSON)
	public User DodeliMenadzeruFabriku(@PathParam("id") String id, @PathParam("fabrikaId") String fabrikaId) {

		User user = getUsersDAO().getById(id);
		user.setFabrikaCokoladeId(fabrikaId);
		getUsersDAO().update(id, user);
		return getUsersDAO().save(user);
	}

	@POST
	@Path("/novimenadzeriliradnik")
	@Produces(MediaType.APPLICATION_JSON)
	public User NewManager(RegistrationDto dto) {

		System.out.println(dto.toString());
		User user = new User(dto.getFirstName(), dto.getLastName(), dto.getGender(), dto.getDateOfBirth(),
				dto.getUsername(), dto.getPassword());
		// ispravi u getRole i namesti da se prosledi integer ako niko nije ulogovan
		// role je 0, ostalo kako je izabrano
		user.setRole(dto.getRole());
		user.setBodovi(0);
		user.setFabrikaCokoladeId(dto.getFabrikaCokoladeId());
		user.setTipKupca(new CustomerType(CustomerTypeName.NONE, 0.0, 0));
		return getUsersDAO().save(user);
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public LoginReturnDTO login(LoginDTO dto, @Context HttpServletRequest request) {
		System.out.println("POGODIO SAM LOGIN");
		System.out.println("OVO SAM PRIMIO " + dto.getUsername() + "" + dto.getPassword());
		LoginReturnDTO lrd = getUsersDAO().login(dto);

		if (lrd.isSuccess()) {
			System.out.println("Usao sam ovde");
			request.getSession().setAttribute("user", lrd);
		}

		System.out.println("VRACAM OVO: " + lrd.getUsername() + " " + lrd.getRole() + " " + lrd.isSuccess());
		return lrd;
	}

	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public User logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
		return getUsersDAO().logout();
	}

	@GET
	@Path("/currentUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginReturnDTO login2(@Context HttpServletRequest request) {
		// ili samo return (User) request.getSession().getAttribute("user");
		System.out.println("Dobavljanje kolacica");
		LoginReturnDTO lrd = (LoginReturnDTO) request.getSession().getAttribute("user");
		if (lrd == null) {
			return new LoginReturnDTO(null, -1, false);
		}
		System.out.println("VRACAM OVO: " + lrd.getUsername() + " " + lrd.getRole() + " " + lrd.isSuccess());
		return lrd;
	}

	@POST
	@Path("/update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User updateCokolada(@PathParam("id") String id, User product) {
		return getUsersDAO().update(id, product);
	}

	@POST
	@Path("/update/{id}/kazneni/{bodovi}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User DeductUserPoints(@PathParam("id") String id, @PathParam("bodovi") String bodovi) {
		User user = getUsersDAO().getById(id);
		user.setBodovi(user.getBodovi() - Integer.parseInt(bodovi));
		user.getTipKupca().setPointsRequired(user.getTipKupca().getPointsRequired() + Integer.parseInt(bodovi));
		return getUsersDAO().update(id, user);
	}

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsers(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName, @QueryParam("username") String username,
			@QueryParam("role") int role, @QueryParam("customerTypeName") CustomerTypeName customerTypeName) {
		List<User> users = getUsersDAO().combinedSearch(firstName, lastName, username, role, customerTypeName);
		return Response.ok(users).build();
	}

}
