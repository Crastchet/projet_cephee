package fr.cephee.unilille.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cephee.unilille.database.CategoryPersistence;
import fr.cephee.unilille.database.MemberInterestPersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.ParticipantDataPersistence;
import fr.cephee.unilille.database.PublicationEventPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.MemberInterest;
import fr.cephee.unilille.model.Participantdata;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationEvent;


@Controller
public class LoginController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParticipantDataPersistence dataparticipant;

	@Autowired
	private MemberInterestPersistence datainterest;

	@Autowired
	private CategoryPersistence datacat;

	@Autowired
	private MemberPersistence datamem;

	@Autowired
	Validator validator;

	@Autowired
	private PublicationPersistence datapub;

	@Autowired
	private PublicationEventPersistence dataevent;

	@RequestMapping(value = {"/", "/home"})
	public String returnToHome(Model model, HttpSession session, Authentication auth)
	{	
		//SI LA PERSONNE SE CONNECTE
		if(session.getAttribute("member") == null) {
			Member signed = (Member) auth.getPrincipal();		//membre lille1 qui se connecte
			String username = signed.getUsername();				//on récupère son username du CAS (prenom.nom)
			Member member = datamem.findByUsername(username);	//on check si on a déjà une trace de lui en base
			if( member == null ) {								//s'il n'existe pas en base #premiereFois
				datamem.save(signed);								//on le save (ça lui crée un id)
				member = datamem.findByUsername(username);			//on le récupère (il a maintenant un id)
			}
			session.setAttribute("member", member);
		}
		model.addAttribute("member", session.getAttribute("member"));
		
		// !!!!!!!!!!!!!! PARTIE DE SOFIAN !!!!!!!!!!!!!!
		Member member = (Member) session.getAttribute("member");
		if (datainterest.findByMember(member) == null)
		{
			List<Category> cat = datacat.findAll();
			MemberInterest meminterest = new MemberInterest(cat, member);
			datainterest.save(meminterest);
		}

		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDescByAuthorisedTrue(member.getId());
		model.addAttribute("listlasttenpub", tenLastPub);


		List<List<Object>> lpubtotal = new ArrayList<>();

		MemberInterest memInt = datainterest.findByMember(member);


		for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
			if (entry.getValue() > 0)
				lpubtotal.add(datapub.findTop20FiltredPublicationByCategoryandDateCreation(member.getId(), entry.getKey().getId()));
			
		List<Publication> pubfiltred = new ArrayList<Publication>();
		for (List<Object> lpub : lpubtotal)
		{			
			Iterator<Object> itr = lpub.iterator();
			while(itr.hasNext()){
				Object[] obj = (Object[]) itr.next();
				for (Object ob : obj)
				{
					if (ob instanceof Publication)
					{
						Publication p = (Publication) ob;
						//System.out.println("publication is : " + p.getTitle() + "  " + p.getClass());
						if (!pubfiltred.contains(p))
							pubfiltred.add(p);
					}
				}
			}
		}

		ArrayList<Publication> finalFiltredPub = new ArrayList<Publication>();
		for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
		{
			int i = 0;
			for (Publication p : pubfiltred)
			{
				for (Category c : p.getCategory())
					if (memInt.getInterests().containsKey(c))
					{
						if (!finalFiltredPub.contains(p))
							finalFiltredPub.add(p);
						i++;
					}
				if (i == entry.getValue())
					break;
			}
		}

		/*for (Publication p : finalFiltredPub)
		{
			log.info("p  : " + p.getTitle());
			for (Category c : p.getCategory())
			{
				log.info("cat = " + c.getTitle());
			}
		}*/


		//partie pour les evenements auquels on participe
		List<PublicationEvent> publiuser = (List<PublicationEvent>) dataevent.findAll();
		List<PublicationEvent> publiUserParticipate = new ArrayList<>();
		List<PublicationEvent> publiUserHasParticipated = new ArrayList<>();
		Date d = Calendar.getInstance().getTime();
		for (PublicationEvent p : publiuser)
		{
				Participantdata data = dataparticipant.findByMemByPubli(member.getId(), p.getId());			
				if (data != null)
					if (p.getStartevent().after(d))
					{
						//System.out.println("particpe :  " + p.getTitle());
						publiUserParticipate.add((PublicationEvent) p);	
					}
					else
					{
						//System.out.println("has particped :  " + p.getTitle());
						publiUserHasParticipated.add((PublicationEvent) p);	
					}
		}
		
        Collections.sort(publiUserParticipate, new Comparator<PublicationEvent>(){
        	 
            @Override
            public int compare(PublicationEvent o1, PublicationEvent o2) {
                return o2.getStartevent().compareTo(o1.getStartevent());
            }
        });
        Collections.sort(publiUserHasParticipated, new Comparator<PublicationEvent>(){
       	 
            @Override
            public int compare(PublicationEvent o1, PublicationEvent o2) {
                return o2.getStartevent().compareTo(o1.getStartevent());
            }
        });
		model.addAttribute("listparticipateuser", publiUserParticipate);
		model.addAttribute("listhasparticipateduser", publiUserHasParticipated);
		model.addAttribute("listlasttenpub", tenLastPub);
		model.addAttribute("finalFiltredPub", pubfiltred);

		return "home";
	}
	

	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session, Authentication auth) {
		session.removeAttribute("member");
		session.setMaxInactiveInterval(1);
		auth.setAuthenticated(false);
		return "logout";
	}

}
