package _9oormthonuniv.be.domain.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  private static final String TOPIC = "comments";

  public void sendTopic(Object comment) {
    kafkaTemplate.send(TOPIC, comment);
    log.info("Sent comment to Kafka: {}", comment);
  }
}