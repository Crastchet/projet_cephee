package fr.cephee.unilille.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import fr.cephee.unilille.database.MemberInterestPersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.NotificationPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.database.SkillPersistence;
import fr.cephee.unilille.exceptions.CompetenceTitleException;
import fr.cephee.unilille.exceptions.DescriptionException;
import fr.cephee.unilille.exceptions.DisplaynameFormatException;
import fr.cephee.unilille.exceptions.EmailFormatException;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.MemberInterest;
import fr.cephee.unilille.model.Notification;
import fr.cephee.unilille.model.ProfileActivationForm;
import fr.cephee.unilille.model.ProfileForm;
import fr.cephee.unilille.model.ProfileSkillForm;
import fr.cephee.unilille.model.Publication;
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
	private MemberInterestPersistence datainterest;
		
	@Autowired
	private CompetencePersistence datacom;
	
	@Autowired
	private NotificationPersistence datanotif;
	
	@Autowired
	private PublicationPersistence datapubli;
	
	
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
			@RequestParam(value="displayname", required=false) String displayname,		//The member login who asks to see his profile
			Model model,
			HttpSession session) {
		
		//If no login is specified, return session profile
		if(displayname == null)
			return this.profile(
				((Member)session.getAttribute("member")).getDisplayname(), 
				model, 
				session);
		
		//Get member we visiting
		Member member = datamem.findByDisplayname(displayname);
		if(member == null)
			return this.profile(
				((Member)session.getAttribute("member")).getDisplayname(), 
				model, 
				session);
				
		//Add session member to model
		model.addAttribute("member", session.getAttribute("member"));
		
		//Add member we visiting to model 
		model.addAttribute("memberprofile", member);
		//Search if we are on own profile or not
		boolean itIsMemberSession = displayname.equals( ((Member)session.getAttribute("member")).getDisplayname() );
		//Help profile menu to decide which word to display (personnal or not)
		model.addAttribute("membersession", itIsMemberSession);
		
		//If it is my Profile
		if( itIsMemberSession ) {
			//If profile is not activated - we suggest to activate
			if( member.getActivated() == false ) {
				model.addAttribute("profileActivationForm", new ProfileActivationForm());
				model.addAttribute("DESCRIPTION_SIZE_MAX", Controls.DESCRIPTION_SIZE_MAX);
				model.addAttribute("DESCRIPTION_SIZE_MIN", Controls.DESCRIPTION_SIZE_MIN);
				return "profilePersonnal-NotActivated";
			}
			//If it is activated - we don't suggest to activate and we load data
			else {
				this.addProfileSkills(member, model);
				this.addProfilePublications(member, model);
				this.addProfileNotifications(member, model);
				this.addSearchInterest(member, model);
				return "profilePersonnal";
			}
		}
		
		//If it is not my Profile
		else {
			//If profile is not activated, it's because he typed url manually
			if( member.getActivated() == false ) //not supposed to get here so we don't add any specific message
				return "errorPage";
			//If profile is activated
			else {
				this.addProfilePublications(member, model);
				this.addProfileSkills(member, model);
				return "profileMember";
			}
		}
	}
	
	private void addSearchInterest(Member member, Model model)
	{

		MemberInterest memint = datainterest.findByMember(member);
		Map<Category, Integer> interests = memint.getInterests();
		model.addAttribute("interests", interests);
	}
	
	private void addProfilePublications(Member member, Model model) {
		List<Publication> publications = member.getListpublication();
		Collections.reverse(publications);
		model.addAttribute("publications", publications);
	}
	
	private void addProfileSkills(Member member, Model model) {
		model.addAttribute("skills", member.getSkills());
		
		model.addAttribute("competences", datacom.findAll());
		model.addAttribute("profileSkillForm", new ProfileSkillForm()); //let us add skills on the same page
	}
	
	private void addProfileNotifications(Member member, Model model) {
		List<Notification> notifs = datanotif.findByMemberTargeted(member);
		model.addAttribute("notifications", notifs);
	}
	
	
	@RequestMapping("/deletenotification")
	public String deleteNotif(Integer notification, Model model, HttpSession session)
	{
						
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("membersession", true);
		model.addAttribute("memberprofile", member);
		model.addAttribute("member", session.getAttribute("member"));
		
		datanotif.delete(notification);
		List<Notification> notifs = datanotif.findByMemberTargeted(member);
		model.addAttribute("notifications", notifs);
		return "profilePersonnal";
	}
	
	@RequestMapping(value = "/editprofile", method = RequestMethod.GET)
	public String editProfile(
			@RequestParam(value="displayname", required=false) String displayname,
			Model model,
			HttpSession session) {
		
		//If no login is specified, recall for session profile
		if(displayname == null)
			return this.editProfile(
				((Member)session.getAttribute("member")).getDisplayname(),
				model, 
				session);
		
		//Get member session
		Member member = (Member)session.getAttribute("member");
		//Add member session to model
		model.addAttribute("member", member);

		//Get member we editing
		Member memberProfile = datamem.findByDisplayname(displayname);
		//If no member for displayname, go error page
		if(memberProfile == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer ce membre");
			return "errorPage";
		}
		
		//Search if we are on own profile or not
		boolean itIsMemberSession = displayname.equals( member.getDisplayname() );
				
		if( itIsMemberSession || ((Member)session.getAttribute("member")).getIsAdmin() ) {
			model.addAttribute("memberprofile", memberProfile);
			
			ProfileForm profileForm = new ProfileForm();
			profileForm.setDescription(memberProfile.getDescription());
			profileForm.setEmail(memberProfile.getEmail());
			List<Competence> listCompetence = datacom.findAll();
			model.addAttribute("competenceList", listCompetence);
			List<Skill> listSkill = memberProfile.getSkills();
			model.addAttribute("skillList", listSkill);
			
			model.addAttribute("profileForm", profileForm);
			model.addAttribute("profileSkillForm", new ProfileSkillForm());
			model.addAttribute("DESCRIPTION_SIZE_MAX", Controls.DESCRIPTION_SIZE_MAX);
			model.addAttribute("DESCRIPTION_SIZE_MIN", Controls.DESCRIPTION_SIZE_MIN);
			return "profileEdit";
		}
		return this.profile(displayname, model, session);
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
			@RequestParam(value="displayname", required=false) String displayname,
			@ModelAttribute("profileForm") ProfileForm profileForm,
			Model model,
			HttpSession session) {
		
		//Get member session
		Member member = (Member)session.getAttribute("member");
		//Add member session to model
		model.addAttribute("member", member);
				
		//If no member is specified, return error
		if(displayname == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer");
			return "errorPage";
		}
			
		//Get member we editing
		Member memberProfile = datamem.findByDisplayname(displayname);
		//If no member for displayname, go error page
		if(memberProfile == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer ce membre");
			return "errorPage";
		}
		
		//Search if we are on own profile or not
		boolean itIsMemberSession = displayname.equals( member.getDisplayname() );
		
		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || member.getIsAdmin() ) {
			try {
				Controls.checkEmail(profileForm.getEmail());
				memberProfile.setEmail(profileForm.getEmail());
				Controls.checkDescription(profileForm.getDescription());
				memberProfile.setDescription(profileForm.getDescription());
				
				datamem.save(memberProfile);
				
				//If I edited my own profile, we update session member
				if( itIsMemberSession )
					session.setAttribute("member", memberProfile);
			} catch (EmailFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DescriptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Add member we editing to model 
		model.addAttribute("memberprofile", memberProfile);
		//We return member profile
		return this.profile(displayname, model, session);
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
			@RequestParam(value="displayname", required=false) String displayname,
			@ModelAttribute("profileSkillForm") ProfileSkillForm profileSkillForm,
			Model model,
			HttpSession session) {
		
		//Get member session
		Member member = (Member)session.getAttribute("member");
		//Add member session to model
		model.addAttribute("member", member);
				
		//If no member is specified, return error
		if(displayname == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer");
			return "errorPage";
		}
				
		//Get member we editing
		Member memberProfile = datamem.findByDisplayname(displayname);
		//If no member for displayname, go error page
		if(memberProfile == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer ce membre");
			return "errorPage";
		}
		
		//Search if we are on own profile or not
		boolean itIsMemberSession = displayname.equals( member.getDisplayname() );
		
		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || member.getIsAdmin() ) {
			try {
				Controls.checkCompetenceTitle(profileSkillForm.getCompetenceTitle());
			
				Competence competence = datacom.findByTitle(profileSkillForm.getCompetenceTitle().toLowerCase());
				if(competence == null) {
					competence = new Competence();
					competence.setTitle(profileSkillForm.getCompetenceTitle().toLowerCase());
					datacom.save(competence);
				}
				
				//Check if doesn't already exist a skill for this competence and member
				Skill skill = dataski.findByCompetenceAndMember(competence, memberProfile);
				//If there is no skill for this competence and this member - create it first
				if( skill == null ) {
					skill = new Skill();
					skill.setCompetence(competence);
					skill.setMember(memberProfile);
				}
				skill.setLevel( Integer.parseInt(profileSkillForm.getLevel()) );
				
				dataski.save(skill);
				//obligé de faire les add et save dans les 2 sens Soso ???
				memberProfile.addSkill(skill);
				datamem.save(memberProfile);
				
			} catch (CompetenceTitleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		session.setAttribute("member", member);
		//We return member profile
		return this.profile(displayname, model, session);
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
	@RequestMapping(value = "/editprofileremoveskill", method = RequestMethod.POST)
	public String removeProfileSkill(
			@RequestParam(value="displayname", required=false) String displayname,
			@ModelAttribute("profileSkillForm") ProfileSkillForm profileSkillForm,
			Model model,
			HttpSession session) {
		
		//Get member session
		Member member = (Member)session.getAttribute("member");
		//Add member session to model
		model.addAttribute("member", member);
				
		//If no member is specified, return error
		if(displayname == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer");
			return "errorPage";
		}
				
		//Get member we editing
		Member memberProfile = datamem.findByDisplayname(displayname);
		//If no member for displayname, go error page
		if(memberProfile == null) {
			model.addAttribute("displaymessage", "Impossible d'éditer ce membre");
			return "errorPage";
		}
		
		//Search if we are on own profile or not
		boolean itIsMemberSession = displayname.equals( member.getDisplayname() );
		
		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || member.getIsAdmin() ) {
			try {
				Controls.checkCompetenceTitle(profileSkillForm.getCompetenceTitle());
			
				Skill skill = null;
				for( Skill s : memberProfile.getSkills() )
					if( s.getCompetence().getTitle().equals(profileSkillForm.getCompetenceTitle()) ) {
						skill = s;
						break;
					}
				if( skill != null )
					dataski.delete(skill);
				
			} catch (CompetenceTitleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		session.setAttribute("member", member);
		//We return member profile
		return this.profile(displayname, model, session);
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
				Controls.checkDisplayname(profileActivationForm.getDisplayname());
				member.setDisplayname(profileActivationForm.getDisplayname());
				Controls.checkEmail(profileActivationForm.getEmail());
				member.setEmail(profileActivationForm.getEmail());
				Controls.checkDescription(profileActivationForm.getDescription());
				member.setDescription(profileActivationForm.getDescription());
				member.setActived(true);
				
				datamem.save(member);
				session.setAttribute("member", member);
			} catch (DisplaynameFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EmailFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DescriptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return this.profile(member.getDisplayname(), model, session);
	}
}
