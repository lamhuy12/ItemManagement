/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import DTO.SupplierDTO;
import DAO.SupplierDAO;
import DTO.ItemDTO;
import DAO.ItemDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import utils.MyConnnection;

public class Main extends javax.swing.JDialog {

    private int indexOldTab = 0;
    private final int SUPTAB = 0;
    private final int ITEMTAB = 1;

    public static boolean isLogin = false;

    Vector<String> headerSup = new Vector<>();
    Vector dataSup = new Vector();
    boolean addNewSup = false;
    DefaultTableModel defaulModelSup;
    SupplierDAO supplierDAO;

    final int SUCCESS = 1;

    Vector<String> headerItem = new Vector<>();
    Vector dataItem = new Vector();
    boolean addNewItem = false;
    DefaultTableModel defaultModelItem;
    ItemDAO itemDAO;

    public Main(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        parent.setVisible(false);

        initComponents();
        this.setLocationRelativeTo(null);
        lbWelcome.setText("Welcome " + parent.getName());

        headerSup.add("Code");
        headerSup.add("Name");
        headerSup.add("Address");
        loadSup();

        defaulModelSup = (DefaultTableModel) tblSupplier.getModel();
        defaulModelSup.setDataVector(dataSup, headerSup);

        headerItem.add("Code");
        headerItem.add("Name");
        headerItem.add("Supplier");
        headerItem.add("Unit");
        headerItem.add("Price");
        headerItem.add("Supplying");
        loadItem();

        defaultModelItem = (DefaultTableModel) tblItems.getModel();
        defaultModelItem.setDataVector(dataItem, headerItem);

        this.cbSupplier.setModel(new DefaultComboBoxModel(supplierDAO));
        txtSupCode.setEditable(false);
        txtItemCode.setEditable(false);
        tblSupplier.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    void loadSup() {
        if (!dataSup.isEmpty()) {
            dataSup.removeAll(dataSup);
        }

        try {
            supplierDAO = SupplierDAO.getAllSuppliers();
            for (SupplierDTO e : supplierDAO) {
                Vector row = new Vector();
                row.add(e.getSupCode());
                row.add(e.getSupName());
                row.add(e.getAddress());
                row.add(e.isColloborating());
                dataSup.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot load data");
        }
    }

    void loadItem() {

        if (!dataItem.isEmpty()) {
            dataItem.removeAll(dataItem);
        }

        try {
            itemDAO = ItemDAO.getAllItems();
            for (ItemDTO e : itemDAO) {
                Vector row = new Vector();
                row.add(e.getItemCode());
                row.add(e.getItemName());
                row.add(e.getSupplierDTO());
                row.add(e.getUnit());
                row.add(e.getPrice());
                row.add(e.isSupplying());
                dataItem.add(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot load Data");
        }
    }

    private boolean validSupInfo() {
        //check code
        String code = txtSupCode.getText().trim();
        if (code.equals("")) {
            lbSupCode.setText("Code cannot blank");
            return false;
        } else if (code.length() > 10 || code.length() < 0) {
            lbSupCode.setText("Chars must be from 1-10");
            return false;
        } else {
            lbSupAddress.setText("");
            lbSupName.setText("");
            lbSupCode.setText("");
        }

        //check name
        String name = txtSupName.getText().trim();
        if (name.equals("")) {
            lbSupName.setText("Name cannot blank");
            return false;
        } else if (name.length() > 50 || name.length() < 0) {
            lbSupName.setText("Chars must be from 1-50");
            return false;
        } else {
            lbSupAddress.setText("");
            lbSupName.setText("");
            lbSupCode.setText("");
        }

        //check address
        String address = txtSupAddress.getText().trim();
        if (address.equals("")) {
            lbSupAddress.setText("Address cannot blank");
            return false;
        } else if (address.length() > 50 || address.length() < 0) {
            lbSupAddress.setText("Chars must be from 1-50");
            return false;
        } else {
            lbSupAddress.setText("");
            lbSupName.setText("");
            lbSupCode.setText("");
        }
        return true;
    }

    private boolean validItemInfo() {
        String code = txtItemCode.getText().trim();
        if (code.equals("")) {
            lbItemCode.setText("Code cannot blank");
            return false;
        } else if (code.length() > 10 || code.length() < 0) {
            lbItemCode.setText("Chars must be from 1-10");
            return false;
        } else {
            lbItemCode.setText("");
            lbItemName.setText("");
            lbItemPrice.setText("");
            lbItemUnit.setText("");
        }

        String name = txtItemName.getText().trim();
        if (name.equals("")) {
            lbItemName.setText("Name cannot blank");
            return false;
        } else if (name.length() > 50 || name.length() < 0) {
            lbItemName.setText("Chars must be from 1-50");
            return false;
        } else {
            lbItemCode.setText("");
            lbItemName.setText("");
            lbItemPrice.setText("");
            lbItemUnit.setText("");
        }

        String unit = txtItemUnit.getText().trim();
        if (unit.equals("")) {
            lbItemUnit.setText("Unit cannot blank");
            return false;
        } else if (unit.length() > 50 || unit.length() < 0) {
            lbItemUnit.setText("Chars must be from 1-50");
            return false;
        } else {
            lbItemCode.setText("");
            lbItemName.setText("");
            lbItemPrice.setText("");
            lbItemUnit.setText("");
        }

        try {
            float price = Float.parseFloat(txtItemPrice.getText().trim());
            if (price < 0) {
                lbItemPrice.setText("Price must be >=0");
                return false;
            } if (price > Float.MAX_VALUE) {
                lbItemPrice.setText("Price too big");
                return false;
            } 
            else {
                lbItemCode.setText("");
                lbItemName.setText("");
                lbItemPrice.setText("");
                lbItemUnit.setText("");
            }
        } catch (NumberFormatException e) {
            lbItemPrice.setText("Must be number and cannot blank");
            return false;
        }

        if (cbSupplier.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Need supplier. Cannot blank");
            return false;
        }

        return true;
    }

    private void setNoneSup() {
        txtSupCode.setText("");
        txtSupCode.setEnabled(true);
        txtSupCode.requestFocus();
        txtSupCode.setEditable(true);
        txtSupAddress.setText("");
        txtSupName.setText("");
        lbSupAddress.setText("");
        lbSupCode.setText("");
        lbSupName.setText("");
        tblSupplier.clearSelection();
        chkSupColloborating.setSelected(false);
    }

    private void setNoneItem() {
        txtItemCode.setText("");
        txtItemCode.setEnabled(true);
        txtItemCode.requestFocus();
        txtItemCode.setEditable(true);
        txtItemName.setText("");
        txtItemPrice.setText("");
        txtItemUnit.setText("");
        lbItemCode.setText("");
        lbItemName.setText("");
        lbItemPrice.setText("");
        lbItemUnit.setText("");
        tblItems.clearSelection();
        chkitemSupplying.setSelected(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tabAll = new javax.swing.JTabbedPane();
        Suppliers = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSupCode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtSupName = new javax.swing.JTextField();
        a = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSupAddress = new javax.swing.JTextField();
        btnSupNew = new javax.swing.JButton();
        btnSupSave = new javax.swing.JButton();
        btnSupDelete = new javax.swing.JButton();
        lbSupCode = new javax.swing.JLabel();
        lbSupName = new javax.swing.JLabel();
        lbSupAddress = new javax.swing.JLabel();
        chkSupColloborating = new javax.swing.JCheckBox();
        btnLogoutSup = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        b = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        txtItemUnit = new javax.swing.JTextField();
        lbItemSup = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtItemPrice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        chkitemSupplying = new javax.swing.JCheckBox();
        btnItemAddNew = new javax.swing.JButton();
        btnItemSave = new javax.swing.JButton();
        btnItemDelete = new javax.swing.JButton();
        lbItemCode = new javax.swing.JLabel();
        lbItemName = new javax.swing.JLabel();
        lbItemUnit = new javax.swing.JLabel();
        lbItemPrice = new javax.swing.JLabel();
        btnLogoutItem = new javax.swing.JButton();
        lbWelcome = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabAllMouseClicked(evt);
            }
        });

        Suppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SuppliersMouseClicked(evt);
            }
        });

        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplier.getTableHeader().setReorderingAllowed(false);
        tblSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierMouseClicked(evt);
            }
        });
        tblSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblSupplierKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplier);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Code:");

        jLabel2.setText("Name:");

        txtSupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupNameActionPerformed(evt);
            }
        });

        a.setText("Address:");

        jLabel3.setText("Colloborating:");

        btnSupNew.setText("Add New");
        btnSupNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupNewActionPerformed(evt);
            }
        });

        btnSupSave.setText("Save");
        btnSupSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupSaveActionPerformed(evt);
            }
        });

        btnSupDelete.setText("Delete");
        btnSupDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupDeleteActionPerformed(evt);
            }
        });

        lbSupCode.setBackground(new java.awt.Color(255, 0, 51));
        lbSupCode.setForeground(new java.awt.Color(255, 0, 0));

        lbSupName.setForeground(new java.awt.Color(255, 0, 51));

        lbSupAddress.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(54, 54, 54)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbSupName, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(lbSupCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtSupCode, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                    .addComponent(txtSupName)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(a)
                                    .addComponent(jLabel3))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkSupColloborating)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbSupAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSupAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnSupNew)
                        .addGap(34, 34, 34)
                        .addComponent(btnSupSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(btnSupDelete)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSupCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbSupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(lbSupName)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(a)
                    .addComponent(txtSupAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(lbSupAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(chkSupColloborating))
                .addGap(39, 39, 39)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSupNew)
                    .addComponent(btnSupSave)
                    .addComponent(btnSupDelete))
                .addContainerGap(164, Short.MAX_VALUE))
        );

        btnLogoutSup.setText("Logout");
        btnLogoutSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutSupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SuppliersLayout = new javax.swing.GroupLayout(Suppliers);
        Suppliers.setLayout(SuppliersLayout);
        SuppliersLayout.setHorizontalGroup(
            SuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SuppliersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogoutSup)
                    .addGroup(SuppliersLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SuppliersLayout.setVerticalGroup(
            SuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SuppliersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(btnLogoutSup)
                .addContainerGap())
        );

        tabAll.addTab("Suppliers", Suppliers);

        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Tittle 6"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItems.getTableHeader().setReorderingAllowed(false);
        tblItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemsMouseClicked(evt);
            }
        });
        tblItems.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblItemsKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblItems);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel4.setText("Item Code:");

        b.setText("Item Name:");

        jLabel6.setText("Unit:");

        lbItemSup.setText("Supplier:");

        cbSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSupplierActionPerformed(evt);
            }
        });

        jLabel8.setText("Price:");

        jLabel9.setText("Supplying:");

        btnItemAddNew.setText("Add New");
        btnItemAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemAddNewActionPerformed(evt);
            }
        });

        btnItemSave.setText("Save");
        btnItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemSaveActionPerformed(evt);
            }
        });

        btnItemDelete.setText("Delete");
        btnItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDeleteActionPerformed(evt);
            }
        });

        lbItemCode.setForeground(new java.awt.Color(255, 0, 0));

        lbItemName.setForeground(new java.awt.Color(255, 0, 0));

        lbItemUnit.setForeground(new java.awt.Color(255, 0, 51));

        lbItemPrice.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnItemAddNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(btnItemSave)
                        .addGap(34, 34, 34)
                        .addComponent(btnItemDelete))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(b)
                            .addComponent(jLabel6)
                            .addComponent(lbItemSup)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtItemUnit, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbSupplier, 0, 191, Short.MAX_VALUE)
                            .addComponent(txtItemName)
                            .addComponent(txtItemPrice, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtItemCode)
                            .addComponent(lbItemCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbItemName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbItemUnit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(chkitemSupplying)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lbItemPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbItemCode)
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbItemName)
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbItemSup)
                    .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtItemUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(lbItemUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbItemPrice)
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(chkitemSupplying))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnItemAddNew)
                    .addComponent(btnItemSave)
                    .addComponent(btnItemDelete))
                .addContainerGap(94, Short.MAX_VALUE))
        );

        lbItemSup.getAccessibleContext().setAccessibleName("");

        btnLogoutItem.setText("Logout");
        btnLogoutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLogoutItem)
                        .addGap(30, 30, 30))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnLogoutItem)
                .addGap(21, 21, 21))
        );

        tabAll.addTab("Items", jPanel1);

        lbWelcome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbWelcome.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabAll)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(tabAll, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSupDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupDeleteActionPerformed
        // TODO add your handling code here:
        if (dataSup.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add supplier first");
            return;
        }

        int selectedRow = tblSupplier.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Connection con = null;
                con = MyConnnection.makeConnection();
                if (con == null) {
                    JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                    return;
                }
            } catch (Exception e) {
            }

//            String supCode = txtSupCode.getText();
//            int selectedRow = tblSupplier.getSelectedRow();
            int r = JOptionPane.showConfirmDialog(this, "do you want to remove?", "Remove?", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                String supCode = tblSupplier.getValueAt(selectedRow, 0).toString();
                try {
                    if (SupplierDAO.deleteSup(supCode) == SUCCESS) {
                        dataSup.remove(selectedRow);
//                        loadSup();
//                        defaulModelSup.setDataVector(dataSup, headerSup);
                        this.cbSupplier.setModel(new DefaultComboBoxModel(SupplierDAO.getAllSuppliers()));
                    }
                    JOptionPane.showMessageDialog(this, "Data remove");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "This supplier is using by item, cannot delete. Please delete all item first. ");
                }
            }

//            this.cbSupplier.setModel(new DefaultComboBoxModel(supplierDAO));
            tblSupplier.updateUI();
        } else {
            JOptionPane.showMessageDialog(this, "Choose Supplier to remove");
        }
        setNoneSup();
        txtSupCode.setEditable(false);
    }//GEN-LAST:event_btnSupDeleteActionPerformed

    private void btnSupSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupSaveActionPerformed
        // TODO add your handling code here:

        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (SQLServerException e) {
//            JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
        } catch (SQLException ex) {
        }

        if (!validSupInfo()) {
            return;
        }

        String supCode = txtSupCode.getText().trim();
        String supName = txtSupName.getText().trim();
        String address = txtSupAddress.getText().trim();
        boolean checkSup = chkSupColloborating.isSelected();
        SupplierDTO sup = new SupplierDTO(supCode, supName, address, checkSup);

//        if (addNewSup == false) {
//            JOptionPane.showMessageDialog(this, "Cannot save. Please click Add new first!");
//            return;
//        }
        if (addNewSup) {
            //insert into database  
            try {
                if (supplierDAO.getSupplierByCode(supCode) != null) {
                    JOptionPane.showMessageDialog(this, "Code is duplicated");
                    return;
                }
                int result = SupplierDAO.insertSup(sup);
                if (result == SUCCESS) {
                    Vector row = new Vector();
                    row.add(supCode);
                    row.add(supName);
                    row.add(address);
                    row.add(checkSup);
                    dataSup.add(row);
                    JOptionPane.showMessageDialog(this, "Add Success");
                    addNewSup = false;
                                       
                } else {
                    JOptionPane.showMessageDialog(this, "Add Fail. Please click Add new first.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { //update database 
            try {
                int result = SupplierDAO.updateSup(sup);
                if (result == SUCCESS) {
                    int selectedRow = tblSupplier.getSelectedRow();
                    Vector row = (Vector) dataSup.get(selectedRow);
                    row.set(1, supName);
                    row.set(2, address);
                    //    row.set(3, checkSup);
                    JOptionPane.showMessageDialog(this, "Data updated");

                } else {
                    JOptionPane.showMessageDialog(this, "Update Fail. Please choose supplier to update!!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        
        loadSup();
        loadItem();
        setNoneSup();
        txtSupCode.setEditable(false);
        defaultModelItem.setDataVector(dataItem, headerItem);
        this.cbSupplier.setModel(new DefaultComboBoxModel(supplierDAO));
        tblSupplier.updateUI();
    }//GEN-LAST:event_btnSupSaveActionPerformed

    private void btnSupNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupNewActionPerformed
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (SQLException ex) {

        }

        this.addNewSup = true;
        setNoneSup();
        txtSupCode.setEditable(true);

    }//GEN-LAST:event_btnSupNewActionPerformed

    private void tblSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierMouseClicked
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        this.addNewSup = false;
        txtSupCode.setEnabled(false);
        int selectedRow = tblSupplier.getSelectedRow();

        if (selectedRow < 0 || selectedRow > dataSup.size()) {
            return;
        }

        String supCode = tblSupplier.getValueAt(selectedRow, 0).toString();
        String supName = tblSupplier.getValueAt(selectedRow, 1).toString();
        String address = tblSupplier.getValueAt(selectedRow, 2).toString();
        boolean checkSup = supplierDAO.get(selectedRow).isColloborating();
        txtSupCode.setText(supCode);
        txtSupName.setText(supName);
        txtSupAddress.setText(address);
        chkSupColloborating.setSelected(checkSup);
        lbSupAddress.setText("");
        lbSupCode.setText("");
        lbSupName.setText("");
        tblSupplier.updateUI();
    }//GEN-LAST:event_tblSupplierMouseClicked

    private void btnItemAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemAddNewActionPerformed
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        this.addNewItem = true;
        txtItemCode.setEditable(true);

        if (cbSupplier.getItemCount() > 0) {
            this.cbSupplier.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Need at least 1 supplier to add!!!");
        }
        setNoneItem();
    }//GEN-LAST:event_btnItemAddNewActionPerformed

    private void tblItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemsMouseClicked
        // TODO add your handling code here:
        addNewItem = false;
        txtItemCode.setEnabled(false);

        int selectedRow = tblItems.getSelectedRow();

        if (selectedRow < 0 || selectedRow > dataItem.size()) {
            return;
        }

        String code = tblItems.getValueAt(selectedRow, 0).toString();
        String name = tblItems.getValueAt(selectedRow, 1).toString();
        SupplierDTO sup = (SupplierDTO) tblItems.getValueAt(selectedRow, 2);
        String unit = tblItems.getValueAt(selectedRow, 3).toString();
        float price = (float) tblItems.getValueAt(selectedRow, 4);
        boolean checkIt = (boolean) tblItems.getValueAt(selectedRow, 5);

        txtItemCode.setText(code);
        txtItemName.setText(name);
        int index = supplierDAO.find(sup.getSupCode());
        cbSupplier.setSelectedIndex(index);
        txtItemUnit.setText(unit);
        txtItemPrice.setText(price + "");
        chkitemSupplying.setSelected(checkIt);

    }//GEN-LAST:event_tblItemsMouseClicked

    private void btnItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemSaveActionPerformed
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        if (!validItemInfo()) {
            return;
        }

        String code = txtItemCode.getText().trim();
        String name = txtItemName.getText().trim();
        String unit = txtItemUnit.getText().trim();
        float price = Float.parseFloat(txtItemPrice.getText());
        boolean supplying = chkitemSupplying.isSelected();
        SupplierDTO supplier = (SupplierDTO) cbSupplier.getSelectedItem();
        ItemDTO it = new ItemDTO(code, name, unit, price, supplying, supplier);

//        if (addNewItem == false) {
//            JOptionPane.showMessageDialog(this, "Cannot save. Please click Add new first!");
//            return;
//        }
        if (addNewItem) { //add new row
            //insert into database
            try {
                if (ItemDAO.getItemsByCode(code) != null) {
                    JOptionPane.showMessageDialog(this, "Code is duplicated");
                    return;
                }
                int result = ItemDAO.insertItem(it);
                if (result == SUCCESS) {
                    Vector row = new Vector();
                    row.add(code);
                    row.add(name);
                    row.add(supplier);
                    row.add(unit);
                    row.add(price);
                    row.add(supplying);
                    dataItem.add(row);
                    addNewItem = false;
                    JOptionPane.showMessageDialog(this, "Data saved");
                } else {
                    JOptionPane.showMessageDialog(this, "Add fail. Please click Add new first.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // update database
            try {
                int result = ItemDAO.update(it);
                if (result == SUCCESS) {
                    int selectedRow = tblItems.getSelectedRow();
                    System.out.println(selectedRow);
                    Vector row = (Vector) dataItem.get(selectedRow);
                    System.out.println(row);
                    row.set(1, name);
                    row.set(2, supplier);
                    row.set(3, unit);
                    row.set(4, price);
                    row.set(5, supplying);
                    JOptionPane.showMessageDialog(this, "Data Updated");
                } else {
                    JOptionPane.showMessageDialog(this, "Update fail!!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setNoneItem();
        txtItemCode.setEditable(false);
        tblItems.updateUI();
    }//GEN-LAST:event_btnItemSaveActionPerformed

    private void btnItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDeleteActionPerformed
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        if (dataItem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add item first");
            return;
        }

        int selectedRow = tblItems.getSelectedRow();
        if (selectedRow >= 0) {
            int r = JOptionPane.showConfirmDialog(this, "Do you want to remove?", "Remove?", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
//                int selectedRow = tblItems.getSelectedRow();
                String code = tblItems.getValueAt(selectedRow, 0).toString();
                try {
                    if (ItemDAO.delete(code) == SUCCESS) {
                        dataItem.remove(selectedRow);

                    }
                    JOptionPane.showMessageDialog(this, "Data remove");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            if (cbSupplier.getItemCount() > 0) {
//                this.cbSupplier.setSelectedIndex(0);
//            } else {
//                JOptionPane.showMessageDialog(this, "Need at least 1 supplier to add. Please add supplier first.");
//            }
        } else {
            JOptionPane.showMessageDialog(this, "Choose Item to delete");
        }
        setNoneItem();
        txtItemCode.setEditable(false);
        tblItems.updateUI();
    }//GEN-LAST:event_btnItemDeleteActionPerformed

    private void txtSupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupNameActionPerformed

    private void btnLogoutSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutSupActionPerformed
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        int logout = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (logout == JOptionPane.YES_OPTION) {
            Login c = new Login();
            c.setVisible(true);
            this.dispose();
        } else if (logout == JOptionPane.NO_OPTION) {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }//GEN-LAST:event_btnLogoutSupActionPerformed

    private void cbSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSupplierActionPerformed

    private void btnLogoutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutItemActionPerformed
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        int logout = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (logout == JOptionPane.YES_OPTION) {
            Login c = new Login();
            c.setVisible(true);
            this.dispose();
        } else if (logout == JOptionPane.NO_OPTION) {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }//GEN-LAST:event_btnLogoutItemActionPerformed

    private void tabAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabAllMouseClicked
        // TODO add your handling code here:
        int indexCurrentTab = tabAll.getSelectedIndex();

        if (indexCurrentTab == indexOldTab) {
            return;
        } else {

            if (indexOldTab == SUPTAB) {
                if (txtSupCode.getText().trim().length() > 0
                        || txtSupName.getText().trim().length() > 0
                        || txtSupAddress.getText().trim().length() > 0) {
                    int result = JOptionPane.showConfirmDialog(this, "You have unfinished session of tab supplier. Do you want to return?", "Notify", JOptionPane.YES_NO_OPTION);
                    if ((result == JOptionPane.NO_OPTION)) {
                        setNoneSup();
                        txtSupCode.setEditable(false);
                    } else {
                        tabAll.setSelectedIndex(indexOldTab);
                        indexCurrentTab = tabAll.getSelectedIndex();
                    }
                }
            } else if (indexOldTab == ITEMTAB) {
                if (txtItemCode.getText().trim().length() > 0
                        || txtItemName.getText().trim().length() > 0
                        || txtItemUnit.getText().trim().length() > 0
                        || txtItemPrice.getText().trim().length() > 0) {
                    int result = JOptionPane.showConfirmDialog(this, "You have unfinished session of tab items. Do you want to return?", "Notify", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.NO_OPTION) {
                        setNoneItem();
                        txtItemCode.setEditable(false); 
                    } else {
                        tabAll.setSelectedIndex(indexOldTab);
                        indexCurrentTab = tabAll.getSelectedIndex();
                    }
                }
            }
            indexOldTab = indexCurrentTab;
        }


    }//GEN-LAST:event_tabAllMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to exit? ", "Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            Login c = new Login();
            c.setVisible(true);
            this.dispose();
        } else if (choice == JOptionPane.NO_OPTION) {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }//GEN-LAST:event_formWindowClosing

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
//        loadItem();
//        loadSup();
    }//GEN-LAST:event_formMouseClicked

    private void tblSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSupplierKeyReleased
        // TODO add your handling code here:
        try {
            Connection con = null;
            con = MyConnnection.makeConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Server disconnect. Please try later.");
                return;
            }
        } catch (Exception e) {
        }

        this.addNewSup = false;
        txtSupCode.setEnabled(false);
        int selectedRow = tblSupplier.getSelectedRow();

        if (selectedRow < 0 || selectedRow > dataSup.size()) {
            return;
        }

        String supCode = tblSupplier.getValueAt(selectedRow, 0).toString();
        String supName = tblSupplier.getValueAt(selectedRow, 1).toString();
        String address = tblSupplier.getValueAt(selectedRow, 2).toString();
        boolean checkSup = supplierDAO.get(selectedRow).isColloborating();
        txtSupCode.setText(supCode);
        txtSupName.setText(supName);
        txtSupAddress.setText(address);
        chkSupColloborating.setSelected(checkSup);
        lbSupAddress.setText("");
        lbSupCode.setText("");
        lbSupName.setText("");
        tblSupplier.updateUI();
    }//GEN-LAST:event_tblSupplierKeyReleased

    private void tblItemsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemsKeyReleased
        // TODO add your handling code here:
        addNewItem = false;
        txtItemCode.setEnabled(false);

        int selectedRow = tblItems.getSelectedRow();

        if (selectedRow < 0 || selectedRow > dataItem.size()) {
            return;
        }

        String code = tblItems.getValueAt(selectedRow, 0).toString();
        String name = tblItems.getValueAt(selectedRow, 1).toString();
        SupplierDTO sup = (SupplierDTO) tblItems.getValueAt(selectedRow, 2);
        String unit = tblItems.getValueAt(selectedRow, 3).toString();
        float price = (float) tblItems.getValueAt(selectedRow, 4);
        boolean checkIt = (boolean) tblItems.getValueAt(selectedRow, 5);

        txtItemCode.setText(code);
        txtItemName.setText(name);
        int index = supplierDAO.find(sup.getSupCode());
        cbSupplier.setSelectedIndex(index);
        txtItemUnit.setText(unit);
        txtItemPrice.setText(price + "");
        chkitemSupplying.setSelected(checkIt);
    }//GEN-LAST:event_tblItemsKeyReleased

    private void SuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SuppliersMouseClicked

    }//GEN-LAST:event_SuppliersMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

    }//GEN-LAST:event_jPanel1MouseClicked

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
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (!isLogin) {
                    Login c = new Login();
                    c.setVisible(true);
                } else {
                    Main dialog = new Main(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);

                        }
                    });
                    dialog.setVisible(true);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Suppliers;
    private javax.swing.JLabel a;
    private javax.swing.JLabel b;
    private javax.swing.JButton btnItemAddNew;
    private javax.swing.JButton btnItemDelete;
    private javax.swing.JButton btnItemSave;
    private javax.swing.JButton btnLogoutItem;
    private javax.swing.JButton btnLogoutSup;
    private javax.swing.JButton btnSupDelete;
    private javax.swing.JButton btnSupNew;
    private javax.swing.JButton btnSupSave;
    private javax.swing.JComboBox<String> cbSupplier;
    private javax.swing.JCheckBox chkSupColloborating;
    private javax.swing.JCheckBox chkitemSupplying;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbItemCode;
    private javax.swing.JLabel lbItemName;
    private javax.swing.JLabel lbItemPrice;
    private javax.swing.JLabel lbItemSup;
    private javax.swing.JLabel lbItemUnit;
    private javax.swing.JLabel lbSupAddress;
    private javax.swing.JLabel lbSupCode;
    private javax.swing.JLabel lbSupName;
    private javax.swing.JLabel lbWelcome;
    private javax.swing.JTabbedPane tabAll;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemPrice;
    private javax.swing.JTextField txtItemUnit;
    private javax.swing.JTextField txtSupAddress;
    private javax.swing.JTextField txtSupCode;
    private javax.swing.JTextField txtSupName;
    // End of variables declaration//GEN-END:variables
}
