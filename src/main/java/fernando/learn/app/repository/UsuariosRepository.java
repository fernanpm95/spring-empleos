package fernando.learn.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fernando.learn.app.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

	Usuario findByUsername(String username);
	
}
