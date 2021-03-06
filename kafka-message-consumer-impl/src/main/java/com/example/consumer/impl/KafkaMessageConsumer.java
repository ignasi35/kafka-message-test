package com.example.consumer.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import com.example.generator.api.KafkaMessageGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
public class KafkaMessageConsumer {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final KafkaMessageGeneratorService kafkaMessageGeneratorService;

    @Inject
    public KafkaMessageConsumer(KafkaMessageGeneratorService kafkaMessageGeneratorService) {
        this.kafkaMessageGeneratorService = kafkaMessageGeneratorService;
    }

    void start() {
        if (started.compareAndSet(false, true)) {
            kafkaMessageGeneratorService.messages()
                    .subscribe()
                    .atLeastOnce(
                            Flow.fromFunction(m -> {
                                log.info("Consumed message: " + m);
                                return Done.getInstance();
                            })
                    );

        }
    }
}
