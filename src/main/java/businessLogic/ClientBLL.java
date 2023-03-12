package businessLogic;

import connection.ConnectionFactory;
import model.Client;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import connection.DataAccessClass;
import presentationLayer.*;


public class ClientBLL {
    private ArrayList<Client> clients;
    private DataAccessClass<Client> clientDAO;
    private MainFrame mainFrame;
    private int newID;


    public int getNewID()
    {
        return newID;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     * Creates a new instance of ClientBLL.
     * @param connectionFactory is the object holding the connection to the database.
     * @param mainFrame is the main window of the application, which will have to be updated in this class when client changes are made.
     */

    public ClientBLL(ConnectionFactory connectionFactory, MainFrame mainFrame)
    {
        this.mainFrame=mainFrame;
        clientDAO = new DataAccessClass<Client>(connectionFactory, new Client(), mainFrame);
        clients= clientDAO.findAll();
        if(clients.isEmpty())
            newID = 1;
        else
            newID=clients.get(clients.size()-1).getId()+1;
    }


    /**
     * Validates the input provided by the user when trying to edit/add a client.
     * If the input is correct, it edits/inserts the new client and makes the necessary updates in the GUI and in the database.
     * @param id provided a valid integer
     * @param name checked to be not empty and containing only letters and spaces.
     * @param address can be null and can contain any character.
     * @param phone can be null and if it is not null must have exactly 10 digits.
     * @param email checked to be not empty and have a valid email format.
     * @param dateOfBirth can be null, if not null then it must have the format 'yyyy-mm-dd'.
     * @return -1 if the input is correct and the updates were made, 0 if the name is incorrect, 1 if the phone is incorrect, 2 if the email is incorrect and 3 if the date of birth is incorrect.
     */
    public int validateClient(String id, String name, String address, String phone, String email, String dateOfBirth)
    {
        if(name==null || name.isEmpty()) return 0;
        char[] chars = name.toCharArray();
        for(char c : chars)
            if(!Character.isLetter(c) && c!=' ') return 0;

        if(phone != null && !phone.isEmpty()) {
            if(phone.length()!=10) return 1;
            for(int i=0;i<9;i++)
                if(!Character.isDigit(phone.charAt(i))) return 1;
        }

        if(email != null && !email.isEmpty()) {
            if(!validateEmail(email)) return 2;
        } else return 2;

        if(dateOfBirth!= null && !dateOfBirth.isEmpty()) {
            if(dateOfBirth.length()!=10) return 3;
            char[] chars2 = dateOfBirth.toCharArray();
            if (chars2[4]!='-' || chars2[7]!='-') return 3;
            try {
                int year=Integer.parseInt(dateOfBirth.substring(0,4));
                int month=Integer.parseInt(dateOfBirth.substring(5,7));
                int day=Integer.parseInt(dateOfBirth.substring(8,10));
                if(year>2022 || year<1920) return 3;
                if(month>12 || month<1) return 3;
                if(day>31 || day<1) return 3;
            } catch (NumberFormatException e) { return 3; }
        }

        Client client = new Client(Integer.valueOf(id), name, address, phone, email, dateOfBirth);
        updateClientsList(client);

        return -1;
    }

    private void updateClientsList(Client client)
    {
        int i=0;
        while(i<clients.size() && clients.get(i).getId()!=client.getId())
            i++;

        if(i==clients.size()) //new client
        {
            clients.add(client);
            newID++;
            clientDAO.insert(client);
        }
        else
        {
            clients.set(i,client);
            clientDAO.update(client);
        }
        ((ClientsView)mainFrame.panels[0]).updateTable(clients);
        ((OrdersView)mainFrame.panels[2]).updateClientsBox(clients);
    }

    /**
     * Deletes the client from the application and makes the updates in the GUI and database.
     * @param id the id of the client that will be deleted, provided it is a valid id.
     */
    public void deleteClient(int id)
    {
        int i=0;
        while(i<clients.size() && clients.get(i).getId()!=id)
            i++;
        if ( clientDAO.deleteByID(id) != -2 ){
            clients.remove(i);
            ((ClientsView)mainFrame.panels[0]).updateTable(clients);
            ((OrdersView)mainFrame.panels[2]).updateClientsBox(clients);
        }
    }


    private boolean validateEmail(String input)
    {
        String emailFormat = "^[A-Z0-9._]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailFormat,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }
}