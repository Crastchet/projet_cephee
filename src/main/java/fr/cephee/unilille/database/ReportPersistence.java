package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.Report;

public interface ReportPersistence  extends CrudRepository<Report, Integer>{
	public List<Report> findByReporter(Member reporter);
	public List<Report> findByPublication(Publication publication);
	public List<Report> findById(Integer id);
	public List<Report> findAllByOrderByDateDesc();
}
