package presentationLayer;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class ClientsView extends JPanel {
    private int nrButtons;
    public ArrayList<JButton> buttons;
    public JTable clientsTable;
    private final DefaultTableModel tableModel;
    public ArrayList<JTextField> fields;
    private final int nrWrongLabels;
    public ArrayList<JLabel> wrongLabels;


    /**
     * Creates a new instance of ClientsView.
     * @param height height of the panel
     * @param width width of the panel
     */
    public ClientsView(int height, int width)
    {
        super();
        this.setBounds(0,0, width, height);
        this.setLayout(null);
        this.setBackground(Color.DARK_GRAY);
        nrButtons=7;
        buttons=new ArrayList<>();

        for(int i=0;i<nrButtons;i++)
        {
            JButton button=new JButton();
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("TimesRoman",20,20));
            buttons.add(button);
            this.add(button);
        }

        buttons.get(0).setText("CLIENTS");
        buttons.get(0).setBounds(100,60, 150, 50);
        buttons.get(1).setText("PRODUCTS");
        buttons.get(1).setBounds(300, 60, 150, 50);
        buttons.get(2).setText("ORDERS");
        buttons.get(2).setBounds(500, 60, 150, 50);
        buttons.get(3).setText("EDIT CLIENT");
        buttons.get(3).setBounds(970, 150, 150, 50);
        buttons.get(4).setText("DELETE CLIENT");
        buttons.get(4).setBounds(1140, 150, 200, 50);
        buttons.get(5).setText("ADD CLIENT");
        buttons.get(5).setBounds(1360, 150, 200, 50);
        buttons.get(6).setText("SAVE");
        buttons.get(6).setBounds(1360, 700, 200, 50);

        int nrDataLabels = 6;
        JLabel[] dataLabels = new JLabel[nrDataLabels];
        for(int i = 0; i< nrDataLabels; i++)
        {
            dataLabels[i]=new JLabel();
            dataLabels[i].setFont(new Font("TimesRoman",20,20));
            dataLabels[i].setForeground(Color.WHITE);
            this.add(dataLabels[i]);
            dataLabels[i].setVisible(true);
        }

        dataLabels[5].setText("Id:");
        dataLabels[5].setBounds(970,250,150,30);
        dataLabels[0].setText("Name:");
        dataLabels[0].setBounds(970,330,150,30);
        dataLabels[1].setText("Address:");
        dataLabels[1].setBounds(970,410,150,30);
        dataLabels[2].setText("Phone number:");
        dataLabels[2].setBounds(970,490,150,30);
        dataLabels[3].setText("Email:");
        dataLabels[3].setBounds(970,570,150,30);
        dataLabels[4].setText("DateOfBirth:");
        dataLabels[4].setBounds(970,650,150,30);

        int nrFields = 6;
        fields = new ArrayList<>();

        for(int i = 0; i< nrFields; i++)
        {
            JTextField field=new JTextField();
            field.setFont(new Font("TimesRoman",20,20));
            field.setForeground(Color.WHITE);
            field.setOpaque(false);
            this.add(field);
            fields.add(field);
        }

        fields.get(5).setBounds(1120,245,440,45);
        fields.get(5).setEditable(false);
        fields.get(0).setBounds(1120,325,440,45);
        fields.get(1).setBounds(1120,405,440,45);
        fields.get(2).setBounds(1120,485,440,45);
        fields.get(3).setBounds(1120,565,440,45);
        fields.get(4).setBounds(1120,645,440,45);

        Border border = BorderFactory.createEmptyBorder();

        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.DARK_GRAY);
        tablePanel.setBounds(50,150,900, 650);
        tablePanel.setBorder(border);

        tableModel= new DefaultTableModel();

        clientsTable=new JTable();
        clientsTable.setModel(tableModel);
        clientsTable.setBackground(Color.DARK_GRAY);
        clientsTable.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        clientsTable.setForeground(Color.WHITE);
        clientsTable.setFont(new Font("TimesRoman",20,20));
        clientsTable.setRowHeight(40);
        clientsTable.getTableHeader().setFont(new Font("TimesRoman",20,20));
        clientsTable.getTableHeader().setBackground(Color.DARK_GRAY);
        clientsTable.getTableHeader().setForeground(Color.WHITE);
        clientsTable.setPreferredScrollableViewportSize(new Dimension(900,650));
        clientsTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(clientsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(870,640));
        scrollPane.setBorder(border);
        tablePanel.add(scrollPane);
        this.add(tablePanel);

        nrWrongLabels=5;
        wrongLabels=new ArrayList<>();

        for(int i=0;i<nrWrongLabels;i++)
        {
            JLabel wrongLabel;
            wrongLabel=new JLabel();
            wrongLabel.setFont(new Font("TimesRoman",20,20));
            wrongLabel.setForeground(Color.RED);
            this.add(wrongLabel);
            wrongLabels.add(wrongLabel);
            wrongLabel.setVisible(false);
        }

        wrongLabels.get(0).setText("*please introduce only letters (and spaces)");
        wrongLabels.get(0).setBounds(1120,290,440,30);
        wrongLabels.get(1).setText("*please introduce a valid phone number");
        wrongLabels.get(1).setBounds(1120,450,440,30);
        wrongLabels.get(2).setText("*please introduce a valid email");
        wrongLabels.get(2).setBounds(1120,530,440,30);
        wrongLabels.get(3).setText("*please introduce a valid date");
        wrongLabels.get(3).setBounds(1120,610,440,30);
        wrongLabels.get(4).setText("*please select a row");
        wrongLabels.get(4).setBounds(970,200,440,30);
    }


    public void addButtonListener(ActionListener listener, int nrOfTheButton)
    {
        if(nrOfTheButton<nrButtons)
            buttons.get(nrOfTheButton).addActionListener(listener);
    }


    public int getNrButtons() {
        return nrButtons;
    }


    /**
     * receives a list of objects and creates the header of the table by accessing the fields of the elements of data through reflection, and then adds entries in the table.
     * @param data list of objects
     */
    public void updateTable(ArrayList<?> data)
    {
        ArrayList<String> columns=new ArrayList<>();
        if ( data.isEmpty() ){
            tableModel.removeRow(0);
            clientsTable.setModel(tableModel);
            return;
        }
        Class cls = data.get(0).getClass();
        ArrayList<Method> readMethods = new ArrayList<>();

        for(Field field: cls.getDeclaredFields()) {
            columns.add(field.getName());
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(),cls);
                readMethods.add(propertyDescriptor.getReadMethod());
            } catch (Exception e) { System.out.println("ERROR updating Table Clients\n"); e.printStackTrace();}
        }
        tableModel.setColumnCount(0);
        for(int i=0;i<columns.size();i++)
            tableModel.addColumn(columns.get(i));
        int nr= tableModel.getRowCount();
        for(int i=nr-1;i>=0;i--)
            tableModel.removeRow(i);
        Object[] values= new Object[columns.size()];
        try{
            for(int i=0;i< data.size();i++) {
                for(int j=0;j<readMethods.size();j++) {
                    values[j] = (readMethods.get(j)).invoke(data.get(i));
                }
                tableModel.addRow(values);
            }
        } catch (Exception e) { System.out.println("ERROR updating Table Clients\n"); e.printStackTrace(); }
        clientsTable.setModel(tableModel);
    }

    /**
     * attempts to make a label in the panel visible or not.
     * @param visible true to be visible, false if not.
     * @param nrOfTheLabel the index of the label in the wrongLabels list.
     * @param all true if all the labels to be set visible/unvisible, false if only one of them.
     */
    public void setWrongLabelVisible(boolean visible, int nrOfTheLabel, boolean all)
    {
        if(all)
            for(int i=0;i<nrWrongLabels;i++)
                wrongLabels.get(i).setVisible(visible);
        else
            wrongLabels.get(nrOfTheLabel).setVisible(visible);
    }


    public void updateFieldsToEdit(int row, boolean filled)
    {
        if(filled)
        {
            fields.get(0).setText((String)(clientsTable.getValueAt(row,1)));
            fields.get(1).setText((String)(clientsTable.getValueAt(row,2)));
            fields.get(2).setText((String)(clientsTable.getValueAt(row,3)));
            fields.get(3).setText((String)(clientsTable.getValueAt(row,4)));
            fields.get(4).setText((String)(clientsTable.getValueAt(row,5)));
            fields.get(5).setText(String.valueOf(clientsTable.getValueAt(row,0)));
        }
        else
        {
            fields.get(0).setText(null);
            fields.get(1).setText(null);
            fields.get(2).setText(null);
            fields.get(3).setText(null);
            fields.get(4).setText(null);
            fields.get(5).setText(null);
        }
    }

}