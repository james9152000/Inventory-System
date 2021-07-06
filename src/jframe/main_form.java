/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author JAMES DE GUZMAN
 */
public class main_form extends javax.swing.JFrame {

   static Connection conn;
   static Statement stm;
   static ResultSet rs;
    int xMouse;
    int yMouse;
    
    public main_form() {
        initComponents();
       
        connectionDB(); //CALLING FUNCTION    
        readProduct(); 
    }
    
    public void connectionDB(){ // CONNECTING INTO THE DATABASE
    
        try{
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","1234"); 
        stm = conn.createStatement();
       
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "No connection"+ e);
        }
    }
    
    public void readProduct(){// GETTING DATA IN DATABASE AND SHOW IN THE TABLE
        
        try{
            String strCommand = "SELECT id, name, price FROM stock";
            rs = stm.executeQuery(strCommand);
            
            while(rs.next()){ 
                String id = rs.getString("id");    
                String name = rs.getString("name");
                int price = rs.getInt("price");
                
                Object[] row = {id, name, price};
                DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();
                model.addRow(row);
            }
           
        }catch(Exception e){
            
        }//TRY CATCH
    }

   public void addBuy(){ // ADDING THE PURCHASED PRODUCT IN TABLE
       
       String id = txtId.getText();                        // GETTING THE VALUE
       String name = txtName.getText();
                                                          //COMPUTE TOTAL PRICE
       int price = Integer.parseInt(txtPrice.getText());
       int quantity = Integer.parseInt(txtQuantity.getText());
       int total = price * quantity;         // AND ASSING IT IN A NEW VARIABLE
      
                                                    //CONVERT INT TYPE TO STRING 
       String strPrice = Integer.toString(price);
       String strQuantity = Integer.toString(quantity);
       String strTotal = Integer.toString(total);
                                         // STORE ALL THE VALUE IN OBJECT[] ROW
       Object[] row = {id,name, strPrice, strQuantity, strTotal};
       DefaultTableModel model = (DefaultTableModel) tblBuy.getModel(); // GET TABLE MODEL
       model.addRow(row);   // ADDING ROW IN THE TABLE MODEL
       
       
                            // CREAR TEXT AFTER ADDING DATA
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
            
   }                            // ADDING THE PURCHASED PRODUCT IN TABLE
   
   public void totalAmount(){ // COMPUTE ALL TOTAL PURCHASED
       
       int amount = 0; // FOR EACH ROW DATA
       int total = 0; // FOR SUM OF ALL DATA IN ROW
       int i = 0;
       TableModel model = tblBuy.getModel(); //GETTING TABLE MODEL
       
       for(i = 0; i < tblBuy.getRowCount(); i++){
           amount = Integer.parseInt(model.getValueAt(i, 4)+""); //GETTING THE VALUE IN SPECIFIC ROW
           total += amount; //STORE THE VALUE IN TOTAL 
       } //REPEAT UNTIL THE CONDITION MET
       txtAmount.setText(total+""); //SET THE COMPUTE DATA
                    //RESETING VARIABLE VALUE
       i = 0;
       amount = 0;
       total = 0;
   }
   
   public void insertSale(){ //INSERTING DATA INTO THE DATABASE
       
       TableModel model = tblBuy.getModel(); // GETTING TABLE MODEL
                            //LOCAL VARIABLE
       int i = 0;           //START OF THE ROW COUNT
       int product_id = 0;
       int quantity = 0;
       int price = 0;
       int total = 0;
      
       for(i = 0; i < tblBuy.getRowCount(); i++){
           product_id = Integer.parseInt(model.getValueAt(i, 0)+"");             //STORE DATA THAT FROM INTO THE TABLE
           String product_name = model.getValueAt(i, 1).toString();              // i IS THE SPECIFIC ROW
           quantity = Integer.parseInt(model.getValueAt(i, 3)+"");               //0, 1, 3, ... INDEX OF COMLUMN THAT FROM INTO THE TABLE
           price = Integer.parseInt(model.getValueAt(i, 2)+"");
           total = Integer.parseInt(model.getValueAt(i, 4)+"");
           try{
           connectionDB(); //OPENING CONNECTION
           String strCommand = "INSERT INTO sale(costumer_name, product_id, product_name, quantity, price, total) VALUES('"+txtCustomer.getText()+"','"+product_id+"','"+product_name+"','"+quantity+"','"+price+"','"+total+"')";
           stm.execute(strCommand);
           stm.close(); //CLOSE CONNECTION
           
      
       }catch(Exception e){
             JOptionPane.showMessageDialog(this, "Failed to add on database please check here : "+e);
       }
           
            } // LOOP END
       JOptionPane.showMessageDialog(this, "Success transaction!");
   }
   
   public void updateStock(){
       
       TableModel model = tblBuy.getModel();
       
       
       try{
           connectionDB();
           for(int i = 0; i < tblBuy.getRowCount(); i++){
               int id = Integer.parseInt(model.getValueAt(i, 0).toString()); // GETTING THE ID IN THE TABLE
               int stock = Integer.parseInt(model.getValueAt(i, 3).toString());
               String strCommand1 = "SELECT stock FROM stock WHERE id = '"+id+"' ";
               rs = stm.executeQuery(strCommand1);
            while(rs.next()){
                connectionDB();
               int oldStock = rs.getInt("stock");
               int newStock = oldStock - stock;
               
               String strCommand = "UPDATE stock SET stock = '"+newStock+"' WHERE id = '"+id+"'";
               stm.execute(strCommand);
               
               
           }
           }
           stm.close();
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "There was a problem in updating data : "+e);
       }
            
   }//updateStock
   

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMain = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblMinimize1 = new javax.swing.JLabel();
        lblClose1 = new javax.swing.JLabel();
        spProduct = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        txtName = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        spBuy = new javax.swing.JScrollPane();
        tblBuy = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        txtCash = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtChange = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnProcess = new javax.swing.JButton();
        txtQuantity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        drag = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);

        panelMain.setBackground(new java.awt.Color(102, 102, 102));

        panelHeader.setBackground(new java.awt.Color(255, 255, 255));

        lblMinimize1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblMinimize1.setForeground(new java.awt.Color(0, 0, 0));
        lblMinimize1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMinimize1.setText("-");
        lblMinimize1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblMinimize1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMinimize1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMinimize1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMinimize1MouseClicked(evt);
            }
        });

        lblClose1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblClose1.setForeground(new java.awt.Color(51, 51, 51));
        lblClose1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClose1.setText("X");
        lblClose1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblClose1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblClose1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblClose1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelHeaderLayout.createSequentialGroup()
                    .addGap(1190, 1190, 1190)
                    .addComponent(lblMinimize1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(50, Short.MAX_VALUE)))
            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                    .addContainerGap(1234, Short.MAX_VALUE)
                    .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelHeaderLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblMinimize1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelHeaderLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tblProduct.setBackground(new java.awt.Color(153, 153, 153));
        tblProduct.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblProduct.setForeground(new java.awt.Color(255, 255, 255));
        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Product Name", "Price"
            }
        ));
        tblProduct.setRowHeight(26);
        tblProduct.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblProduct.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblProduct.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblProduct.getTableHeader().setResizingAllowed(false);
        tblProduct.getTableHeader().setReorderingAllowed(false);
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        spProduct.setViewportView(tblProduct);

        txtName.setEditable(false);
        txtName.setBackground(new java.awt.Color(204, 204, 204));
        txtName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtName.setForeground(new java.awt.Color(102, 102, 102));
        txtName.setSelectionColor(new java.awt.Color(204, 204, 204));

        txtPrice.setEditable(false);
        txtPrice.setBackground(new java.awt.Color(204, 204, 204));
        txtPrice.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPrice.setForeground(new java.awt.Color(102, 102, 102));
        txtPrice.setSelectionColor(new java.awt.Color(204, 204, 204));

        btnAdd.setBackground(new java.awt.Color(102, 102, 102));
        btnAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAdd.setBorderPainted(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        tblBuy.setBackground(new java.awt.Color(153, 153, 153));
        tblBuy.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblBuy.setForeground(new java.awt.Color(255, 255, 255));
        tblBuy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Id", "Name", "Price", "Quantity", "Total"
            }
        ));
        tblBuy.setRowHeight(30);
        tblBuy.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblBuy.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblBuy.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblBuy.getTableHeader().setResizingAllowed(false);
        tblBuy.getTableHeader().setReorderingAllowed(false);
        tblBuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuyMouseClicked(evt);
            }
        });
        spBuy.setViewportView(tblBuy);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Total Amount");

        txtAmount.setEditable(false);
        txtAmount.setBackground(new java.awt.Color(204, 204, 204));
        txtAmount.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtAmount.setForeground(new java.awt.Color(102, 102, 102));
        txtAmount.setSelectionColor(new java.awt.Color(204, 204, 204));
        txtAmount.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                txtAmountComponentAdded(evt);
            }
        });
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
        });

        txtCash.setBackground(new java.awt.Color(255, 255, 255));
        txtCash.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtCash.setForeground(new java.awt.Color(102, 102, 102));
        txtCash.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                txtCashComponentAdded(evt);
            }
        });
        txtCash.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtCashInputMethodTextChanged(evt);
            }
        });
        txtCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCashActionPerformed(evt);
            }
        });
        txtCash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cash");

        txtChange.setEditable(false);
        txtChange.setBackground(new java.awt.Color(204, 204, 204));
        txtChange.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtChange.setForeground(new java.awt.Color(102, 102, 102));
        txtChange.setSelectionColor(new java.awt.Color(204, 204, 204));
        txtChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChangeActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Change");

        btnProcess.setBackground(new java.awt.Color(102, 102, 102));
        btnProcess.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnProcess.setForeground(new java.awt.Color(255, 255, 255));
        btnProcess.setText("Process");
        btnProcess.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProcess.setBorderPainted(false);
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        txtQuantity.setBackground(new java.awt.Color(255, 255, 255));
        txtQuantity.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtQuantity.setForeground(new java.awt.Color(102, 102, 102));
        txtQuantity.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtQuantityInputMethodTextChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Id");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Name");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Price");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quantity");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Costumer Name");

        txtCustomer.setBackground(new java.awt.Color(255, 255, 255));
        txtCustomer.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtCustomer.setForeground(new java.awt.Color(102, 102, 102));

        btnRemove.setBackground(new java.awt.Color(102, 102, 102));
        btnRemove.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRemove.setForeground(new java.awt.Color(255, 255, 255));
        btnRemove.setText("Remove");
        btnRemove.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRemove.setBorderPainted(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("BACK");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        drag.setBackground(new java.awt.Color(204, 255, 255));
        drag.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        drag.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                dragMouseDragged(evt);
            }
        });
        drag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dragMousePressed(evt);
            }
        });

        txtId.setEditable(false);
        txtId.setBackground(new java.awt.Color(204, 204, 204));
        txtId.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtId.setForeground(new java.awt.Color(102, 102, 102));
        txtId.setSelectionColor(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8)
                                    .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel5)))
                                .addGap(28, 28, 28))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(jLabel7))
                                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)))
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addComponent(spProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spBuy, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMainLayout.createSequentialGroup()
                    .addComponent(drag, javax.swing.GroupLayout.PREFERRED_SIZE, 1184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 96, Short.MAX_VALUE)))
            .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(spProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(spBuy))
                .addContainerGap())
            .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMainLayout.createSequentialGroup()
                    .addComponent(drag, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 568, Short.MAX_VALUE)))
            .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMainLayout.createSequentialGroup()
                    .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 561, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
           
        if (txtId.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Please insert product!");
        }else{
             if(txtQuantity.getText().equals("")){
                 JOptionPane.showMessageDialog(this, "Please check the quantity!");
            }else{
                addBuy();
                totalAmount();
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
     int amount = Integer.parseInt(txtAmount.getText());
       int cast = Integer.parseInt(txtCash.getText());
       int change = cast - amount;
       txtChange.setText(change+"");  
    }//GEN-LAST:event_txtAmountActionPerformed

    private void txtQuantityInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtQuantityInputMethodTextChanged
        
    }//GEN-LAST:event_txtQuantityInputMethodTextChanged

    private void txtCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCashActionPerformed
     
    }//GEN-LAST:event_txtCashActionPerformed

    private void txtCashInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtCashInputMethodTextChanged
     
    }//GEN-LAST:event_txtCashInputMethodTextChanged

    private void txtChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChangeActionPerformed
     
    }//GEN-LAST:event_txtChangeActionPerformed

    private void txtAmountComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_txtAmountComponentAdded
      
    }//GEN-LAST:event_txtAmountComponentAdded

    private void txtCashComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_txtCashComponentAdded
     
    }//GEN-LAST:event_txtCashComponentAdded

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
       
    }//GEN-LAST:event_txtAmountKeyReleased

    private void txtCashKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashKeyReleased
      
    }//GEN-LAST:event_txtCashKeyReleased

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed

      
       if(txtCash.getText().equals("")){
        JOptionPane.showMessageDialog(this, "Please input the cash");
            
       }else{
            int cash = Integer.parseInt(txtCash.getText());
       int amount = Integer.parseInt(txtAmount.getText());
       int change = 0;
            if(cash >= amount) { 
                change = cash - amount;
                txtChange.setText(change + "");
                insertSale();
                updateStock();
                txtAmount.setText("");
                txtCash.setText("");
                txtChange.setText("");
                DefaultTableModel model = (DefaultTableModel) tblBuy.getModel();
                model.setRowCount(0);
                }
            else{
                JOptionPane.showMessageDialog(this, "Insufficient cash");
            }

       }

      
    }//GEN-LAST:event_btnProcessActionPerformed

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
        int selectRow = tblProduct.getSelectedRow();
        TableModel model = tblProduct.getModel();
        txtId.setText(model.getValueAt(selectRow, 0).toString());
        txtName.setText(model.getValueAt(selectRow, 1).toString());
        txtPrice.setText(model.getValueAt(selectRow, 2).toString());
        
    }//GEN-LAST:event_tblProductMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        
        int selectRow = tblBuy.getSelectedRow();
        
        if(selectRow >= 0){
        DefaultTableModel model = (DefaultTableModel) tblBuy.getModel();
        model.removeRow(selectRow);
        totalAmount();
        }else{
            JOptionPane.showMessageDialog(this, "Please select item want to remove!");
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
         
         dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void dragMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dragMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - xMouse ,y - yMouse);

    }//GEN-LAST:event_dragMouseDragged

    private void dragMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dragMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_dragMousePressed

    private void lblMinimize1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMinimize1MouseClicked
        this.setState(this.ICONIFIED);
    }//GEN-LAST:event_lblMinimize1MouseClicked

    private void lblClose1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblClose1MouseClicked
        int response = JOptionPane.showConfirmDialog(this,"Are your sure to exit the program? " , "Exit" , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(response ==JOptionPane.YES_OPTION){
            this.dispose();
        }

        else if(response ==JOptionPane.NO_OPTION){
            //None for now
        }
    }//GEN-LAST:event_lblClose1MouseClicked

    private void tblBuyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuyMouseClicked
        btnRemove.setBackground(Color.red);
    }//GEN-LAST:event_tblBuyMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnProcess;
    private javax.swing.JButton btnRemove;
    private javax.swing.JLabel drag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblClose1;
    private javax.swing.JLabel lblMinimize1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMain;
    private javax.swing.JScrollPane spBuy;
    private javax.swing.JScrollPane spProduct;
    public static javax.swing.JTable tblBuy;
    private javax.swing.JTable tblProduct;
    public static javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtChange;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    // End of variables declaration//GEN-END:variables
}
