package fr.cephee.unilille.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.SkillPersistence;
import fr.cephee.unilille.exceptions.CompetenceTitleException;
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
			Authentication auth) {
		
		//If no login is specified, return session profile
		if(login == null)
			return this.profile(
					((Member)auth.getPrincipal()).getLogin(), 
					model, 
					auth);
		
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)auth.getPrincipal()).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		
		
		model.addAttribute("member", member);
		
		//If it is my Profile
		if( itIsMemberSession ) {
			//If profile is not activated - we suggest to activate
			if( member.getActivated() == false ) {
				model.addAttribute("profileActivationForm", new ProfileActivationForm());
				model.addAttribute("DESCRIPTION_SIZE_MAX", Controls.DESCRIPTION_SIZE_MAX);
				model.addAttribute("DESCRIPTION_SIZE_MIN", Controls.DESCRIPTION_SIZE_MIN);
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
	
	
	
	
	@RequestMapping(value = "/editprofile", method = RequestMethod.GET)
	public String editProfile(
			Model model,
			Authentication auth) {
		Member member = (Member)auth.getPrincipal();
		ProfileForm profileForm = new ProfileForm();
		profileForm.setDescription(member.getDescription());
		profileForm.setEmail(member.getEmail());
		List<Competence> listcompetence = datacom.findAll();
		model.addAttribute("competenceList", listcompetence);
		model.addAttribute("member", member);
		model.addAttribute("profileForm", profileForm);
		model.addAttribute("profileSkillForm", new ProfileSkillForm());
		model.addAttribute("DESCRIPTION_SIZE_MAX", Controls.DESCRIPTION_SIZE_MAX);
		model.addAttribute("DESCRIPTION_SIZE_MIN", Controls.DESCRIPTION_SIZE_MIN);
		return "profileEdit";
	}
	
	/**
	 * Register email and description
	 * @param login
	 * @param profileForm
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/editprofileregisterinfos", method = RequestMethod.POST)
	public String registerProfileEdition(
			@RequestParam(value="login", required=false) String login,
			@ModelAttribute("profileForm") ProfileForm profileForm,
			Model model,
			Authentication auth) {
		
		//If no login is specified, recall for session profile
		if(login == null)
			return this.registerProfileEdition(
					((Member)auth.getPrincipal()).getLogin(),
					profileForm,
					model, 
					auth);
		
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)auth.getPrincipal()).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		

		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || ((Member)auth.getPrincipal()).getIsAdmin() ) {
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
		//session.setAttribute("member", member);
		model.addAttribute("member", member);
		//We return member profile
		return this.profile(login, model, auth);
	}
	
	/**
	 * Register a skill for member
	 *  If competence doesn't exist in DB, creates competence
	 * @param login
	 * @param profileSkillForm
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/editprofileregisterskill", method = RequestMethod.POST)
	public String registerProfileSkill(
			@RequestParam(value="login", required=false) String login,
			@ModelAttribute("profileSkillForm") ProfileSkillForm profileSkillForm,
			Model model,
			Authentication auth) {
		
		//If no login is specified, recall for session profile (need to do that because we handle admin situation)
		if(login == null)
			return this.registerProfileSkill(
					((Member)auth.getPrincipal()).getLogin(),
					profileSkillForm,
					model, 
					auth);
				
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)auth.getPrincipal()).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		

		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || ((Member)auth.getPrincipal()).getIsAdmin() ) {
			try {
				Controls.checkCompetenceTitle(profileSkillForm.getCompetenceTitle());
			
				Competence competence = datacom.findByTitle(profileSkillForm.getCompetenceTitle().toLowerCase());
				if(competence == null) {
					competence = new Competence();
					competence.setTitle(profileSkillForm.getCompetenceTitle().toLowerCase());
					datacom.save(competence);
				}
				
				Skill skill = new Skill();
				skill.setCompetence(competence);
				skill.setLevel(profileSkillForm.getLevel());
				skill.setMember(member);
				dataski.save(skill);
				//obligé de faire les add et save dans les 2 sens Soso ???
				member.addSkill(skill);
				datamem.save(member);
				
			} catch (CompetenceTitleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//We return member profile
		return this.profile(login, model, auth);
	}
	
	
	@RequestMapping(value = "/activateprofileregister", method = RequestMethod.POST)
	public String registerActivateProfile (
			@ModelAttribute("profileActivationForm") ProfileActivationForm profileActivationForm,
			Model model,
			Authentication auth) {
		
		Member member = ((Member)auth.getPrincipal());
		
		//If is not activated - process
		if(!member.getActivated()) {
			try {
				Controls.checkEmail(profileActivationForm.getEmail());
				member.setEmail(profileActivationForm.getEmail());
				Controls.checkDescription(profileActivationForm.getDescription());
				member.setDescription(profileActivationForm.getDescription());
				member.setActived(true);
				datamem.save(member);
			} catch (EmailFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DescriptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return this.profile(member.getLogin(), model, auth);
	}
}
