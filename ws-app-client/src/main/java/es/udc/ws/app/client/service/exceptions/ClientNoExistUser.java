package es.udc.ws.app.client.service.exceptions;

public class ClientNoExistUser extends Exception{
    private String user;
    private  Long id;



    public ClientNoExistUser(String user, Long id) {
        super("User=\"" + user + "\"\n dont have with id "+id);
        this.user = user;
        this.id=id;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
