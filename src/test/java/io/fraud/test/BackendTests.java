package io.fraud.test;

import io.fraud.database.model.Deal;
import io.fraud.kafka.KafkaRecord;
import io.fraud.kafka.KafkaService;
import io.fraud.kafka.messages.DealMessage;
import io.fraud.kafka.messages.GeneratorMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class BackendTests extends BaseTest {

    @Test
    void shouldWriteMessageToQueTransaction() {
        kafkaService.subscribe("body");
        kafkaService.send("body", "You got damn right! :-)");

        KafkaRecord recievedRecords = kafkaService.waitForMessage("You got damn right! :-)");

        assertThat(recievedRecords).isNotNull();
    }

    @Test
    void testApplicationCanProcessValidMessages() {
     //  GeneratorMessage generatorMessage = new GeneratorMessage();
     //  generatorMessage.setDate(new Date().toString());
     //  generatorMessage.setAmount(900);
     //  generatorMessage.setCurrency("EUR");
     //  generatorMessage.setSource(RandomStringUtils.randomAlphanumeric(10));
     //  generatorMessage.setTarget(RandomStringUtils.randomAlphanumeric(10));

        kafkaService.subscribeLegitTopic();

        // "queuing.transactions", generatorMessage

        GeneratorMessage generatorMessage = kafkaService.send();

        //kafkaService.send("queuing.transactions","{\"date\": \"10/20/2020 17:28:35\", \"source\": \"java12\"," +
        //     " \"target\": \"python\", " +
        //  "\"amount\": 900.00, \"currency\": \"EUR\"}");

        KafkaRecord recievedMessage = kafkaService.waitForMessage(generatorMessage.getSource());

        DealMessage dealMessage = recievedMessage.valuesAs(DealMessage.class);
        assertThat(dealMessage.getAmount()).isEqualTo(generatorMessage.getAmount());

        assertThat(dealMessage.getBaseCurrency()).isEqualTo("USD");
    }

    @Test
    void testApplicationCanProcessToFraud() {
        GeneratorMessage generatorMessage = new GeneratorMessage();
        generatorMessage.setDate(new Date().toString());
        generatorMessage.setAmount(2000);
        generatorMessage.setCurrency("EUR");
        generatorMessage.setSource(RandomStringUtils.randomAlphanumeric(10));
        generatorMessage.setTarget(RandomStringUtils.randomAlphanumeric(10));

        kafkaService.subscribeFraudTopic();

        kafkaService.send(generatorMessage);

        KafkaRecord recievedMessage = kafkaService.waitForMessage(generatorMessage.getSource());

        DealMessage dealMessage = recievedMessage.valuesAs(DealMessage.class);
        assertThat(dealMessage.getAmount()).isEqualTo(2000.00);

        assertThat(dealMessage.getBaseCurrency()).isEqualTo("USD");
    }

    @Test
    void testApplicationCanSaveFraudMessageTodb() {
        List<Deal> dealById = dbService.findDealByCurrency("USD");
        assertThat(dealById.size()).isGreaterThan(0);
        System.out.println(dealById.size());
    }

    @Test
    void testApplicationCanSaveFraudMessageTodbByID() {
        Deal dealById = dbService.findDealById(1);
        assertThat(dealById.getAmount()).isEqualTo(992.61);
    }

    @Test
    void addToKafkaAndGetFromDb() {
        GeneratorMessage generatorMessage = kafkaService.send();
        List<Deal> findDealsBySource = dbService.findDealBySource(generatorMessage.getSource());
        assertThat(findDealsBySource.size()).isEqualTo(1);
    }
}
