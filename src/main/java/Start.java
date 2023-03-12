import businessLogic.*;
import presentationLayer.*;
import connection.ConnectionFactory;

public class Start {
    public static void main( String[] args )
    {
        ConnectionFactory myConnection=new ConnectionFactory();
        MainFrame mainFrame= new MainFrame("Order management");
        ClientBLL clientsOperations = new ClientBLL(myConnection, mainFrame);
        ProductBLL productsOperations = new ProductBLL(myConnection, mainFrame);
        OrderBLL ordersOperations = new OrderBLL(myConnection, mainFrame);
        Controller myController = new Controller(mainFrame, clientsOperations, productsOperations, ordersOperations);
        if ( !clientsOperations.getClients().isEmpty() )
            ((ClientsView)mainFrame.panels[0]).updateTable(clientsOperations.getClients());
        if ( !productsOperations.getProducts().isEmpty() )
            ((ProductsView)mainFrame.panels[1]).updateTable(productsOperations.getProducts());
        if ( !clientsOperations.getClients().isEmpty() )
            ((OrdersView)mainFrame.panels[2]).updateClientsBox(clientsOperations.getClients());
        if ( !productsOperations.getProducts().isEmpty() )
            ((OrdersView)mainFrame.panels[2]).updateProductsBox(productsOperations.getProducts());

    }
}
