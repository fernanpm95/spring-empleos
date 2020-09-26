package fernando.learn.app.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fernando.learn.app.model.Vacante;
import fernando.learn.app.service.ICategoriasService;
import fernando.learn.app.service.IVacantesService;
import fernando.learn.app.util.Utileria;

@Controller
@RequestMapping("/vacantes")
public class VacantesController {

	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;

	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	// @Qualifier("categoriasServiceJPA")
	private ICategoriasService serviceCategorias;

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> listaVacantes = serviceVacantes.buscarTodas();
		model.addAttribute("vacantes", listaVacantes);
		return "vacantes/listVacantes";
	}

	@GetMapping(value = "/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Vacante> lista = serviceVacantes.buscarTodas(page);
		model.addAttribute("vacantes", lista);
		return "vacantes/listVacantes";
	}

	@GetMapping("/create")
	public String crear(Vacante vacante, Model model) {

		return "vacantes/formVacante";
	}

//	@PostMapping("/save")
//	public String guardar(@RequestParam("nombre") String nombre, 
//			@RequestParam("descripcion") String descripcion, 
//			@RequestParam("categoria") String categoria, 
//			@RequestParam("estatus") String estatus, 
//			@RequestParam("fecha") String fecha, 
//			@RequestParam("destacado") int destacado,
//			@RequestParam("salario") double salario, 
//			@RequestParam("detalles") String detalles) {
//		
//		
//		return "vacantes/listVacantes";
//	}

	// Binding result justo despues de objeto de modelo!
	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result, RedirectAttributes attributes,
			@RequestParam("archivoImagen") MultipartFile multiPart) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "vacantes/formVacante";
		}
		System.out.println("Vacante: " + vacante);

		if (!multiPart.isEmpty()) {
			// String ruta = "/empleos/img-vacantes/"; // Linux/MAC
//			String ruta = "B:/Programas/Spring/Recursos/empleos/img-vacantes/"; // Windows
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null) { // La imagen si se subio
				// Procesamos la variable nombreImagen
				vacante.setImagen(nombreImagen);
				System.out.println("Vacante " + vacante);
			}

		}
		serviceVacantes.guardar(vacante);
		attributes.addFlashAttribute("msg", "Registro guardado");
		return "redirect:/vacantes/index";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante, RedirectAttributes attributes) {
		System.out.println("Borrando vacante con id: " + idVacante);

		serviceVacantes.eliminar(idVacante);
		attributes.addFlashAttribute("msg", "Vacante eliminada con éxito");
		return "redirect:/vacantes/index";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);

		return "vacantes/formVacante";
	}

	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);

		System.out.println("Vacante: " + vacante);
		model.addAttribute("vacante", vacante);
		return "detalle";
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
	}

	@InitBinder
	public void initBinder(WebDataBinder webdatabinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webdatabinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

}
