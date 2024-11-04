package services;

import java.time.LocalDate;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Kupovina;
import beans.KupovinaItem;
import beans.User;
import dao.KupovinaDAO;
import dao.KupovinaItemDAO;
import dao.UsersDAO;
import enums.Status;

@Path("/kupovina")
public class KupovinaService {
	@Context
	ServletContext ctx;

	public KupovinaService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("kupovinaDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("kupovinaDAO", new KupovinaDAO(contextPath));
		}
		if (ctx.getAttribute("kupovinaItemDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("kupovinaItemDAO", new KupovinaItemDAO(contextPath));
		}
		if (ctx.getAttribute("UsersDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UsersDAO", new UsersDAO(contextPath));
		}
	}

	@GET
	@Path("/kupac/{kupacId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kupovina> getAllPurchasesForCustomer(@PathParam("kupacId") String kupacId) {
		KupovinaDAO dao = (KupovinaDAO) ctx.getAttribute("kupovinaDAO");

		Collection<Kupovina> allPurchases = dao.findAllByKupacId(kupacId);

		// Collection<Kupovina> activePurchases = allPurchases.stream().filter(kupovina
		// -> !kupovina.getStatus()==StatusKupovine.Odbijeno)
		// .collect(Collectors.toList());
		return allPurchases;
	}

	@GET
	@Path("/artikli/{kupovinaId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<KupovinaItem> sviArtikli(@PathParam("kupovinaId") String kupovinaId) {
		KupovinaItemDAO dao = (KupovinaItemDAO) ctx.getAttribute("kupovinaItemDAO");

		Collection<KupovinaItem> allPurchases = dao.findAllByKupovinaId(kupovinaId);

		// Collection<Kupovina> activePurchases = allPurchases.stream().filter(kupovina
		// -> !kupovina.isDeleted())
		// .collect(Collectors.toList());
		return allPurchases;
	}

	@GET
	@Path("/kupovine/menadzer/{menadzerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasesFabrike(@PathParam("menadzerId") String menadzerId) {
		KupovinaDAO dao = (KupovinaDAO) ctx.getAttribute("kupovinaDAO");
		UsersDAO usersDao = (UsersDAO) ctx.getAttribute("UsersDAO");
		
		User manager = usersDao.getById(menadzerId);
		  if (manager == null) {
		        return Response.status(Response.Status.NOT_FOUND)
		                .entity("Manager not found with id: " + menadzerId).build();
		    }
		  
		  Collection<Kupovina> allPurchases = dao.findAllByFabrikaId(manager.getFabrikaCokoladeId());
		  return Response.ok(allPurchases).build();
	}


	/*Kupac ima opciju da otkaže kupovinu dok je ona u statusu Obrada. Ako se odustane od
kupovine gubi se broj bodova po sledećoj formuli:
broj_izgubljenih_bodova = ukupna_cena_porudžbine/1000 * 133 * 4

.*/
	
	@POST
	@Path("/otkazi/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Kupovina Otkazi(@PathParam("id") String id, Kupovina purchase) {
		
		KupovinaDAO dao = (KupovinaDAO) ctx.getAttribute("kupovinaDAO");
		Kupovina kupovina = dao.getById(id);
		if (kupovina.getStatus() == Status.Obrada) {
			kupovina.setStatus(Status.Otkazano);
		}
		return null;
	}
	
	
	@POST
	@Path("/update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Kupovina UpdateMenadzera(@PathParam("id") String id, Kupovina purchase) {
		
		System.out.println(purchase.toString());
		KupovinaDAO dao = (KupovinaDAO) ctx.getAttribute("kupovinaDAO");
		return dao.update(id, purchase);
	}

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Kupovina getPurchaseById(@PathParam("id") String id) {
		KupovinaDAO dao = (KupovinaDAO) ctx.getAttribute("kupovinaDAO");
		return dao.getById(id);
	}



	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kupovina> searchByCenaAndDatum(@QueryParam("minCena") double minCena,
			@QueryParam("maxCena") double maxCena, @QueryParam("startDatum") String startDatumStr,
			@QueryParam("endDatum") String endDatumStr) {
		KupovinaDAO dao = (KupovinaDAO) ctx.getAttribute("kupovinaDAO");

		LocalDate startDatum = LocalDate.parse(startDatumStr);
		LocalDate endDatum = LocalDate.parse(endDatumStr);

		return dao.findAllByCenaAndDatum(minCena, maxCena, startDatum, endDatum);
	}
}
