package ra.demo.service.impl;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ra.demo.entity.Medicine;
import ra.demo.repository.MedicineRepository;
import ra.demo.service.MedicineService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final RedissonClient redissonClient;

    @Override
    @Cacheable(value = "medicines")
    public List<Medicine> getAllMedicines() {
        System.out.println("--> [DB QUERY] Đang truy vấn danh sách toàn bộ thuốc từ Database...");
        return medicineRepository.findAll();
    }

    // 1. Lấy thông tin thuốc theo ID và lưu vào Cache
    @Override
    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại medicine với id: " + id));
    }

    @Override
    @Cacheable(value = "medicines", key = "#medicines.id")
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    // 2. Cập nhật thông tin thuốc và XÓA Cache cũ
    @Override
    @CacheEvict(value = "medicines", key = "#medicine.id")
    public Medicine updateMedicine(Long id, Medicine medicine) {
        System.out.println("--> [UPDATE LOG] Cap nhat DB va Evict Cache cho Medicine ID = " + id);
        medicineRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại medicine với id: " + id));
        medicine.setId(id);
        return medicineRepository.save(medicine);
    }

    @Override
    @Cacheable(value = "medicines", key = "#id")
    public void deleteMedicine(Long id) {
        medicineRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại medicine với id: " + id));
        medicineRepository.deleteById(id);
    }

    @Override
    public String sellMedicine(Long id) {
        RLock lock = redissonClient.getLock("lock:medicine:" + id);
        try {
            // Cố gắng lấy Lock: waitTime = 3s, leaseTime = 5s
            boolean isLocked = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (!isLocked) {
                return "Hệ thống đang bận, vui lòng thử lại sau!";
            }

            try {
                // Kiểm tra tồn kho
                Medicine medicine = medicineRepository.findById(id).orElse(null);
                if (medicine == null) {
                    return "Sản phẩm không tồn tại!";
                }

                if (medicine.getQuantity() <= 0) {
                    return "Sản phẩm đã hết hàng!";
                }

                // Trừ số lượng kho
                medicine.setQuantity(medicine.getQuantity() - 1);
                medicineRepository.save(medicine);
                return "Thanh toán thành công thuốc: " + medicine.getName();
            } finally {
                // Luôn giải phóng Lock sau khi xử lý xong
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return "Lỗi xử lý luồng!";
        }
    }
}
