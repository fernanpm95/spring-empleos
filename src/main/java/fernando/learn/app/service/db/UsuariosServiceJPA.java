package fernando.learn.app.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fernando.learn.app.model.Usuario;
import fernando.learn.app.repository.UsuariosRepository;
import fernando.learn.app.service.IUsuariosService;

@Service
public class UsuariosServiceJPA implements IUsuariosService {

	@Autowired
	UsuariosRepository repoUsuarios;
	
	@Override
	public void guardar(Usuario usuario) {
		repoUsuarios.save(usuario);
		
	}

	@Override
	public void eliminar(Integer idUsuario) {
		repoUsuarios.deleteById(idUsuario);
		
	}

	@Override
	public List<Usuario> buscarTodos() {
		return repoUsuarios.findAll();
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		return repoUsuarios.findByUsername(username);
	}

}
