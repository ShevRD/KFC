/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic;

import DataManager.CartDataManager;
import DataManager.CustomerDataManager;
import DataManager.EmployeeDataManager;
import DataManager.InventoryDataManager;
import DataManager.OrderDataManager;
import DataManager.PaymentDataManager;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import Security.SecurityManager;

/**
 *
 * @author tavon
 */
public class Facade {
    private InventoryDataManager inventory;
    private CartDataManager cart;
    private OrderDataManager orderC;
    private PaymentDataManager pay;
    private CustomerDataManager cust;
    private EmployeeDataManager emp;
    private SecurityManager secman;
    
    
    public Facade(){
        inventory = new InventoryDataManager();
        cart = new CartDataManager();
        orderC= new OrderDataManager();
        pay = new PaymentDataManager();
        cust = new CustomerDataManager();
        emp = new EmployeeDataManager();
        secman = new SecurityManager();
    
    }
    
    //Creates a customer object, hashes the password entered and passes the customer object to the customerdatamanger to be added to the database
    public void registerCustomer(String Name, int ID, String Password){
        Customer customer = new Customer();
        customer.setName(Name);
        customer.setId(ID);
        customer.setPassword(Password);
        String hashedPassword=secman.hashPassword(customer.getPassword(),customer.getId());
        customer.setPassword(hashedPassword);
        cust.registerCustomer(customer);
    
    }
    //Creates a customer object, hashes the password entered and passes the customer object to the customerdatamanger to see if it is in the database
    public void getCustomer(String Name, int ID, String Password){
        Customer customer = new Customer();
        customer.setName(Name);
        customer.setId(ID);
        customer.setPassword(Password);
        String hashedPassword=secman.hashPassword(customer.getPassword(),customer.getId());
        customer.setPassword(hashedPassword);
        cust.getCustomer(customer);
    }
    //Creates a customer object, hashes the password entered and passes the admin object to the employeedatamanger to see if it is in the database
    public void getAdmin(String Name, int ID, String Password){
        Admin admin = new Admin();
        admin.setName(Name);
        admin.setId(ID);
        admin.setPassword(Password);
        emp.getAdmin(admin);
    }
    //Creates a employee object, hashes the password entered and passes the admin object to the employeedatamanger to see if it is in the database
    public void getEmployee(String Name, int ID, String Password){
        Employee employee = new Employee();
        employee.setName(Name);
        employee.setId(ID);
        employee.setPassword(Password);
        emp.getEmployee(employee);
    }
    //Creates a payment object,and passes the object to the paymentmanger to see if the payment details is valild
    public boolean isAuthorized(String cNum, String cExpm, String cExpy, String cCvv){
        long cnum = Long.parseLong(cNum);
        int cexpm = Integer.valueOf(cExpm);
        int cexpy = Integer.valueOf(cExpy);
        int ccvv = Integer.valueOf(cCvv);
        Payment paid = new Payment();
        paid.setCardNum(cnum);
        paid.setExpDateMonth(cexpm);
        paid.setExpDateYear(cexpy);
        paid.setCVV(ccvv);
        boolean res = pay.isAuthorized(paid);
        return res;
    }
    
    //Creates an order object,and passes the object to the orderdatamanager to place an order for a customer
    public boolean placeOrder(String Name, int CID, String Order, int TotalCost, String Status,String Quantity,String Email){
        Order order = new Order();
        order.setName(Name);
        order.setCID(CID);
        order.setOrders(Order);
        order.setTotalCost(TotalCost);
        order.setStatus(Status);
        order.setQTy(Quantity);
        order.setEmail(Email);
        boolean res = orderC.placeOrder(order);
        return res;
    }
    //Returns an arraylist with all the details of the orders in the database
    public ArrayList<String> grabDetails(){
        ArrayList<String> list  = new ArrayList<>();
        list = orderC.grabDetails();
        return list; 
    }
    
    //Returns an arraylist with all the the orders in the database
    public ArrayList<Order> viewOrders(){
        return orderC.viewOrders();
    }
    //Returns an arraylist with all the the orders in the database by using a customer id
    public ArrayList<Order> receiveOrder(){
        return orderC.receiveOrder();
    }
    //Used to cancel a specific order
    public boolean cancelOrder(String id) throws ParseException{
        boolean res = orderC.cancelOrder(id);
        return res;
    }
    //sends an email to a customer after placing an order
    public void notifyCustomer(String recepient, String Msg) throws Exception{
        orderC.notifyCustomer(recepient, Msg);
    }
    //returns all products tht are in a customer cart
    public ArrayList<Products> GenerateCart(){
        return cart.GenerateCart();
    }
    //Creates a product object and adds it to the cart
    public boolean addtoCart(int ID, String Name, int QtyInStock, int Price, String Category){
        Products product = new Products();
        product.setId(ID);
        product.setName(Name);
        product.setQuantity(QtyInStock);
        product.setPrice(Price);
        product.setCategory(Category);
        boolean res = cart.addtoCart(product);
        return res;
        
    }
    
    //Calculate the  total for a specific order
    public int calculateTotal(int Quantity, int ID){
        Order order = new Order();
        String quantity = Integer.toString(Quantity);
        order.setQTy(quantity);
        order.setId(ID);
        int total = cart.calculateTotal(order);
        return total;
    }
    
    //Removes a specific product from the customer's cat
    public boolean removefromCart(int ID){
        Products product = new Products();
        product.setId(ID);
        boolean res = cart.removefromCart(product);
        return res;
    }
    
    //Clears all products from the cart
    public void clearCart(){
        cart.clearCart();

    }
    
    public ArrayList<Products> GenerateTable(){
        return inventory.GenerateTable();
    }
    
    public ArrayList<Products> searchByName(){
        return inventory.searchByName();
    }
    //Updates the quantity of a specific product in the restaurant inventory
    public boolean updateQuantityOfProduct(int product_id, int product_quantity){
        Products product= new Products();
        product.setId(product_id);
        product.setQuantity(product_quantity);
        boolean res = inventory.updateInventory(product);
        return res;

    }
    //Deletes a product from the restaurant inventory
    public boolean deleteProductFromInventory(int delid){
        Products product = new Products();
        product.setId(delid);
        boolean res = inventory.deleteProductFromInventory(product);
        return res;
    }
    
    public ArrayList<Products> manageInventory(){
        return inventory.manageInventory();
    }
    
    //Checks if a product is already in the inventory when adding it
    public boolean checkIfProductExists(String newproductname){
        Products newproduct= new Products();
        newproduct.setName(newproductname);
        boolean res = inventory.checkIfProductExists(newproduct);
        return res;
    }

    //Adds a new product to the inventory
    public boolean addNewProduct(String Name,int stock,int price,String categ, byte[] item_image,String descrip){
        Products newproduct = new Products(0,Name,stock,price,categ, item_image, descrip);
        boolean res=inventory.addNewProduct(newproduct);
        return res;

    }
    
    public void tempProductInsertion(String id){
        inventory.tempProductInsertion(id);
    }
}
