package es.udc.ws.app.client.service.exceptions;

public class ClientNoValidAmount extends Exception{

    private int amount;

    public ClientNoValidAmount(int amount) {
        super("Amount "+amount+" doesnt exist");
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
