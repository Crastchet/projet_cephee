package fr.cephee.unilille.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;

@Entity
public class MemberInterest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ElementCollection
	@CollectionTable(name = "category_interest")
	@MapKeyJoinColumn(name = "id")
	@Column(name = "compteur_interest")
	private Map<Category, Integer> interests = new HashMap<Category, Integer>();

	@OneToOne
	private Member member;

	public MemberInterest() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void addInterest(Category cat) {
		Map<Category, Integer> tmp = new HashMap<Category, Integer>();
		tmp.putAll(interests);
		if (!this.interests.containsKey(cat))
			tmp.put(cat, 0);
		else {
				Integer interest = tmp.get(cat);
				tmp.put(cat, interest + 1);
			}		
		this.interests = tmp;
	}

	public MemberInterest(List<Category> list, Member member) {
		for (Category c : list) {
			interests.put(c, 0);
		}
		this.member = member;
	}

	public Map<Category, Integer> getInterests() {
		return interests;
	}

	public void setInterests(Map<Category, Integer> interests) {
		this.interests = interests;
	}

}
