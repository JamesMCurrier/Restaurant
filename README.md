Created by Mark, Brandon, Teo, and James


# PROGRAM CAPABILITIES AND DESCRIPTION

The program will take in input for the Menu and the Inventory because they are serializable. As a result, when the user
closes the program and opens them again, any changes made to either would be remembered.

## Employees

There are three different types of Employees working at the Restaurant:
Manager: the manager of the restaurant. The manager is able to check on the inventory, change the amount of supplies
being ordered when restocking, etc.
Server: one of the servers of the restaurant. Servers are able to deliver food, take orders, etc.
Chef: one of the chefs of the restaurant. They can confirm orders and prepare them.

## Menu, MenuItems, and Ingredients

The menu contains many MenuItems that can be ordered. A MenuItem is basically a single food item. For example,
Hamburgers and Coffee are MenuItems. Each MenuItem can be customized. There is no limitation on the customization
for MenuItems, even if it does not make sense (this is true in some restaurants in real life, for example, at some
McDonalds and Tim Hortons locations). For example, one can add extra lettuce to Coffee.

Each MenuItem is further made up of Ingredients. Each Ingredient represents one single food ingredient. Examples include
buns, lettuce, etc. They can be added to MenuItems.

## Inventory

The restaurant's stock of ingredients is stored in the Inventory. Whenever a Order is made, the required Ingredients
will be removed from the Inventory. The Inventory can viewed from a Manager Employee.

## Orders and Bills 

Whenever a Server creates an Order, it gets added to the Bill for the table that ordered it. Each Order consists
of one or more MenuItems.



# HOW TO USE THE PROGRAM

The following is a quick guide on how one can use the program.

When the program is first run, click on the Server button to access the Server GUI in a new Window. Once you do that,
you have to sign in. If you don't sign in, everything is locked, so you can't do anything. Select any of the Employees.
Once you do that, you can then create an Order. Click on the "Create Order" button to do this, and follow instructions
on the Dialog that pops up. Now, choose the Menu Items that you want to order on the Left. Click "Add Item" to add
them to your order. If you wish, you can add additional Ingredients to your MenuItem by clicking "Send Order".
When you are done, Click "Send Order".

Close the Window, and click on the Chef button. Again, sign in. Click on the Order that you created in the Server.
Claim the Order so that you can prepare it. When done cooking, Click "Order Ready".

Close the Window, and click on the Server button again. Sign in, and click on "View Ready Orders". Click on the Order
and click "Serve" to serve it.

Now, close the Window and click on "View tables / pay" button. Click on the Table button corresponding to the Table that
you Ordered for. Select the Order (it displays how much the Order costs), and click Confirm to pay it.

Now, you are going to Roleplay as the Manager! Close the previous window and Click on the Manager button. Again, sign
in. Click on one of the Tab Buttons on the side to view the various things going on in the Restaurant. Some things
that you can do include creating/editing Ingredients in the Inventory, creating new Menu Items, among other things.
Notably, you can go to the Inventory tab, click on "Requests", which displays the Ingredients in the Inventory that
need to be resupplied. You can copy-paste it into your E-Mail, if you wish.

That's it!
