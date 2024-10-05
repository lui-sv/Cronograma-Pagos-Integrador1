/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import Clases.ClaseEstatica;

import Clases.ClaseCalculo;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Administrador
 */
public class PanelOfertaInicial extends javax.swing.JPanel {

    ClaseCalculo CCst = ClaseEstatica.CC;

    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DecimalFormat df = new DecimalFormat("#.##");

    public PanelOfertaInicial() {
        initComponents();
        BloquearTxt();
        

        txtTipoSeguro.setText(CCst.getTipo_Seguro());
        txtCuotaMensual.setText(String.valueOf("S/." + (df.format(CCst.getMontoCuota()))));
        txtCuotas.setText(String.valueOf(CCst.getCuotas()));
        txtFecha1erPago.setText(String.valueOf(CCst.getFecha1erpago().format(formatoFecha)));
        txtFechaDesembolso.setText(String.valueOf(CCst.getFechadesembolso().format(formatoFecha)));
        txtImportePrestamo.setText(String.valueOf("S/." + (df.format(CCst.getMontoTotal()))));
        txtMAFInicial.setText(String.valueOf("S/." + (df.format(CCst.getMAFInicial()))));
        txtMoneda.setText("SOLES");
        txtMontoSeguro.setText(String.valueOf("S/." + (df.format(CCst.getMontoSeguroFinal()))));
        txtPrimaSeguro.setText(String.valueOf(df.format(CCst.getPrimaSeguroFinal() * 100)) + "%");
        txtTCEA.setText(String.valueOf(df.format(CCst.getTCEA()* 100)) + "%");
        txtTEA.setText(String.valueOf(CCst.getTEA() * 100) + "%");
        txtTipoSeguro.setText(CCst.getTipo_Seguro());
        LlenarTabla();
    }

    public void LlenarTabla() {
        DefaultTableModel model = (DefaultTableModel) tblOfertaInicial.getModel();
        model.setRowCount(0);

        model.addRow(new Object[]{"Tipo de Seguro", CCst.getTipo_Seguro()});
        model.addRow(new Object[]{"Importe del préstamo", txtImportePrestamo.getText()});
        model.addRow(new Object[]{"Moneda", "SOLES"});
        model.addRow(new Object[]{"Tiempo de cuotas", CCst.getCuotas()});
        model.addRow(new Object[]{"Fecha de Desembolso", txtFechaDesembolso.getText()});
        model.addRow(new Object[]{"Fecha del 1er Pago", txtFecha1erPago.getText()});
        model.addRow(new Object[]{"TEA (tasa de interés)", txtTEA.getText()});
        model.addRow(new Object[]{"Cuota mensual", txtCuotaMensual.getText()});
    }

    public void mostrarTipo(JPanel j) {

        j.setSize(852, 558);
        j.setLocation(0, 0);

        PnlGeneral3.removeAll();
        PnlGeneral3.add(j, BorderLayout.CENTER);
        PnlGeneral3.revalidate();
        PnlGeneral3.repaint();
    }

    public void BloquearTxt() {
        txtCuotaMensual.setEnabled(false);
        txtCuotas.setEnabled(false);
        txtFecha1erPago.setEnabled(false);
        txtFechaDesembolso.setEnabled(false);
        txtImportePrestamo.setEnabled(false);
        txtMAFInicial.setEnabled(false);
        txtMoneda.setEnabled(false);
        txtMontoSeguro.setEnabled(false);
        txtPrimaSeguro.setEnabled(false);
        txtTCEA.setEnabled(false);
        txtTEA.setEnabled(false);
        txtTipoSeguro.setEnabled(false);

    }

    public void exportarExcel(JTable t) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xls");
            try {
                File archivoXLS = new File(ruta);
                if (archivoXLS.exists()) {
                    archivoXLS.delete();
                }
                archivoXLS.createNewFile();

                // Cargar la plantilla desde los recursos del paquete
                InputStream inputStream = getClass().getResourceAsStream("/resources/PlantillaOfertaInicial.xls");
                Workbook libro = new HSSFWorkbook(inputStream);
                Sheet hoja = libro.getSheetAt(0); // Suponemos que los datos se escriben en la primera hoja
                inputStream.close();

                // Escribir los datos del JTable en la hoja
                int filaInicio = 6; // Comenzar en la fila 7 (índice 6)
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.getRow(filaInicio);
                    if (fila == null) {
                        fila = hoja.createRow(filaInicio);
                    }
                    int colExcel = 1; // Empezar en la columna B (índice 1)
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        Cell celda = fila.getCell(colExcel);
                        if (celda == null) {
                            celda = fila.createCell(colExcel);
                        }
                        Object value = t.getValueAt(f, c);
                        if (value instanceof Double) {
                            celda.setCellValue((Double) value);
                        } else if (value instanceof Float) {
                            celda.setCellValue((Float) value);
                        } else if (value instanceof Boolean) {
                            celda.setCellValue((Boolean) value);
                        } else {
                            celda.setCellValue(value.toString());
                        }
                        colExcel++;
                    }
                    filaInicio++;
                }

                // Guardar el archivo modificado
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                libro.write(archivo);
                archivo.close();

                // Abrir el archivo guardado
                Desktop.getDesktop().open(archivoXLS);
            } catch (IOException | NumberFormatException e) {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlGeneral3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOfertaInicial = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtImportePrestamo = new javax.swing.JTextField();
        txtTipoSeguro = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPrimaSeguro = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMontoSeguro = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMAFInicial = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMoneda = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCuotas = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtFechaDesembolso = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtFecha1erPago = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCuotaMensual = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTCEA = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTEA = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnOfertaInicial = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        btnDatosNuevos = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblPrueba = new javax.swing.JLabel();

        PnlGeneral3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(153, 221, 255));

        tblOfertaInicial.setForeground(new java.awt.Color(0, 0, 0));
        tblOfertaInicial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Tipo de Seguro", null},
                {"Importe del prestamo", null},
                {"Moneda", "SOLES"},
                {"Tiempo de cuotas", ""},
                {"Fecha de Desembolso", null},
                {"Fecha de 1er Pago", null},
                {"TEA TOTAL", null},
                {"Cuota mensual", null}
            },
            new String [] {
                "DESCRIPCION", "VALORES"
            }
        ));
        jScrollPane1.setViewportView(tblOfertaInicial);

        jLabel1.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel1.setText("Importe del prestamo:");

        jLabel2.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel2.setText("Tipo de seguro:");

        jLabel3.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel3.setText("Prima de seguro:");

        jLabel4.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel4.setText("Monto del seguro:");

        jLabel5.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel5.setText("MAF Inicial:");

        jLabel6.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel6.setText("Moneda:");

        jLabel7.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel7.setText("Cuotas:");

        jLabel8.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel8.setText("Fecha de desembolso:");

        jLabel9.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel9.setText("Fecha de 1er pago:");

        jLabel10.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel10.setText("Cuota mensual:");

        jLabel11.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel11.setText("TCEA:");

        txtTCEA.setForeground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel12.setText("TEA:");

        jLabel13.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel13.setText("Vista previa de la oferta para el cliente:");

        jPanel3.setBackground(new java.awt.Color(5, 130, 202));

        btnOfertaInicial.setBackground(new java.awt.Color(0, 166, 251));
        btnOfertaInicial.setFont(new java.awt.Font("Verdana", 1, 15)); // NOI18N
        btnOfertaInicial.setForeground(new java.awt.Color(0, 53, 84));
        btnOfertaInicial.setText("Cronograma de pagos");
        btnOfertaInicial.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 15, 0, 0, new java.awt.Color(0, 0, 0)));
        btnOfertaInicial.setBorderPainted(false);
        btnOfertaInicial.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnOfertaInicial.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnOfertaInicial.setIconTextGap(15);
        btnOfertaInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOfertaInicialActionPerformed(evt);
            }
        });

        btnExportarExcel.setBackground(new java.awt.Color(0, 166, 251));
        btnExportarExcel.setFont(new java.awt.Font("Verdana", 1, 15)); // NOI18N
        btnExportarExcel.setForeground(new java.awt.Color(0, 53, 84));
        btnExportarExcel.setText("Exportar a EXCEL");
        btnExportarExcel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 15, 0, 0, new java.awt.Color(0, 0, 0)));
        btnExportarExcel.setBorderPainted(false);
        btnExportarExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnExportarExcel.setHideActionText(true);
        btnExportarExcel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnExportarExcel.setIconTextGap(15);
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        btnDatosNuevos.setBackground(new java.awt.Color(0, 166, 251));
        btnDatosNuevos.setFont(new java.awt.Font("Verdana", 1, 15)); // NOI18N
        btnDatosNuevos.setForeground(new java.awt.Color(0, 53, 84));
        btnDatosNuevos.setText("Nueva Oferta");
        btnDatosNuevos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 15, 0, 0, new java.awt.Color(0, 0, 0)));
        btnDatosNuevos.setBorderPainted(false);
        btnDatosNuevos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnDatosNuevos.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDatosNuevos.setIconTextGap(15);
        btnDatosNuevos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosNuevosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOfertaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExportarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnDatosNuevos, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOfertaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDatosNuevos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(0, 67, 107));

        lblPrueba.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        lblPrueba.setForeground(new java.awt.Color(255, 255, 255));
        lblPrueba.setText("OFERTA INICIAL");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(295, Short.MAX_VALUE)
                .addComponent(lblPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(206, 206, 206))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPrueba, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(txtImportePrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104)
                        .addComponent(txtTipoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtCuotaMensual, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89)
                        .addComponent(txtPrimaSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(117, 117, 117)
                        .addComponent(txtTCEA, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(txtMontoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107)
                        .addComponent(jLabel13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMAFInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaDesembolso, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFecha1erPago, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTEA, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(107, 107, 107)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImportePrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCuotaMensual, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTipoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTCEA, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtPrimaSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMontoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtMAFInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(txtMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(txtCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(txtFechaDesembolso, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(txtFecha1erPago, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(txtTEA, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(7, 7, 7)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout PnlGeneral3Layout = new javax.swing.GroupLayout(PnlGeneral3);
        PnlGeneral3.setLayout(PnlGeneral3Layout);
        PnlGeneral3Layout.setHorizontalGroup(
            PnlGeneral3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlGeneral3Layout.setVerticalGroup(
            PnlGeneral3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlGeneral3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlGeneral3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOfertaInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOfertaInicialActionPerformed
        PanelCronogramaPago pCPag = new PanelCronogramaPago();
        mostrarTipo(pCPag);
    }//GEN-LAST:event_btnOfertaInicialActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        try {
            exportarExcel(tblOfertaInicial);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void btnDatosNuevosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosNuevosActionPerformed
        PanelIngresarDatos pIDat = new PanelIngresarDatos();
        pIDat.VolverDatos();
        mostrarTipo(pIDat);
    }//GEN-LAST:event_btnDatosNuevosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlGeneral3;
    private javax.swing.JButton btnDatosNuevos;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnOfertaInicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPrueba;
    private javax.swing.JTable tblOfertaInicial;
    private javax.swing.JTextField txtCuotaMensual;
    private javax.swing.JTextField txtCuotas;
    private javax.swing.JTextField txtFecha1erPago;
    private javax.swing.JTextField txtFechaDesembolso;
    private javax.swing.JTextField txtImportePrestamo;
    private javax.swing.JTextField txtMAFInicial;
    private javax.swing.JTextField txtMoneda;
    private javax.swing.JTextField txtMontoSeguro;
    private javax.swing.JTextField txtPrimaSeguro;
    private javax.swing.JTextField txtTCEA;
    private javax.swing.JTextField txtTEA;
    private javax.swing.JTextField txtTipoSeguro;
    // End of variables declaration//GEN-END:variables
}
