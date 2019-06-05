package hello;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Define a message receiver.
 *
 * <p>Receiver is also known as a message driven POJO. As you can see in the code above, <br>
 * there is no need to implement any particular interface or for the method to have any particular name. <br>
 * Besides, the method may have a very flexible signature. Note in particular that this class has no import on the JMS API.</p>
 *
 * <p>The JmsListener annotation defines the name of the Destination that this method should listen to and the reference to the JmsListenerContainerFactory <br>
 * to use to create the underlying message listener container. Strictly speaking that last attribute is not necessary unless you need to customize the way <br>
 * the container is built as Spring Boot registers a default factory if necessary.</p>
 */
@Component
public class Receiver {

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }

}
