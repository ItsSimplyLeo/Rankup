package cx.leo.rankup.rank;

public class Rank {

    private final String id;
    private final double price;

    public Rank(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
}
