package fernando.learn.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernando.learn.app.model.Usuario;
import fernando.learn.app.service.IUsuariosService;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    IUsuariosService serviceUsuarios;
	
    @GetMapping("/index")
	public String mostrarIndex(Model model) {

    	// Ejercicio
    	List<Usuario> listaUsuarios = serviceUsuarios.buscarTodos();
    	model.addAttribute("usuarios", listaUsuarios);
    	return "usuarios/listUsuarios";
	}
    
    @GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {		    	
		
    	serviceUsuarios.eliminar(idUsuario);
    	attributes.addFlashAttribute("msg", "Usuario eliminado con éxito");
    	
		return "redirect:/usuarios/index";
	}
    
}
