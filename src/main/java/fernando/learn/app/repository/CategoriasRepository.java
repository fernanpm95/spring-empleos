package fernando.learn.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fernando.learn.app.model.Categoria;

//public interface CategoriasRepository extends CrudRepository<Categoria, Integer> {
public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	
}
