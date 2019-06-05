package hello;

/**
 * Create a very simply POJO.
 *
 * let’s create a very simply POJO that embodies the details of an email message.<br>
 * Pay note, we aren’t sending an email message. We’re simply sending the details from one place to another about WHAT to send in a message.
 * <p>This POJO is quite simple, containing two fields, to and body, along with the presumed set of getters and setters.</p>
 */
public class Email {

    private String to;
    private String body;

    public Email() {
    }

    public Email(String to, String body) {
        this.to = to;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("Email{to=%s, body=%s}", getTo(), getBody());
    }

}
