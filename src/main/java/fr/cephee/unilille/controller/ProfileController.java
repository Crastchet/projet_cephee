package fr.cephee.unilille.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.SkillPersistence;
import fr.cephee.unilille.exceptions.DateFormatException;
import fr.cephee.unilille.exceptions.DescriptionException;
import fr.cephee.unilille.exceptions.EmailFormatException;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.ProfileActivationForm;
import fr.cephee.unilille.model.ProfileForm;
import fr.cephee.unilille.model.ProfileSkillForm;
import fr.cephee.unilille.model.Skill;
import fr.cephee.unilille.utils.Controls;

@Controller
public class ProfileController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MemberPersistence datamem;
	
	@Autowired
	private SkillPersistence dataski;
	
	@Autowired
	private CompetencePersistence datacom;
	
	
	/**
	 * - if /profile - return session profile
	 * - if /profile?login=mylogin - return mylogin profile
	 * @param login
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/profile")
	public String profile(
			@RequestParam(value="login", required=false) String login,		//The member login who asks to see his profile
			Model model,
			HttpSession session) {
		
		//If no login is specified, return session profile
		if(login == null)
			return this.profile(
					((Member)session.getAttribute("member")).getLogin(), 
					model, 
					session);
		
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)session.getAttribute("member")).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		
		
		model.addAttribute("member", member);
		
		//If it is my Profile
		if( itIsMemberSession ) {
			//If profile is not activated - we suggest to activate
			if( member.getActivated() == false ) {
				model.addAttribute("profileActivationForm", new ProfileActivationForm());
				return "profilePersonnal-NotActivated";
			}
			//If it is activated - we don't suggest to activate
			else {
				this.addProfilePublications(member, model);
				this.addProfileSkills(member, model);
				return "profilePersonnal";
			}
		}
		
		//If it is not my Profile
		return "profileMember";
	}
	
	
	private void addProfilePublications(Member member, Model model) {
//		model.addAttribute("publications", datapub.findByAuthor(member); //en parler à Sofian
		model.addAttribute("publications", member.getListpublication());
	}
	
	private void addProfileSkills(Member member, Model model) {
		model.addAttribute("skills", member.getSkills());
		
		model.addAttribute("competences", datacom.findAll());
		model.addAttribute("profileSkillForm", new ProfileSkillForm()); //let us add skills on the same page
	}
	
	
	@RequestMapping(value = "/skillprofileregister", method = RequestMethod.POST)
	public String registerProfileSkill(
			@RequestParam(value="login", required=false) String login,
			@ModelAttribute("profileSkillForm") ProfileSkillForm profileSkillForm,
			Model model,
			HttpSession session) {
		
		//If no login is specified, recall for session profile (need to do that because we handle admin situation)
		if(login == null)
			return this.registerProfileSkill(
					((Member)session.getAttribute("member")).getLogin(),
					profileSkillForm,
					model, 
					session);
				
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)session.getAttribute("member")).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		

		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || ((Member)session.getAttribute("member")).getIsAdmin() ) {
			Competence competence = new Competence();
			competence.setTitle(profileSkillForm.getCompetenceTitle());
			datacom.save(competence);
			Skill skill = new Skill();
			skill.setCompetence(competence);
			skill.setLevel(profileSkillForm.getLevel());
			skill.setMember(member);
			dataski.save(skill);
			//obligé de faire les add et save dans les 2 sens Soso ???
			member.addSkill(skill);
			datamem.save(member);
		}
		
		//We return member profile
		return this.profile(login, model, session);
	}
	
	
	@RequestMapping(value = "/editprofile", method = RequestMethod.GET)
	public String editProfile(
			Model model,
			HttpSession session) {
		Member member = (Member)session.getAttribute("member");
		model.addAttribute("member", member);
		ProfileForm profileForm = new ProfileForm();
		profileForm.setDescription(member.getDescription());
		profileForm.setEmail(member.getEmail());
		model.addAttribute("profileForm", profileForm);
		return "profileEdit";
	}
	
	@RequestMapping(value = "/editprofileregister", method = RequestMethod.POST)
	public String registerProfileEdition(
			@RequestParam(value="login", required=false) String login,
			@ModelAttribute("profileForm") ProfileForm profileForm,
			Model model,
			HttpSession session) {
		
		//If no login is specified, recall for session profile
		if(login == null)
			return this.registerProfileEdition(
					((Member)session.getAttribute("member")).getLogin(),
					profileForm,
					model, 
					session);
		
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)session.getAttribute("member")).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		

		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || ((Member)session.getAttribute("member")).getIsAdmin() ) {
			try {
				Controls.checkEmail(profileForm.getEmail());
				member.setEmail(profileForm.getEmail());
				Controls.checkDescription(profileForm.getDescription());
				member.setDescription(profileForm.getDescription());
				
				datamem.save(member);
			} catch (EmailFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DescriptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//We return member profile
		return this.profile(login, model, session);
	}
	
	
	
	@RequestMapping(value = "/activateprofileregister", method = RequestMethod.POST)
	public String registerActivateProfile (
			@ModelAttribute("profileActivationForm") ProfileActivationForm profileActivationForm,
			Model model,
			HttpSession session) {
		
		Member member = ((Member)session.getAttribute("member"));
		
		//If is not activated - process
		if(!member.getActivated()) {
			try {
				Controls.checkEmail(profileActivationForm.getEmail());
				member.setEmail(profileActivationForm.getEmail());
				member.setDescription(profileActivationForm.getDescription());
				member.setActived(true);
				datamem.save(member);
			} catch (EmailFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return this.profile(member.getLogin(), model, session);
	}
}
