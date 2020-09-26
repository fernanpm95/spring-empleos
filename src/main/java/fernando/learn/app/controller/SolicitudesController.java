package fernando.learn.app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernando.learn.app.model.Solicitud;
import fernando.learn.app.model.Usuario;
import fernando.learn.app.model.Vacante;
import fernando.learn.app.service.ISolicitudesService;
import fernando.learn.app.service.IUsuariosService;
import fernando.learn.app.service.IVacantesService;
import fernando.learn.app.util.Utileria;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {
	
	@Autowired
	ISolicitudesService serviceSolicitudes;
	
	@Autowired
	IVacantesService serviceVacantes;

	@Autowired
	IUsuariosService serviceUsuarios;
	
	/**
	 * EJERCICIO: Declarar esta propiedad en el archivo application.properties. El valor sera el directorio
	 * en donde se guardarán los archivos de los Curriculums Vitaes de los usuarios.
	 */
	@Value("${empleosapp.ruta.cv}")
	private String ruta;
		
    /**
	 * Metodo que muestra la lista de solicitudes sin paginacion
	 * Seguridad: Solo disponible para un usuarios con perfil ADMINISTRADOR/SUPERVISOR.
	 * @return
	 */
    @GetMapping("/index") 
	public String mostrarIndex(Model model) {
    	// EJERCICIO
    	List<Solicitud> solicitudes = serviceSolicitudes.buscarTodas();
    	model.addAttribute("solicitudes", solicitudes);
		return "solicitudes/listSolicitudes";
		
	}
    
    /**
	 * Metodo que muestra la lista de solicitudes con paginacion
	 * Seguridad: Solo disponible para usuarios con perfil ADMINISTRADOR/SUPERVISOR.
	 * @return
	 */
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		// EJERCICIO
		Page<Solicitud> solicitudes = serviceSolicitudes.buscarTodas(page);
    	model.addAttribute("solicitudes", solicitudes);
		return "solicitudes/listSolicitudes";
		
	}
    
	/**
	 * Método para renderizar el formulario para aplicar para una Vacante
	 * Seguridad: Solo disponible para un usuario con perfil USUARIO.
	 * @return
	 */
	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable("idVacante") int idVacante, Model model) {
		// EJERCICIO
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		
		return "solicitudes/formSolicitud";
		
	}
	
	/**
	 * Método que guarda la solicitud enviada por el usuario en la base de datos
	 * Seguridad: Solo disponible para un usuario con perfil USUARIO.
	 * @return
	 */
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, BindingResult result, @RequestParam(value="archivoCV",required = false) MultipartFile multiPart,@RequestParam(value="archivoCV2",required = false) String archivo2, HttpSession session, Authentication auth, RedirectAttributes attributes) {	
		// EJERCICIO
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "solicitudes/formSolicitud";
		}
		System.out.println("Solicitud " + solicitud);
		System.out.println("Solicitudl " + solicitud.getUsuario());

		if (solicitud.getUsuario().getId() == null) {
			String username = auth.getName();
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);
			solicitud.setUsuario(usuario);
		}
		solicitud.setFecha(new Date());
		
		if (!multiPart.isEmpty()) {
			String nombreArchivo = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreArchivo != null) { // La imagen si se subio
				// Procesamos la variable nombreImagen
				solicitud.setArchivo(nombreArchivo);
				System.out.println("Solicitud " + solicitud);
			}
		} else {
			//solicitud.setArchivo(archivo2);
		}
		serviceSolicitudes.guardar(solicitud);
		attributes.addFlashAttribute("msg", "Vacante guardada");
		return "redirect:/";	
	}
	
	/**
	 * Método para eliminar una solicitud
	 * Seguridad: Solo disponible para usuarios con perfil ADMINISTRADOR/SUPERVISOR. 
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud) {
		// EJERCICIO
		serviceSolicitudes.eliminar(idSolicitud);
		return "redirect:/solicitudes/indexPaginate";
		
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int idSolicitud, Model model) {
		Solicitud solicitud = serviceSolicitudes.buscarPorId(idSolicitud);
		model.addAttribute("solicitud", solicitud);
		model.addAttribute("vacante", solicitud.getVacante());
		return "solicitudes/formSolicitud";
	}
			
	/**
	 * Personalizamos el Data Binding para todas las propiedades de tipo Date
	 * @param webDataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
