package lab.reservation_server.repository;

import lab.reservation_server.domain.Lab;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabRepository extends JpaRepository<Lab, Long> {

}
