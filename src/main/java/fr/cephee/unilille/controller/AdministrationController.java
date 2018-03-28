package fr.cephee.unilille.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationAnnoncePersistence;
import fr.cephee.unilille.database.PublicationEventPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.database.PublicationProjectPersistence;
import fr.cephee.unilille.database.ReportPersistence;
import fr.cephee.unilille.database.SkillPersistence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Report;

@Controller
public class AdministrationController {

private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MemberPersistence datamem;
	
	@Autowired
	private PublicationPersistence datapub;
	
	@Autowired
	private PublicationEventPersistence datapubeve;
	
	@Autowired
	private PublicationAnnoncePersistence datapubann;
	
	@Autowired
	private PublicationProjectPersistence datapubpro;
	
	@Autowired
	private SkillPersistence dataski;
	
	@Autowired
	private CompetencePersistence datacom;
	
	@Autowired
	private ReportPersistence datarep;
	
	
	/**
	 * - Return admin page if member is an admin : statistiques and notifications
	 * @param login
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin")
	public String profile(Model model, HttpSession session) {
		Member member = (Member)session.getAttribute("member");
		model.addAttribute("member", member);
		if( ! member.getIsAdmin() ) {
			model.addAttribute("displaymessage", " Vous n'avez pas accès à cette section :)");
			return "errorPage";
		}
		
		long nbEvents = datapubeve.count();
		long nbAnnounces = datapubann.count();
		long nbProjects = datapubpro.count();
		model.addAttribute("nbevents", nbEvents);
		model.addAttribute("nbannounces", nbAnnounces);
		model.addAttribute("nbprojects", nbProjects);
		model.addAttribute("nbpublications", nbEvents + nbAnnounces + nbProjects);
		
		long nbMembersActivated = datamem.findByIsActivated(true).size();
		long nbMembersVisitors = datamem.findByIsActivated(false).size();
		model.addAttribute("nbmembersactivated", nbMembersActivated);
		model.addAttribute("nbmembersvisitors", nbMembersVisitors);
		model.addAttribute("nbmembers", nbMembersActivated + nbMembersVisitors);
		
		//Reports
		List<Report> reports = datarep.findAllByOrderByDateDesc();
		model.addAttribute("reports", reports);
		
		return "dashboardAdmin";
	}
	
	
		
		
		
		
}
