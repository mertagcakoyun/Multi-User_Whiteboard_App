import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class DrawFrame extends JFrame
{
    private JLabel stausLabel; //mouse koordinatlari
    private DrawPanel panel;
    
    private JButton undo; 
    private JButton redo;
    private JButton clear; 
    
    private JComboBox colors; 
    
    
    private String colorOptions[]=
    {"Black","Blue","Cyan","Dark Gray","Gray","Green","Light Gray",
        "Magenta","Orange","Pink","Red","White","Yellow"};
    
    
    private Color colorArray[]=
    {Color.BLACK , Color.BLUE , Color.CYAN , Color.darkGray , Color.GRAY , 
        Color.GREEN, Color.lightGray , Color.MAGENTA , Color.ORANGE , 
    Color.PINK , Color.RED , Color.WHITE , Color.YELLOW};
    
    private JComboBox shapes; 
    private String shapeOptions[]=
    {"Line","Rectangle","Oval"};
    private JCheckBox filled; 
    private JPanel widgetJPanel; 
    private JPanel widgetPadder;
    private JPanel usersJPanel;
    private JTextField allowedIPText;
    private JList<String> allowedIPList;
    private JButton addIPButton;
    private JButton deleteIPButton; 
    private DefaultListModel<String> allowedListModel = new DefaultListModel<>();
    private ArrayList<String> allowedArrayList;
    private IpAddressValidator validator;
    public DrawFrame() throws IOException
    {
        super("Multi-User paint app");         
        JLabel statusLabel = new JLabel( "" );
        panel = new DrawPanel(statusLabel); 
        undo = new JButton( "Undo" );
        redo = new JButton( "Redo" );
        clear = new JButton( "Clear" );
        //For connection and allowed lists -->  user components
        addIPButton = new JButton( "Add IP" );
        deleteIPButton = new JButton( "Delete IP" );
        allowedIPList = new JList<String>();
        allowedIPList.setModel(allowedListModel);        
        allowedArrayList = new ArrayList<>();
        allowedIPText = new JTextField(" . . .  : ");
        validator = new IpAddressValidator();
        //end
        colors = new JComboBox( colorOptions );
        shapes = new JComboBox( shapeOptions );
        filled = new JCheckBox( "Filled" );
        widgetJPanel = new JPanel();
        usersJPanel = new JPanel();
        usersJPanel.setLayout(new GridLayout(5, 1, 20, 2));
        widgetJPanel.setLayout( new GridLayout( 1, 6, 10, 5 ) );
        widgetPadder = new JPanel();
        widgetPadder.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 5));
        widgetJPanel.add( undo );
        widgetJPanel.add( redo );
        widgetJPanel.add( clear );
        widgetJPanel.add( colors );
        widgetJPanel.add( shapes );                 
        widgetJPanel.add( filled );
        widgetPadder.add( widgetJPanel );
        //usersJPanel.add(new JLabel("Please Enter Allowed \n IP address that you want "
             //  +"\n" + "\n With order like this 1.1.1.1/80"));
        usersJPanel.add(allowedIPText);
        usersJPanel.add(allowedIPList);
        usersJPanel.add(addIPButton);
        usersJPanel.add(deleteIPButton);
        add( widgetPadder, BorderLayout.NORTH);
        add( panel, BorderLayout.CENTER);
        add(usersJPanel, BorderLayout.EAST);
        allowedIPText.setPreferredSize( new Dimension( 75, 20 ) );
        ButtonHandler buttonHandler = new ButtonHandler();
        undo.addActionListener( buttonHandler );
        redo.addActionListener( buttonHandler );
        clear.addActionListener( buttonHandler );
        addIPButton.addActionListener(buttonHandler);
        deleteIPButton.addActionListener(buttonHandler);
        ItemListenerHandler handler = new ItemListenerHandler();
        colors.addItemListener( handler );
        shapes.addItemListener( handler );
        filled.addItemListener( handler );
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize( 700, 500 );
        setVisible( true );
        
    } 
    
    
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed( ActionEvent event )
        {
            if (event.getActionCommand().equals("Undo")){
                panel.clearLastShape();
            }
            else if (event.getActionCommand().equals("Redo")){
                panel.redoLastShape();
            }
            else if (event.getActionCommand().equals("Clear")){
                panel.clearDrawing();
            }
            else if (event.getActionCommand().equals("Add IP")){
                if (validator.isValid(allowedIPText.getText())) {
                   allowedArrayList.add(allowedIPText.getText());
                   allowedIPText.setText(" . . . ");
                   fillJList(); 
                }else{
                    JOptionPane.showMessageDialog(new JPanel(), "Please Enter Valid Ip and Port Adress");
                }
                
            }
            else if (event.getActionCommand().equals("Delete IP")){
                allowedArrayList.remove(allowedIPList.getSelectedIndex());
                fillJList();
            }
             
        } 

        private void fillJList() {
            allowedListModel.removeAllElements();
            for (int i = 0; i < allowedArrayList.size(); i++) {
                allowedListModel.addElement(allowedArrayList.get(i));
            }
        }
    } 
    private class ItemListenerHandler implements ItemListener
    {
        public void itemStateChanged( ItemEvent event )
        {
            
            if ( event.getSource() == filled )
            {
                boolean checkFill=filled.isSelected() ? true : false; //
                panel.setCurrentShapeFilled(checkFill);
            }
            
            
            if ( event.getStateChange() == ItemEvent.SELECTED )
            {
                
                if ( event.getSource() == colors)
                {
                    panel.setCurrentShapeColor
                        (colorArray[colors.getSelectedIndex()]);
                }
                
                
                else if ( event.getSource() == shapes)
                {
                    panel.setCurrentShapeType(shapes.getSelectedIndex());
                }
            }
            
        } 
    }
    
    
}