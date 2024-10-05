/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import Clases.ClaseCalculo;
import Clases.ClaseEstatica;
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
 * @author Luis
 */
public class PanelCronogramaPago extends javax.swing.JPanel {

    ClaseCalculo CCst = ClaseEstatica.CC;
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DecimalFormat df = new DecimalFormat("#.##");

    public PanelCronogramaPago() {
        initComponents();
        LlenarTabla();
    }

    public void LlenarTabla() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (int i = 0; i < CCst.getFechasDePago().length; i++) {
            model.addRow(new Object[]{
                i + 1, // Número de cuota
                CCst.getFechasDePago()[i].format(formatoFecha), // Fecha de pago
                CCst.getDiasNormalMensuales()[i],
                CCst.getDiasacumuladosMensuales()[i],
                "S/." + df.format(CCst.getSaldoInicialMensual()[i]),
                "S/." + df.format(CCst.getAmortizacionMontosMensuales()[i]), // Amortización
                "S/." + df.format(CCst.getMontoInteresesMensuales()[i]), // Intereses
                "S/." + df.format(CCst.getMontoCuotasMensuales()[i]),// Cuotas
                "S/." + df.format(CCst.getSaldoFinalMensuales()[i]), // Saldos
                df.format(CCst.getFactorcronogramaMensuales()[i])

            });
        }
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
                InputStream inputStream = getClass().getResourceAsStream("/resources/PlantillaCronograma.xls");
                Workbook libro = new HSSFWorkbook(inputStream);
                Sheet hoja = libro.getSheetAt(0); // Suponemos que los datos se escriben en la primera hoja
                inputStream.close();

                int[] columnasAExportar = {0, 1, 5, 6, 7, 8};

                // Escribir los datos del JTable en la hoja
                int filaInicio = 6; // Comenzar en la fila 7 (índice 6)
                for (int f = 0; f < t.getRowCount(); f++) {
                    Row fila = hoja.getRow(filaInicio);
                    if (fila == null) {
                        fila = hoja.createRow(filaInicio);
                    }
                    int colExcel = 1; // Empezar en la columna B (índice 1)
                    for (int c : columnasAExportar) {
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

                // Crear un estilo de celda con fondo blanco
                CellStyle estiloBlanco = libro.createCellStyle();
                estiloBlanco.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                estiloBlanco.setFillPattern((short) FillPatternType.SOLID_FOREGROUND.ordinal());

                // Hacer blancas las celdas por debajo de los datos exportados
                int ultimaFilaUtilizada = filaInicio - 1;
                int ultimaFilaHoja = hoja.getLastRowNum();
                for (int i = ultimaFilaUtilizada + 1; i <= ultimaFilaHoja; i++) {
                    Row row = hoja.getRow(i);
                    if (row == null) {
                        row = hoja.createRow(i);
                    }
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            cell = row.createCell(j);
                        }
                        cell.setCellStyle(estiloBlanco);
                    }
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

    public void mostrarTipo(JPanel j) {

        j.setSize(852, 558);
        j.setLocation(0, 0);

        jPanel1.removeAll();
        jPanel1.add(j, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlGeneral2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lblPrueba = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnOfertaInicial = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        btnDatosNuevos = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(852, 515));

        PnlGeneral2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(153, 221, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(852, 558));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "MES", "VENCIMIENTO", "DN", "DACUMULADOS", "SALDO INICIAL", "AMORTIZACION", "INTERES", "CUOTASMENSUALES", "SALDO FINAL", "FCM"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBackground(new java.awt.Color(0, 67, 107));

        lblPrueba.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        lblPrueba.setForeground(new java.awt.Color(255, 255, 255));
        lblPrueba.setText("CRONOGRAMA DE PAGOS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPrueba, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(5, 130, 202));

        btnOfertaInicial.setBackground(new java.awt.Color(0, 166, 251));
        btnOfertaInicial.setFont(new java.awt.Font("Verdana", 1, 15)); // NOI18N
        btnOfertaInicial.setForeground(new java.awt.Color(0, 53, 84));
        btnOfertaInicial.setText("Oferta Inicial");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDatosNuevos, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDatosNuevos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOfertaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 782, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout PnlGeneral2Layout = new javax.swing.GroupLayout(PnlGeneral2);
        PnlGeneral2.setLayout(PnlGeneral2Layout);
        PnlGeneral2Layout.setHorizontalGroup(
            PnlGeneral2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlGeneral2Layout.setVerticalGroup(
            PnlGeneral2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlGeneral2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlGeneral2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        try {
            exportarExcel(jTable1);
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void btnDatosNuevosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosNuevosActionPerformed
        PanelIngresarDatos pIDat = new PanelIngresarDatos();
        pIDat.VolverDatos();
        mostrarTipo(pIDat);
    }//GEN-LAST:event_btnDatosNuevosActionPerformed

    private void btnOfertaInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOfertaInicialActionPerformed
        PanelOfertaInicial pOIni = new PanelOfertaInicial();
        mostrarTipo(pOIni);
    }//GEN-LAST:event_btnOfertaInicialActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlGeneral2;
    private javax.swing.JButton btnDatosNuevos;
    private javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton btnOfertaInicial;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblPrueba;
    // End of variables declaration//GEN-END:variables
}
