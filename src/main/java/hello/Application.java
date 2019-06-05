package hello;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * Wire up a sender and a receiver.
 *
 * [] @SpringBootApplication is a convenience annotation that adds all of the following:
 * <ul>
 *     <li>@Configuration tags the class as a source of bean definitions for the application context.</li>
 *     <li>@EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.</li>
 *     <li>@EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.</li>
 *     <li>Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.<br>
 *         This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.</li>
 *     <li>@ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.</li>
 * </ul>
 *
 * <p>The main() method uses Spring Boot’s SpringApplication.run() method to launch an application. <br>
 * Did you notice that there wasn’t a single line of XML? No web.xml file either. <br>
 * This web application is 100% pure Java and you didn’t have to deal with configuring any plumbing or infrastructure.</p>
 *
 * [] @EnableJms triggers the discovery of methods annotated with @JmsListener, creating the message listener container under the covers.
 *
 * <p>For clarity, we have also defined a myFactory bean that is referenced in the JmsListener annotation of the receiver. <br>
 * Because we use the DefaultJmsListenerContainerFactoryConfigurer infrastructure provided by Spring Boot, <br>
 * that JmsMessageListenerContainer will be identical to the one that boot creates by default.</p>
 *
 * <p>The default MessageConverter is able to convert only basic types (such as String, Map, Serializable) and our Email is not Serializable on purpose.<br>
 * We want to use Jackson and serialize the content to json in text format (i.e. as a TextMessage). <br>
 * Spring Boot will detect the presence of a MessageConverter and will associate it to both the default JmsTemplate <br>
 *  and any JmsListenerContainerFactory created by DefaultJmsListenerContainerFactoryConfigurer.</p>
 *
 * <p>JmsTemplate makes it very simple to send messages to a JMS destination. In the main runner method, after starting things up, <br>
 * you can just use jmsTemplate to send an Email POJO. Because our custom MessageConverter has been automatically associated to it, <br>
 * a json document will be generated in a TextMessage only.</p>
 *
 * <p>Two beans that you don’t see defined are JmsTemplate and ConnectionFactory. These are created automatically by Spring Boot.<br>
 *     In this case, the ActiveMQ broker runs embedded.</p>
 *
 * <p>By default, Spring Boot creates a JmsTemplate configured to transmit to queues by having pubSubDomain set to false. <br>
 * The JmsMessageListenerContainer is also configured the same. To override, set spring.jms.isPubSubDomain=true <br>
 * via Boot’s property settings (either inside application.properties or by environment variable). <br>
 * Then make sure the receiving container has the same setting.</p>
 *
 */
@SpringBootApplication
@EnableJms
public class Application {

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    public static void main(String[] args) {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        // Send a message with a POJO - the template reuse the message converter
        System.out.println("Sending an email message.");
        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
    }

}