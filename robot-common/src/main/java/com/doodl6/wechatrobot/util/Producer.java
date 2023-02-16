package com.doodl6.wechatrobot.util;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class Producer extends Thread {
    private final KafkaProducer<String, String> producer;


    public Producer(String bootstrapServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);

    }


    public boolean produce(String topic, String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
        long startTime = System.currentTimeMillis();
        producer.send(record, new MsgSendCallBack(startTime, msg) {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (metadata != null) {
                    System.out.println(
                            "message(" + msg + ") sent to partition(" + metadata.partition() +
                                    "), " +
                                    "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
                } else {
                    exception.printStackTrace();
                }
            }
        });

        return true;
    }

}

class MsgSendCallBack implements Callback {

    private final long startTime;

    private final String message;

    public MsgSendCallBack(long startTime,  String message) {
        this.startTime = startTime;

        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                    "message("  + message + ") sent to partition(" + metadata.partition() +
                            "), " +
                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}