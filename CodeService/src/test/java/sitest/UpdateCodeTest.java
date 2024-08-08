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
public class UpdateCodeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        UpdateCodeTest.class
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
        Code entity = new Code();
        entity.setId(1L);
        entity.setItemCode("A001");
        entity.setCodeNo("C001");
        entity.setCode("001");
        entity.setCodeName("Code 1");
        entity.setIsSys("Y");
        entity.setIsUse("Y");
        entity.setEtc("N/A");

        repository.save(entity);

        //when:
        ConnectLogCreated event = new ConnectLogCreated();
        event.setLogNo(1001L);
        event.setLoginDt(1639724400000L);
        event.setLoginId("user1");
        event.setLoginIdnm("User 1");
        event.setAccessIp("192.168.0.1");

        try {
            String msg = objectMapper.writeValueAsString(event);

            this.messageVerifier.send(
                    MessageBuilder
                        .withPayload(msg)
                        .setHeader(
                            MessageHeaders.CONTENT_TYPE,
                            MimeTypeUtils.APPLICATION_JSON
                        )
                        .setHeader("type", event.getEventType())
                        .build(),
                    "sitest"
                );

            //then:
            Message<?> receivedMessage =
                this.messageVerifier.receive(
                        "sitest",
                        5000,
                        TimeUnit.MILLISECONDS
                    );
            assertNotNull("Resulted event must be published", receivedMessage);

            String receivedPayload = (String) receivedMessage.getPayload();
            CodeUpdated outputEvent = objectMapper.readValue(
                receivedPayload,
                CodeUpdated.class
            );

            LOGGER.info("Response received: {}", receivedMessage.getPayload());

            assertEquals(1L, outputEvent.getId().longValue());
            assertEquals("A001", outputEvent.getItemCode());
            assertEquals("C001", outputEvent.getCodeNo());
            assertEquals("001", outputEvent.getCode());
            assertEquals("Code 1", outputEvent.getCodeName());
            assertEquals("Y", outputEvent.getIsSys());
            assertEquals("Y", outputEvent.getIsUse());
            assertEquals("N/A", outputEvent.getEtc());
        } catch (JsonProcessingException e) {
            assertTrue("exception", false);
        }
    }
}
