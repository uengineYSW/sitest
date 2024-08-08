package sitest.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sitest.config.kafka.KafkaProcessor;
import sitest.domain.*;

//<<< Clean Arch / Inbound Adaptor
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

        // Comments //
        //코드 업데이트 테스트

        // Sample Logic //
        Code.updateCode(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
