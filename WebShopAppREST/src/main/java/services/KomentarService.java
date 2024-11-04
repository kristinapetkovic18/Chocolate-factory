package services;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Fabrika;
import beans.Komentar;
import beans.User;
import dao.FabrikaDAO;
import dao.KomentarDAO;
import dao.UsersDAO;
import dto.KomentarDTO;
import enums.Status;

@Path("/komentari")
public class KomentarService {
	
    @Context
    ServletContext ctx;

    public KomentarService() {
    }

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("komentarDAO") == null) {
            ctx.setAttribute("komentarDAO", new KomentarDAO());
        }
        if (ctx.getAttribute("UsersDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UsersDAO", new UsersDAO(contextPath));
		}
    	if (ctx.getAttribute("fabrikaDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("fabrikaDAO", new FabrikaDAO(contextPath));
			}

    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Komentar> getAllComments() {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        return dao.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Komentar getCommentById(@PathParam("id") String id) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        return dao.getById(id);
    }
    
    @GET
    @Path("/fabrika/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public  Collection<Komentar>  getCommentByFabrikaId(@PathParam("id") String id) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        return dao.getByFabrikaId(id);
    }
    
    @GET
    @Path("/fabrika/odobreni/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public  Collection<Komentar>  getOdobreneCommentByFabrikaId(@PathParam("id") String id) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        return dao.getByFabrikaIdOdobrene(id);
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Komentar addComment(KomentarDTO dto) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        FabrikaDAO daoFabrika = (FabrikaDAO) ctx.getAttribute("fabrikaDAO");
        UsersDAO usersDao = (UsersDAO) ctx.getAttribute("UsersDAO");
		
        
        
        Komentar komentar = new Komentar(dto);
        
        
		User user = usersDao.getById(dto.getKupacId());
		komentar.setKupacUsername(user.getUsername());
		
		Fabrika fabrika = daoFabrika.findFabrika(dto.getFabrikaId());

		 Collection<Komentar> approvedComments = getCommentByFabrikaId(fabrika.getId());
		
		    double totalRating = 0;
		    int commentCount = 0;

		    for (Komentar comment : approvedComments) {
		        totalRating += comment.getOcena(); 
		        commentCount++;
		    }

		    if (commentCount > 0) {
		        double averageRating = totalRating / commentCount;
		        fabrika.setProsecnaOcena(averageRating);
		    } else {
		        fabrika.setProsecnaOcena(0);
		    }


		    daoFabrika.update(fabrika.getId(), fabrika);

        return dao.save(komentar);
        
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Komentar updateComment(@PathParam("id") String id, Komentar komentar) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        return dao.update(id, komentar);
    }

    

    @PUT
    @Path("/odobri/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Komentar Odobri(@PathParam("id") String id) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        Komentar kom = dao.getById(id);
        kom.setStatus(Status.Odobreno);
        
        return dao.update(id, kom);
    }


    @PUT
    @Path("/odbij/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Komentar Odbij(@PathParam("id") String id) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        Komentar kom = dao.getById(id);
        kom.setStatus(Status.Odbijeno);
        
        return dao.update(id, kom);
    }


    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Komentar> searchByStatus(@QueryParam("status") String statusStr) {
        KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
        Status status = Status.valueOf(statusStr);
        return dao.findAll().stream()
                .filter(komentar -> komentar.getStatus() == status)
                .collect(Collectors.toList());
    }
}
