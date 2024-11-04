package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Cokolada;
import beans.Fabrika;
import dao.CokoladaDAO;
import dao.FabrikaDAO;
import dto.FabrikaDTO;

@Path("/cokolada")
public class CokoladaService {
	@Context
	ServletContext ctx;

	public CokoladaService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("cokoladaDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("cokoladaDAO", new CokoladaDAO(contextPath));
		}
	}

	// http://localhost:8081/WebShopAppREST/rest/cokolada
	@GET
	@Path("/sve")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Cokolada> getProducts() {
		CokoladaDAO dao = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");

		Collection<Cokolada> allProducts = dao.findAll();

		Collection<Cokolada> activeProducts = allProducts.stream().filter(cokolada -> !cokolada.isDeleted())
				.collect(Collectors.toList());
		return activeProducts;
	}

	@GET
	@Path("/svefabrike{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Cokolada> getProductsByFabrikaId(@PathParam("id") String id) {
		CokoladaDAO dao = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");

		Collection<Cokolada> allProducts = dao.getByFabrikaId(id);

		Collection<Cokolada> activeProducts = allProducts.stream().filter(cokolada -> !cokolada.isDeleted())
				.collect(Collectors.toList());
		return activeProducts;
	}

	@POST
	@Path("/update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cokolada updateCokolada(@PathParam("id") String id, Cokolada product) {
		CokoladaDAO dao = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");
		return dao.update(id, product);
	}

	@DELETE
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteCokolada(@PathParam("id") String id) {
		CokoladaDAO dao = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");
		dao.delete(id);
	}

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Cokolada getCokoladaById(@PathParam("id") String id) {
		System.out.println(" ID: " + id);

		CokoladaDAO daoCokolada = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");
		return daoCokolada.getById(id);
	}

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Cokolada add(Cokolada product) {

		System.out.println(product.toString());
		CokoladaDAO dao = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");
		return dao.save(product);
	}
	
	/*Radnicima je dostupna funkcionalnost izmene broja količine određene čokolade u
fabrici u kojoj je zaposlen. Radniku omogućiti da se klikom na neku čokoladu iz njegove
fabrike, pojavi opcija “Izmeni količinu”. Nakon izmene količine, ta izmena je odmah
vidljiva i čokolade su dostupne korisnicima za kupovinu.*/

}
