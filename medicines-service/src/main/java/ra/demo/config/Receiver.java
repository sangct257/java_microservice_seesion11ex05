package ra.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import ra.demo.dto.AlertMessage;

@Service
public class Receiver implements MessageListener {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        try {
            // Chuyển JSON chuỗi byte từ Redis sang Object AlertMessage
            AlertMessage alert = objectMapper.readValue(message.getBody(), AlertMessage.class);

            System.out.println("=== THÔNG BÁO DASHBOARD QUẢN LÝ ===");
            System.out.println("Type : " + alert.getType());
            System.out.println("Nội dung: " + alert.getMessage());
            System.out.println("==================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
