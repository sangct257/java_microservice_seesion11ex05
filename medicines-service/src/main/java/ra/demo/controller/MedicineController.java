package ra.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.demo.entity.Medicine;
import ra.demo.service.MedicineService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public ResponseEntity<List<Medicine>> getAll() {
        return new ResponseEntity<>(medicineService.getAllMedicines(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        Medicine medicine = medicineService.getMedicineById(id);
        long endTime = System.currentTimeMillis();

        System.out.println("-> Thời gian phản hồi: " + (endTime - startTime) + " ms");
        return ResponseEntity.ok(medicine);
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        return new ResponseEntity<>(medicineService.saveMedicine(medicine), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(@PathVariable Long id, @RequestBody Medicine medicine) {
        return new ResponseEntity<>(medicineService.updateMedicine(id, medicine), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/sell/{id}")
    public void sellMedicine(@PathVariable Long id) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 1 : " + rsBuy);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 2 : " + rsBuy);
            }
        });

        thread2.start();
        thread1.start();
    }
}
