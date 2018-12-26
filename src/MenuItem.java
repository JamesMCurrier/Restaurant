import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A MenuItem. MenuItems are items that one can order from a Menu. MenuItems store the name, the cost, and the
 * Ingredients needed to create the menu item.
 */
public class MenuItem implements Serializable{
    private String name;
    private int cost;
    private Map<Ingredient, Integer> ingredients;
    private boolean payed;

    /**
     * Constructor for MenuItem.
     * @param name Name of the MenuItem.
     * @param cost Cost of the MenuItem.
     * @param ingredients A map with Ingredients and the amount that they contain.
     */
    public MenuItem(String name, int cost, Map<Ingredient, Integer> ingredients){
        this.name = name;
        this.cost = cost;
        this.ingredients = ingredients;
        this.payed = false;
    }

    /**
     * Add specific ingredients to this MenuItem.
     * @param ingredient Ingredient to be added.
     * @param amount Amount of this ingredient to be added.
     */
    public void addIngredient(Ingredient ingredient, Integer amount){
        Integer currAmount = ingredients.get(ingredient);
        currAmount = currAmount == null? amount: currAmount + amount;
        ingredients.put(ingredient, currAmount);
    }

    /**
     * Setter that determinates if a MenuItem was payed or not.
     * @param x Boolean Payed
     */
    public void setPayed(boolean x){
        this.payed=x;
    }

    /**
     * Getter to see if the MenuItem was payed or not.
     * @return Boolean Payed
     */
    public boolean getPayed(){
        return this.payed;
    }
    /**
     * Remove specific ingredients to this MenuItem.
     * @param ingredient Ingredient to be remove.
     * @param amount Amount of this ingredient to be removed.
     */
    public void removeIngredient(Ingredient ingredient, Integer amount){
        ingredients.put(ingredient, ingredients.get(ingredient) - amount);
    }

    /**
     * Getter for the cost of this MenuItem.
     * @return Cost of this MenuItem.
     */
    public int getCost(){
        return this.cost;
    }

    /**
     * Getter for the name of this MenuItem.
     * @return Name of MenuItem.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the Ingredients that are needed to make this MenuItem, in the form of a Map. The key is the Ingredient,
     * while the Integer is the number of Ingredients of that type.
     * @return The ingredients.
     */
    public Map<Ingredient, Integer> getIngredients() {
        return this.ingredients;
    }

    /**
     * Returns a deep copy of this MenuItem. The copy will have the exact same name and ingredients Map.
     * @return A deep copy of this MenuItem.
     */
    public MenuItem makeCopy() {
        // Strings are immutable.
        String nameCopy = this.name;
        // Maps are not. Make a deep copy.
        Map<Ingredient, Integer> ingredientsCopy = this.copyIngredients();
        MenuItem itemCopy = new MenuItem(nameCopy, this.cost, ingredientsCopy);
        return itemCopy;
    }

    /**
     * Makes a deep copy of ingredients.
     * @return The deep copy.
     */
    private Map<Ingredient, Integer> copyIngredients() {
        Map<Ingredient, Integer> ingredientsCopy = new HashMap<>(0);
        for (Ingredient ingredient : this.ingredients.keySet()) {
            // Get the number of this kind of ingredient.
            int number = this.ingredients.get(ingredient);
            ingredientsCopy.put(ingredient, number);
        }
        return ingredientsCopy;
    }

    /**
     * toString function prints the name of the dish.
     * @return String name of the dish.
     */
    @Override
    public String toString(){
        return this.name;
    }
}