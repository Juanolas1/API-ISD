package es.udc.ws.app.model.tripservice.exceptions;

public class NoValidAmount extends Exception{

    private int amount;

    public NoValidAmount(int amount) {
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
