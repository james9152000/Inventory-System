/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import static jframe.sale_form.conn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pc
 */
public class report_data_form extends javax.swing.JFrame {

    static Connection conn;
    static Statement stm;
    static ResultSet rs;
   
   DefaultTableModel dtm = new DefaultTableModel();
   DefaultTableModel dtmStock = new DefaultTableModel();
   
  int xMouse;
  int yMouse;
    
    public report_data_form() {
        initComponents(); 
        connectionDB();
        readData();
        readDataStock();
        getdataSale();
        getdataStock();
     
    }
        //IMAGINARY TABLE FOR SALE DATABASE
    private DefaultTableModel getdataSale(){ 
            // ADD COLUMN IN TABLE   // THIS WILL BE THE COLUMN IN EXCEL FILE
        dtm.addColumn("id");
        dtm.addColumn("Customer Name");
        dtm.addColumn("Product Id");
        dtm.addColumn("Product Name");
        dtm.addColumn("Quantity");
        dtm.addColumn("Price");
        dtm.addColumn("Total");
        dtm.addColumn("Date");
        
            //GETTING DATA IN SALE TABLE IN THE DATABASE INVENTORY
            try{
            String strCommand = "SELECT id, costumer_name, product_id, product_name, quantity, price, total, date FROM sale";
            rs = stm.executeQuery(strCommand);
            
            while(rs.next()){ //LOOP TO GET ALL DATA IN DATABASE
                    //AND STORE IT TO VARIABLE
                String id = rs.getString("id");
                String costumer = rs.getString("costumer_name");
                String product_id = rs.getString("product_id");
                String product_name = rs.getString("product_name");
                String quantity = rs.getString("quantity");
                String price = rs.getString("price");
                String total = rs.getString("total");
                String date = rs.getString("date");
                    //ALL VARIABLE STORE IN ROW OBJECT[]
                Object[] row = {id, costumer, product_id, product_name, quantity, price,total, date};    
                dtm.addRow(row); //AND ADD TO DTM ROW
            }//CONTINUE UNTIL ALL THE DATA ADD TO ROW
            return dtm; //RETURN THE DTM THAT HOLD ALL THE DATA
        }catch(Exception e){
            
        }
        
        return null;
    }
    
     private DefaultTableModel getdataStock(){
         
         
        // ADD COLUMN IN TABLE
        dtmStock.addColumn("id");
        dtmStock.addColumn("Product Name");
        dtmStock.addColumn("Price");
        dtmStock.addColumn("Stock");
        dtmStock.addColumn("Total");
        dtmStock.addColumn("Updated");
        
            try{
            String strCommand = "SELECT id, name, price, stock, total, updated FROM stock";
            rs = stm.executeQuery(strCommand);
            
            while(rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("name");
                String price = rs.getString("price");
                String stock = rs.getString("stock");
                String total = rs.getString("total");
                String updated = rs.getString("updated");
            
                
                Object[] row = {id, name, price, stock, total, updated};    
                dtmStock.addRow(row);
            }
            return dtmStock;
        }catch(Exception e){
            
        }
        
        return null;
    }
    
    //GET DATA FROM DTM
    private String getCellValue(int x, int y){ // THIS IS FOR GETTING THE COLUMN AND ROW OF THE TABLE
        return dtm.getValueAt(x, y).toString(); // AND RETURN IT
    }
    
    //WRITE TO EXCELL SALE DATABASE
    private void writeToExcel(){
            
        JFileChooser excelFileChooser = new JFileChooser("");
        excelFileChooser.setDialogTitle("Save As");
        
        FileNameExtensionFilter fileExtension = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(fileExtension);
        int excelChooser = excelFileChooser.showSaveDialog(null);
        
        if(excelChooser == JFileChooser.APPROVE_OPTION){
        
        XSSFWorkbook wb = new XSSFWorkbook(); // CREATE WORKBOOK
        XSSFSheet ws = wb.createSheet("edit this sheet"); // CREATE WORKSHEET
         
        //LOAD DATA TO TREEMAP
        TreeMap<String,Object[]> data = new TreeMap<>();
        //ADD COLUMN HEADERS
        data.put("-1", new Object[]{dtm.getColumnName(0), dtm.getColumnName(1), dtm.getColumnName(2), dtm.getColumnName(3), dtm.getColumnName(4), dtm.getColumnName(5), dtm.getColumnName(6), dtm.getColumnName(7)});
        
        for(int i = 0; i < dtm.getRowCount(); i++){
         data.put(Integer.toString(i), new Object[]{getCellValue(i, 0),getCellValue(i, 1),getCellValue(i, 2),getCellValue(i, 3),getCellValue(i, 4),getCellValue(i, 5),getCellValue(i, 6),getCellValue(i, 7)});
        }
        //WRITE TO EXCEL
        Set<String> ids = data.keySet();
        XSSFRow row;
        int rowID = 0;
        
        for(String key: ids){
            row = ws.createRow(rowID++);
            
            //GET DATA AS PER KEY
            Object[] values = data.get(key);
            int cellID = 0;
            for(Object o: values){
                
                Cell cell = row.createCell(cellID++);
                cell.setCellValue(o.toString());
            }
        }
        try{
            FileOutputStream fos = new FileOutputStream(excelFileChooser.getSelectedFile() + ".xlsx");
            wb.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(this, "Success to export data");
        }catch(FileNotFoundException ex){
           JOptionPane.showMessageDialog(this, ex);
        }catch(IOException ex){
         JOptionPane.showMessageDialog(this, ex);
        }
        }// END OF IF STATEMENT
        else{
            
        }
    }
    
        //GET DATA FROM DTMSTOCK
    private String getCellValueStock(int x, int y){
        return dtmStock.getValueAt(x, y).toString();
    }
    
     //WRITE TO EXCELL SALE DATABASE
    private void writeToExcelStock(){
        
        JFileChooser excelFileChooser = new JFileChooser("");
        excelFileChooser.setDialogTitle("Save As");
        
        FileNameExtensionFilter fileExtension = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(fileExtension);
        int excelChooser = excelFileChooser.showSaveDialog(null);
        
        if(excelChooser == JFileChooser.APPROVE_OPTION){
        
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet("edit this sheet");
         
        //LOAD DATA TO TREEMAP
        TreeMap<String,Object[]> data = new TreeMap<>();
        //ADD COLUMN HEADERS
        data.put("-1", new Object[]{dtmStock.getColumnName(0), dtmStock.getColumnName(1), dtmStock.getColumnName(2), dtmStock.getColumnName(3), dtmStock.getColumnName(4), dtmStock.getColumnName(5)});
        
        for(int i = 0; i < dtmStock.getRowCount(); i++){
         data.put(Integer.toString(i), new Object[]{getCellValueStock(i, 0),getCellValueStock(i, 1),getCellValueStock(i, 2),getCellValueStock(i, 3),getCellValueStock(i, 4),getCellValueStock(i, 5)});
        }
        //WRITE TO EXCEL
        Set<String> ids = data.keySet();
        XSSFRow row;
        int rowID = 0;
        
        for(String key: ids){
            row = ws.createRow(rowID++);
            
            //GET DATA AS PER KEY
            Object[] values = data.get(key);
            int cellID = 0;
            for(Object o: values){
                
                Cell cell = row.createCell(cellID++);
                cell.setCellValue(o.toString());
            }
        }
        try{
            FileOutputStream fos = new FileOutputStream(excelFileChooser.getSelectedFile() + ".xlsx");
            wb.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(this, "Success to export data");
        }catch(FileNotFoundException ex){
           JOptionPane.showMessageDialog(this, ex);
        }catch(IOException ex){
         JOptionPane.showMessageDialog(this, ex);
        }
        }// END OF IF STATEMENT
        else{
            
        }
    }

    public void connectionDB(){
        try{
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory","root","1234");
        stm = conn.createStatement();
        
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "No connection");
        }
    }
    
    public void readData(){
        try{
            String strCommand = "SELECT id, costumer_name, product_id, product_name, quantity, price, total, date FROM sale";
            rs = stm.executeQuery(strCommand);
            
            while(rs.next()){
                String id = rs.getString("id");
                String costumer = rs.getString("costumer_name");
                String product_id = rs.getString("product_id");
                String product_name = rs.getString("product_name");
                String quantity = rs.getString("quantity");
                String price = rs.getString("price");
                String total = rs.getString("total");
                String date = rs.getString("date");
                
                Object[] row = {id, costumer, product_id, product_name, quantity, price,total, date};
                DefaultTableModel model = (DefaultTableModel) tblSale.getModel();
                model.addRow(row);
            }
        }catch(Exception e){
            
        }
    }
    
        public void readDataStock(){
        
        try{
            String strCommand = "SELECT id, name, price, stock, total, updated FROM stock";
            rs = stm.executeQuery(strCommand);
            
            while(rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("name");
                String price = rs.getString("price");
                String stock = rs.getString("stock");
                String total = rs.getString("total");
                String updated = rs.getString("updated");
                
                Object[] row = {id, name, price, stock, total, updated};
                DefaultTableModel model = (DefaultTableModel) tblStock.getModel();
                model.addRow(row);
                
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Can't read data please check connection "+e);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drag = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblMinimize1 = new javax.swing.JLabel();
        lblClose1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSale = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

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

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(1283, 600));

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addContainerGap(1194, Short.MAX_VALUE)
                .addComponent(lblMinimize1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                    .addContainerGap(1234, Short.MAX_VALUE)
                    .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMinimize1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelHeaderLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblClose1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabel12.setBackground(new java.awt.Color(153, 153, 153));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("BACK");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N

        tblStock.setBackground(new java.awt.Color(153, 153, 153));
        tblStock.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        tblStock.setForeground(new java.awt.Color(255, 255, 255));
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Product Name", "Price", "Stock", "Total", "Updated"
            }
        ));
        tblStock.setRowHeight(30);
        tblStock.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblStock.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblStock.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblStock);

        jTabbedPane1.addTab("Stock", jScrollPane2);

        tblSale.setBackground(new java.awt.Color(153, 153, 153));
        tblSale.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        tblSale.setForeground(new java.awt.Color(255, 255, 255));
        tblSale.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Customer Name", "Product Id", "Product Name", "Quantity", "Price", "Total", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblSale.setRowHeight(30);
        tblSale.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblSale.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblSale.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSale.getTableHeader().setResizingAllowed(false);
        tblSale.getTableHeader().setReorderingAllowed(false);
        tblSale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSaleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSale);

        jTabbedPane1.addTab("Sale", jScrollPane1);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("SALE");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 51), 2));
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("STOCK");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 255), 2));
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Export Data");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(57, 57, 57)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 561, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(drag, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1283, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(drag, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 558, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblSaleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSaleMouseClicked
       // int selectRow = tblSale.getSelectedRow();
       // TableModel model = tblSale.getModel();

       // txtPowerId.setText(model.getValueAt(selectRow, 0).toString());
       // txtCustomer.setText(model.getValueAt(selectRow, 1).toString());
       // txtId.setText(model.getValueAt(selectRow, 2).toString());
       // txtProductname.setText(model.getValueAt(selectRow, 3).toString());
       // txtQuantity.setText(model.getValueAt(selectRow, 4).toString());
       // txtPrice.setText(model.getValueAt(selectRow, 5).toString());
       // txtTotal.setText(model.getValueAt(selectRow, 6).toString());
       // txtDate.setText(model.getValueAt(selectRow, 7).toString());

    }//GEN-LAST:event_tblSaleMouseClicked

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

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
         writeToExcel();
    }//GEN-LAST:event_jLabel2MouseClicked

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

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
       writeToExcelStock();
    }//GEN-LAST:event_jLabel1MouseClicked

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
            java.util.logging.Logger.getLogger(report_data_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(report_data_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(report_data_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(report_data_form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new report_data_form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel drag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblClose1;
    private javax.swing.JLabel lblMinimize1;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JTable tblSale;
    private javax.swing.JTable tblStock;
    // End of variables declaration//GEN-END:variables
}
