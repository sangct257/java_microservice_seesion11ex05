package ra.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertMessage {
    private String topicListen;
    private String type;
    private String message;
}
