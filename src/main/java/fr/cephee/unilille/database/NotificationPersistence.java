package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Notification;

public interface NotificationPersistence  extends CrudRepository<Notification, Integer>{
	public List<Notification> findByAuthor(Member author);
	public List<Notification> findByMemberTargeted(Member targetedmember);
}
