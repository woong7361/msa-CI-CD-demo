package com.example.orderservice.messageque;

import com.example.orderservice.dto.order.OrderRequestDto;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    @KafkaListener(topics = "order-request", groupId = "consumerGroupId")
    public void updateQty(String kafkaMessage) {
        log.info("kafka Message: {}", kafkaMessage);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            OrderRequestDto orderRequestDto = objectMapper.readValue(kafkaMessage, OrderRequestDto.class);
            orderService.order(orderRequestDto, orderRequestDto.getMemberId());
        } catch (Exception e) {
            log.error("order error -> rollback required: {}", e.getMessage());
            kafkaProducer.send("order-rollback", kafkaMessage);
        }


    }
}