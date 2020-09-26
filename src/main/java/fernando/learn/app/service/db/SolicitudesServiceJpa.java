package fernando.learn.app.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fernando.learn.app.model.Solicitud;
import fernando.learn.app.repository.SolicitudesRepository;
import fernando.learn.app.service.ISolicitudesService;

@Service
public class SolicitudesServiceJpa implements ISolicitudesService {

	@Autowired
	SolicitudesRepository solicitudesRepo;
	
	@Override
	public void guardar(Solicitud solicitud) {
		solicitudesRepo.save(solicitud);
		
	}

	@Override
	public void eliminar(Integer idSolicitud) {
		solicitudesRepo.deleteById(idSolicitud);
		
	}

	@Override
	public List<Solicitud> buscarTodas() {
		return solicitudesRepo.findAll();
	}

	@Override
	public Solicitud buscarPorId(Integer idSolicitud) {
		Optional<Solicitud> optional = solicitudesRepo.findById(idSolicitud);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Page<Solicitud> buscarTodas(Pageable page) {
		return solicitudesRepo.findAll(page);
	}	

}
