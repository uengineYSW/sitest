package sitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;
import sitest.config.kafka.KafkaProcessor;
import sitest.domain.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMessageVerifier
public class CreateCodeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        CreateCodeTest.class
    );

    @Autowired
    private KafkaProcessor processor;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MessageVerifier<Message<?>> messageVerifier;

    @Autowired
    public CodeRepository repository;

    @Test
    @SuppressWarnings("unchecked")
    public void test0() {
        //given:
        Code existingEntity = new Code();

        existingEntity.setId(1L);
        existingEntity.setItemCode("example");
        existingEntity.setCodeNo("001");
        existingEntity.setCode("A");
        existingEntity.setCodeName("Example Code");
        existingEntity.setIsSys("N");
        existingEntity.setIsUse("Y");

        repository.save(existingEntity);

        //when:

        try {
            Code newEntity = new Code();

            newEntity.setId(1L);
            newEntity.setItemCode("example");
            newEntity.setCodeNo("001");
            newEntity.setCode("A");
            newEntity.setCodeName("Example Code");
            newEntity.setIsSys("N");
            newEntity.setIsUse("Y");

            repository.save(newEntity);

            this.messageVerifier.send(
                    MessageBuilder
                        .withPayload(newEntity)
                        .setHeader(
                            MessageHeaders.CONTENT_TYPE,
                            MimeTypeUtils.APPLICATION_JSON
                        )
                        .build(),
                    "sitest"
                );

            Message<?> receivedMessage =
                this.messageVerifier.receive(
                        "sitest",
                        5000,
                        TimeUnit.MILLISECONDS
                    );
            assertNotNull("Resulted event must be published", receivedMessage);

            //then:
            String receivedPayload = (String) receivedMessage.getPayload();

            CodeCreated outputEvent = objectMapper.readValue(
                receivedPayload,
                CodeCreated.class
            );

            LOGGER.info("Response received: {}", outputEvent);

            assertEquals(outputEvent.getId(), 1L);
            assertEquals(outputEvent.getItemCode(), "example");
            assertEquals(outputEvent.getCodeNo(), "001");
            assertEquals(outputEvent.getCode(), "A");
            assertEquals(outputEvent.getCodeName(), "Example Code");
            assertEquals(outputEvent.getIsSys(), "N");
            assertEquals(outputEvent.getIsUse(), "Y");
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            assertTrue("exception", false);
        }
    }
}
