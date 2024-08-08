package sitest.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sitest.config.kafka.KafkaProcessor;
import sitest.domain.*;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    CodeRepository codeRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ConnectLogCreated'"
    )
    public void wheneverConnectLogCreated_UpdateCode(
        @Payload ConnectLogCreated connectLogCreated
    ) {
        ConnectLogCreated event = connectLogCreated;
        System.out.println(
            "\n\n##### listener UpdateCode : " + connectLogCreated + "\n\n"
        );

        Code.updateCode(event);
    }
}
