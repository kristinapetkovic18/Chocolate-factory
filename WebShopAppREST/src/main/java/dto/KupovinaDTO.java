package dto;

import java.time.LocalDate;

import enums.Status;

public class KupovinaDTO {
	//grupisi na frontu 
//	 po kupovina id
//nek bude u jendoj kupovini svi skupljeni artikli
	private String id; //10 karaktera
	
	private String kupacId;
	private String imeIprezimeKupca;
	//posle kupovine obavljene smanjiti kolicinu cokolada u fabrici prvo proveriti je  l ih ima uopste
	private double cena;
	private LocalDate vremeKupovine; //treba i datum i vreme
	private Status status;
	private int statusKupovine;
	
	private String kupovinaItemId; 
	private String cokoladaid;
	private int kolicina;
	private String fabrikaId;
	private String fabrikaName;
	
}
