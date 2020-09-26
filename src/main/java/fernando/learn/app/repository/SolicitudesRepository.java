package fernando.learn.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fernando.learn.app.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {

}
