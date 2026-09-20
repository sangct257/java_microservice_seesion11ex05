package ra.demo.service;

import ra.demo.entity.Medicine;

import java.util.List;

public interface MedicineService {
    List<Medicine> getAllMedicines();
    Medicine getMedicineById(Long id);
    Medicine saveMedicine(Medicine medicine);
    Medicine updateMedicine(Long id ,Medicine medicine);
    void deleteMedicine(Long id);

    String sellMedicine(Long id);
}
