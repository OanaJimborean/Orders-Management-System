package presentationLayer;

import javax.swing.*;


public class MainFrame extends JFrame {
    private int screenHeight = 950;
    private int screenWidth = 1600;
    public JPanel[] panels;


    /**
     * Creates a new instance of MainFrame.
     * @param title the title of the frame
     */
    public MainFrame(String title)
    {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(150,50,screenWidth,screenHeight);
        this.setLayout(null);
        this.setVisible(true);
        this.setResizable(false);
        panels = new JPanel[3];
        panels[0]=new ClientsView(screenHeight, screenWidth);
        panels[1]=new ProductsView(screenHeight, screenWidth);
        panels[2]= new OrdersView(screenHeight,screenWidth);
        this.setContentPane(panels[0]);
        this.revalidate();
        this.repaint();
    }


    public void setPanel(int panelNumber)
    {
        this.setContentPane(panels[panelNumber]);
        this.revalidate();
        this.repaint();
    }
}
