import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An Inventory to keep track of Ingredients in stock at a Restaurant.
 */
public class Inventory implements Serializable {

    // The ingredients file.
    final private static String INGREDIENTS_FILE = "ingredients.txt";
    // The requests file, used by the Manager to write emails.
    final private static String REQUESTS_FILE = "requests.txt";

    private HashMap<String, Ingredient> stock = new HashMap<>();
    private ArrayList<String> requests = new ArrayList<>();

    public Inventory() {
        initializeInventory();
    }

    /**
     * Initializes this Inventory based off the ingredients in 'ingredients.txt'
     */
    private void initializeInventory(){
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(INGREDIENTS_FILE)))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] lines = line.split(",");
                String ingredientName = lines[0].trim();
                Integer quantity = Integer.parseInt(lines[1].trim());
                Integer threshold = Integer.parseInt(lines[2].trim());
                Integer requestAmount = Integer.parseInt(lines[3].trim());
                Ingredient ingredient = new Ingredient(ingredientName, quantity, threshold, requestAmount);
                stock.put(ingredientName, ingredient);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("File I/O error!");
        }
    }

    /**
     * Add the the specified amount of ingredient to the stock.
     */
    public void addIngredient(String ingredient, int quantity) {
        stock.get(ingredient).addStock(quantity);
        requests.remove(ingredient);
    }

    /**
     * Remove the the specified amount of ingredient from the stock.
     */
    public void removeIngredient(String ingredientName, int quantity) {
        Ingredient ingredient = stock.get(ingredientName);
        if(!ingredient.removeStock(quantity)){
            System.out.println("Not enough " + ingredientName + " in stock!");
        }
        if(ingredient.getAmount() <= ingredient.getRestockThreshold() && !requests.contains(ingredientName)){
            request(ingredientName);
        }
    }

    /**
     * Returns all of the Ingredients in this Inventory as an ArrayList of Ingredients.
     * @return The ArrayList of Ingredients.
     */
    public ArrayList<Ingredient> getStock() {
        ArrayList<Ingredient> ingredients = new ArrayList<>(this.stock.size());
        // Iterate through the HashMap to get all of the values.
        for (Map.Entry<String, Ingredient> pair : this.stock.entrySet()) {
            ingredients.add(pair.getValue());
        }
        return ingredients;
    }

    public HashMap<String, Ingredient> getIngredients() {
        return stock;
    }

    /**
     * Change the specified Ingredient's amount to request when ordering/restocking supplies.
     * @param ingredientName The name of the Ingredient to change the request amount.
     * @param newRequestAmount The new request amount.
     */
    public void changeRequestAmount(String ingredientName, int newRequestAmount) {
        this.stock.get(ingredientName).setRequestAmount(newRequestAmount);
    }

    /**
     * Change the specified Ingredient's request threshold: the minimum amount that of the Ingredient in the inventory
     * before more of the Ingredient has to be ordered in the resupply.
     * @param ingredientName The name of the Ingredient to change the request threshold.
     * @param newThreshold The new threshold before resupply is needed for the Ingredient.
     */
    public void changeRequestThreshold(String ingredientName, int newThreshold) {
        this.stock.get(ingredientName).setRestockThreshold(newThreshold);
    }

    /**
     * Creates a request for the specified ingredient to be ordered.
     * @param ingredient The ingredient to be ordered.
     */
    private void request(String ingredient) {
        try(PrintWriter output = new PrintWriter(new FileWriter(REQUESTS_FILE, true))) {
            output.append(String.format(ingredient + ", " + getIngredient(ingredient).getRequestAmount()));
            output.close();
            requests.add(ingredient);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns a String representation of this Inventory. Lists all of the Ingredients in the Inventory and their
     * amounts.
     * @return The String representation.
     */
    @Override
    public String toString() {
        String representation = "\n --- INGREDIENTS CURRENTLY IN THE INVENTORY --- \n";
        // Iterate through the HashMap to get all of the values.
        for (Map.Entry<String, Ingredient> pair : this.stock.entrySet()) {
            representation += pair.getKey();
            representation += ": ";
            representation += pair.getValue().getAmount();
            representation += "\n";
        }
        return representation;
    }

    public Ingredient getIngredient(String name){
        return stock.get(name);
    }

    public String getLowStock(){
        String str = "";
        for (Ingredient i : getStock()){
            if (i.getAmount() < i.getRestockThreshold()){
                str += i.getName();
                str += "\n";
            }
        }
        return str;
    }
}