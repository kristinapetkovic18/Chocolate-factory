package services;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import beans.Address;
import beans.Cokolada;
import beans.Fabrika;
import beans.Location;
import dao.CokoladaDAO;
import dao.FabrikaDAO;
import dao.LokacijaDAO;
import dto.FabrikaDTO;

@Path("/fabrika")
public class FabrikaService {

	@Context
	ServletContext ctx;

	public FabrikaService() {
	}

	@PostConstruct
	public void init() {
		if (ctx.getAttribute("fabrikaDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("fabrikaDAO", new FabrikaDAO(contextPath));

			if (ctx.getAttribute("cokoladaDAO") == null) {
				ctx.setAttribute("cokoladaDAO", new CokoladaDAO(contextPath));
			}
			if (ctx.getAttribute("lokacijaDAO") == null) {
				ctx.setAttribute("lokacijaDAO", new LokacijaDAO(contextPath));
			}
		}

	}

	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public FabrikaDTO getFabrikaById(@PathParam("id") String id) {
		System.out.println(" ID: " + id);
		FabrikaDAO daoFabrika = (FabrikaDAO) ctx.getAttribute("fabrikaDAO");
		CokoladaDAO daoCokolada = (CokoladaDAO) ctx.getAttribute("cokoladaDAO");

		LokacijaDAO locationDAO = (LokacijaDAO) ctx.getAttribute("lokacijaDAO");

		ArrayList<Cokolada> cokolade = daoCokolada.getByFabrikaId(id).stream().filter(c -> !c.isDeleted())
				.collect(Collectors.toCollection(ArrayList::new));
		Fabrika fabrika = daoFabrika.findFabrika(id);
		//fabrika.setStatus(isOpened(fabrika));

		FabrikaDTO fabrikaDTO = new FabrikaDTO(fabrika.getId(), fabrika.getIme(), fabrika.getLokacija(),
				fabrika.getProsecnaOcena(), fabrika.getSlika(), fabrika.isStatus(), fabrika.getRadnoVreme(), cokolade);

		Location lokacija = locationDAO.getById(fabrikaDTO.getId());
		fabrikaDTO.setLocation(lokacija);
		return fabrikaDTO;
	}
	
	//search searchFabrike za sve kriterijume osim cokolade; ako trazis po imenu cokolade onda pozovi prvo cokoladaDAO
	//da nadjes koji fabrikaId dobijes tako, i onda nappravi kolekciju pronadjenih fabrika

	//// http://localhost:8081/WebShopAppREST/rest/fabrika/add
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Fabrika add(FabrikaDTO product) {
		FabrikaDAO dao = (FabrikaDAO) ctx.getAttribute("fabrikaDAO");
		LokacijaDAO locationDAO = (LokacijaDAO) ctx.getAttribute("lokacijaDAO");

		System.out.println(product.toString());
		Fabrika nova = dao.save(product);
		Location novaLokacija = new Location(nova.getId(), new Address(product.getStreet(), product.getCity(), product.getPostalNumber()),0, 0);
		
		locationDAO.save(novaLokacija);
		return nova;
	}
	
	
	// http://localhost:8081/WebShopAppREST/rest/fabrika/sve
	@GET
	@Path("/sve")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FabrikaDTO> sveFabrike() {
		FabrikaDAO dao = (FabrikaDAO) ctx.getAttribute("fabrikaDAO");
		LokacijaDAO locationDAO = (LokacijaDAO) ctx.getAttribute("lokacijaDAO");
	
		List<Fabrika> aktivnePrvo = dao.fabrikeOtvorenePrvo();

		ArrayList<FabrikaDTO> fabrikaDTOList = new ArrayList<FabrikaDTO>();

		for (Fabrika aktivna : aktivnePrvo) {
			FabrikaDTO fabrikaDTO = new FabrikaDTO(aktivna.getId(), aktivna.getIme(), aktivna.getProsecnaOcena(),
					aktivna.getSlika(), aktivna.isStatus(), aktivna.getRadnoVreme());

			Location lokacija = locationDAO.getById(fabrikaDTO.getId());
			fabrikaDTO.setLocation(lokacija);

			fabrikaDTOList.add(fabrikaDTO);
		}

		return fabrikaDTOList;
	}

	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FabrikaDTO> searchFabrike(
	        @QueryParam("ime") String ime,
	        @QueryParam("min") double min,
	        @QueryParam("max") double max,
	        @QueryParam("lokacija") String lokacija) {

	    FabrikaDAO fabrikaDAO = (FabrikaDAO) ctx.getAttribute("fabrikaDAO");
	    LokacijaDAO lokacijaDAO = (LokacijaDAO) ctx.getAttribute("lokacijaDAO");


	    List<Fabrika> filteredFabrike = fabrikaDAO.searchFabrike(ime, min, max);
	    List<String> fabrikeIds = lokacijaDAO.searchFabrike(lokacija);
	    Collection<Fabrika> filteredFabrikeBySearch = fabrikaDAO.findAllByIds(fabrikeIds);

	    Map<String, Fabrika> uniqueFabrikeMap = new HashMap<>();
	    filteredFabrike.forEach(fabrika -> uniqueFabrikeMap.put(fabrika.getId(), fabrika));
	    filteredFabrikeBySearch.forEach(fabrika -> uniqueFabrikeMap.put(fabrika.getId(), fabrika));


	    List<FabrikaDTO> fabrikaDTOList = uniqueFabrikeMap.values().stream()
	            .map(fabrika -> {
	                FabrikaDTO dto = new FabrikaDTO(fabrika.getId(), fabrika.getIme(), fabrika.getProsecnaOcena(),
	                        fabrika.getSlika(), fabrika.isStatus(), fabrika.getRadnoVreme());

	                Location location = lokacijaDAO.getById(fabrika.getId());
	                dto.setLocation(location);

	                return dto;
	            })
	            .collect(Collectors.toList());

	    return fabrikaDTOList;
	}

}
