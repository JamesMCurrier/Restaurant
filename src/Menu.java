import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Menu implements Serializable{
    private HashMap<String, MenuItem> menuItems = new HashMap<>();
    private Inventory inventory;
    public Menu(Inventory inventory){
        this.inventory = inventory;
        initializeMenu();
    }

    private void initializeMenu() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("menu.txt")))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] item = line.split("\\|");
                String[] order = item[0].split(" ");
                String name = order[0];
                int cost = Integer.parseInt(order[1].trim());
                Map<Ingredient, Integer> ingredients = new HashMap<>();

                for (int i = 1; i < item.length; i++) {
                    String[] ingredient = item[i].split(",");
                    String ingredientName = ingredient[0].trim();
                    Integer quantity = Integer.parseInt(ingredient[1].trim());
                    ingredients.put(inventory.getIngredient(ingredientName), quantity);
                }

                menuItems.put(name, new MenuItem(name, cost, ingredients));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("File I/O error!");
        }
    }

    public HashMap<String, MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItem(String name){
        return menuItems.get(name);
    }

    /**
     * Returns a copy of the MenuItem. This is necessary so if it needs to be customized, it will not affect the default
     * ingredients of the MenuItem.
     * @param name The name of the MenuItem to copy.
     * @return A deep copy of the MenuItem.
     */
    public MenuItem getCopy(String name) {
        return menuItems.get(name).makeCopy();
    }
}
