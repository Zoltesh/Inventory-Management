abstract public class Part {

    public Part(int id, String name, double price, int stock, int min, int max){
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public int getStock(){
        return this.stock;
    }

    public void setMin(int min){
        this.min = min;
    }

    public int getMin(){
        return this.min;
    }

    public void setMax(int max){
        this.max = max;
    }

    public int getMax(){
        return this.max;
    }

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

}
