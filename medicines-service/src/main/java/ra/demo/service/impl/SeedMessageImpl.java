package ra.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ra.demo.dto.AlertMessage;
import ra.demo.service.SeedMessage;

@Service
@AllArgsConstructor
public class SeedMessageImpl implements SeedMessage {
    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void seedMessage(AlertMessage alertMessage) {
        try {
            // Convert Object thành chuỗi JSON
            String jsonMessage = objectMapper.writeValueAsString(alertMessage);
            // Gửi tới channel truyền trong payload
            redisTemplate.convertAndSend(alertMessage.getTopicListen(), jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
