package com.psql.kafka;

import com.psql.service.PsqlParserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Component
@Transactional
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    PsqlParserService psqlParserService;

    @KafkaListener(topics = "audit_log1", groupId = "audit_log_group")
    public void processData(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, ConsumerRecord<?, ?> record) throws SQLException {
        logger.info("Consumer 1 received on Thread ID: {} - {} :[ key ] from partition : {} ",Thread.currentThread().getId(),message,partition);
        String data = record.value().toString();
        psqlParserService.addLogForData(data);
    }
}
