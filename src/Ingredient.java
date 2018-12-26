import java.io.Serializable;

/**
 * Class that represents an Ingredient in an Inventory. It stores the name of the ingredient, the amount currently
 * available, the threshold before a restock is needed, and the amount to request when it is below the threshold.
 */
public class Ingredient implements Serializable{
    private String name;
    private int amount;

    // The minimum amount of this Ingredient there can be before it has to be restocked.
    private int restockThreshold;
    // The amount of this Ingredient to request when restock is needed.
    private int requestAmount;

    public Ingredient(String name, int amount, int restockThreshold, int requestAmount) {
        this.name = name;
        this.amount = amount;
        this.restockThreshold = restockThreshold;
        this.requestAmount = requestAmount;
    }

    public Ingredient(String name, int amount) {
        this.name = name;
        this.amount = amount;
        this.restockThreshold = 0;
        this.requestAmount = 0;
    }

    /**
     * Add the specified amount of this ingredient to its current amount.
     * @param amount The amount to be added.
     */
    public void addStock(int amount){
        this.amount += amount;
    }

    /**
     * Removes the specified amount of this ingredient from its current amount, if there is enough to do so.
     * @param amount The amount to be removed.
     * @return False if there's not enough in stock, true otherwise.
     */
    public boolean removeStock(int amount) {
        if(amount <= this.amount){
            this.amount -= amount;
            return true;
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return amount;
    }

    public int getRestockThreshold(){
        return restockThreshold;
    }

    public void setRestockThreshold(int restockThreshold) {
        this.restockThreshold = restockThreshold;
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(int requestAmount) {
        this.requestAmount = requestAmount;
    }
}
