package ra.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.demo.entity.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
