package businessLogic;
import connection.ConnectionFactory;
import model.Client;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import model.Product;
import model.Order;
import java.io.File;
import presentationLayer.*;
import connection.DataAccessClass;


public class OrderBLL {
    private DataAccessClass<Order> orderDAO;
    private MainFrame mainFrame;
    private int newID;
    private float totalPrice;
    private String dateAndTime;
    private ArrayList<Order> orders;
    private Client client;

    /**
     * Creates a new instance of OrderBLL.
     * @param connectionFactory is the object holding the connection to the database.
     * @param mainFrame is the main window of the application, which will have to be updated in this class when client changes are made.
     */
    public OrderBLL(ConnectionFactory connectionFactory, MainFrame mainFrame)
    {
        this.mainFrame=mainFrame;
        orderDAO= new DataAccessClass<Order>(connectionFactory, new Order(), mainFrame);
        ArrayList<Order> ordersNotUsed = orderDAO.findAll();
        if(ordersNotUsed.isEmpty()){
            newID = 1;
        }
        else
        newID=ordersNotUsed.get(ordersNotUsed.size()-1).getId()+1;
        totalPrice=0;
        dateAndTime="";
        orders=new ArrayList<>();
    }

    public void reset()
    {
        totalPrice=0;
        dateAndTime="";
        orders=new ArrayList<>();
    }


    public int getNewID()
    {
        return newID;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    /**
     * Attempts to add a new product to the order.
     * @param product the object of Product type that will be added.
     * @param quantity the quantity of the desired product.
     * @return 0 if the product was successfully added to the order, -1 if the quantity is larger than the available stock.
     */
    public int addProduct(Product product, int quantity)
    {
        int i=0;
        while(i<orders.size() && orders.get(i).getIdProduct()!=product.getId())
            i++;

        int q;
        if(i==orders.size()) //this item is not here
        {
            if(product.getStock()<quantity) return -1;
            Order order= new Order();
            order.setIdProduct(product.getId());
            order.setQuantity(quantity);
            order.setPrice(product.getPrice()*quantity);
            orders.add(order);
            totalPrice+=product.getPrice()*quantity;
            return 0; //ok
        }
        if((q=orders.get(i).getQuantity()+quantity)>product.getStock()) return -1;
        //we can add it
        totalPrice=totalPrice- orders.get(i).getPrice();
        orders.get(i).setQuantity(q);
        orders.get(i).setPrice(product.getPrice()*q);
        totalPrice=totalPrice+ orders.get(i).getPrice();
        return 0; //ok
    }


    /**
     * Used to display the current status of the unfinished order.
     * @param productsOperations object of ProductsOperations type, needed to get additional information abut the products.
     * @return an object of String type to be displayed in the GUI, containing the total price, the names, prices and quantities of the products in the order.
     */
    public String toStringGUI(ProductBLL productsOperations)
    {
        String result="Total price: "+totalPrice;
        for(int i=0;i< orders.size();i++)
        {
            Order order=orders.get(i);
            int idProduct=order.getIdProduct();
            Product product = productsOperations.getProductByID(idProduct);
            result= result + "\n" + product.getName() + ", " + order.getQuantity()+", price: "+order.getPrice();
        }

        return result;
    }

    private void createBill(ProductBLL productsOperations)
    {
        File billFile = new File("Bill_"+newID+".txt");
        try {
            FileWriter fw = new FileWriter(billFile);
            PrintWriter pw = new PrintWriter(fw);
            String str= "Order with id: "+newID +"\n\nClient: "+client.toStringBill()+"\n\nDate and time: "+dateAndTime+"\n\nTotal price: "+totalPrice+"\n\nProducts:\n\n";
            for(int i=0;i< orders.size();i++) {
                Product product = productsOperations.getProductByID(orders.get(i).getIdProduct());
                str=str+product.toString() +", quantity: "+orders.get(i).getQuantity()+", total price: "+orders.get(i).getPrice()+"\n";
            }

            pw.append(str);
            pw.close();
        } catch (IOException e) {System.out.println("ERROR writing to bill\n"); e.printStackTrace();}
    }

    public void placeOrder(ProductBLL productsOperations)
    {
        long millis= System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        java.sql.Time time = new java.sql.Time(millis);
        dateAndTime= date + " " + time;
        client = (Client)(((OrdersView)mainFrame.panels[2]).clientsComboBox.getSelectedItem());
        int idClient = client.getId();
        for(int i=0;i< orders.size();i++)
        {
            Order order= orders.get(i);
            order.setDateAndTime(dateAndTime);
            order.setId(newID);
            order.setIdClient(idClient);
            orderDAO.insert(order);
            Product product = productsOperations.getProductByID(order.getIdProduct());
            product.setStock(product.getStock()-order.getQuantity());
            productsOperations.updateProduct(product);
            createBill(productsOperations);
        }
        reset();
        newID++;
    }

}