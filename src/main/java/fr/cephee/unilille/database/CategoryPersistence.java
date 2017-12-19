package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Category;

public interface CategoryPersistence extends CrudRepository<Category, Integer> {
	public List<Category> findAll();
}
