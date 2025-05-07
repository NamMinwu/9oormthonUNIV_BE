package _9oormthonuniv.be.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

  // 댓글을 처리하는 Kafka Consumer
  @KafkaListener(topics = "comments", groupId = "comment-consumer-group")
  public void consumeComment(String message) {
    // 메시지를 받아서 처리 (여기서는 콘솔 출력)
    log.info("Received comment message from Kafka: {}", message);
  }
}
