package pl.forex.trading_platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.forex.trading_platform.domain.nbp.TableA;
import pl.forex.trading_platform.messageBroker.RCP_client.RCPQuotationClient;
import pl.forex.trading_platform.messageBroker.simpleMessageSender.QuotationSenderRabbitMQ;
import pl.forex.trading_platform.repository.NbpRatesRepository;
import pl.forex.trading_platform.service.JsonConverter;
import pl.forex.trading_platform.service.LoadQuotations;
import pl.forex.trading_platform.service.NbpRates;

@EnableScheduling
@Configuration
@PropertySource("classpath:platformSettings.properties")
public class SchedulerConfig {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    LoadQuotations loadQuotations;

    @Value("${platformSettings.nbpTableA}")
    private String nbpTableAurl;

    @Autowired
    NbpRates nbpRates;

    @Autowired
    private NbpRatesRepository nbpRatesRepository;

    @Autowired
    private QuotationSenderRabbitMQ quotationSenderRabbitMQ;

    @Autowired
    private JsonConverter jsonConverter;

    @Scheduled(fixedDelay = 3000)
    public void sendAdhocMessages() {
        System.out.println(loadQuotations.loadLastQuotations());
        simpMessagingTemplate.convertAndSend("/topic/user", loadQuotations.loadLastQuotations());
        try {
            String jsonQoutes = jsonConverter.Quotation2JsonConverter(loadQuotations.loadLastQuotations());
            quotationSenderRabbitMQ.SendMessageToBroker();
            RCPQuotationClient.run(loadQuotations.loadLastQuotations().size(), jsonQoutes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 1000*60*60*24)
    public void SaveDailyNbpRates() {
        TableA[] tableAarray = nbpRates.getTableAQuotesArray(nbpTableAurl);
        nbpRatesRepository.save(tableAarray[0]);

    }
}

