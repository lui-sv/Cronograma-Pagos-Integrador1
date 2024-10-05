/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Pantallas;

import Clases.ClaseCalculo;
import Clases.ClaseEstatica;
import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Administrador
 */
public class PanelIngresarDatos extends javax.swing.JPanel {

    ClaseCalculo CCst = ClaseEstatica.CC;
    DecimalFormat df = new DecimalFormat("#");

    public PanelIngresarDatos() {
        initComponents();

        txt_TEA.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!(Character.isDigit(c) && txt_TEA.getText().length() < 3)) {
                    evt.consume();
                }
            }
        });
        txt_TEA.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!(Character.isDigit(c) && txt_TEA.getText().length() < 3)) {
                    evt.consume();
                }
            }
        });

        txt_CashSolicitado.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!(Character.isDigit(c) && txt_CashSolicitado.getText().length() < 6)) {
                    evt.consume();
                }
            }
        });

        java.awt.event.KeyAdapter keyListener2 = new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!(Character.isDigit(c) && ((javax.swing.JTextField) evt.getSource()).getText().length() < 6)) {
                    evt.consume();
                }
            }
        };
        txtMonto_PP.addKeyListener(keyListener2);
        txtMonto_TC.addKeyListener(keyListener2);

        configureSpinner(spn_CantidadCuotas, 12, 60, 12);
        configureSpinner(spn_PeriodoGracia, 10, 60, 30);
    }

    // Método privado para configurar los JSpinners con límites y valor inicial
    private void configureSpinner(JSpinner spinner, int min, int max, int initialValue) {
        // Establecer el modelo con límites y valor inicial
        spinner.setModel(new SpinnerNumberModel(initialValue, min, max, 1));

        // Obtener el editor del JSpinner
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.NumberEditor) {
            JSpinner.NumberEditor numberEditor = (JSpinner.NumberEditor) editor;
            JFormattedTextField textField = numberEditor.getTextField();

            // Configurar el NumberFormatter para que respete los límites
            NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
            numberFormatter.setValueClass(Integer.class);
            numberFormatter.setMinimum(min);
            numberFormatter.setMaximum(max);
            numberFormatter.setAllowsInvalid(false); // No permitir entradas no numéricas

            textField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
        }
    }

    private boolean verificarDatos() {
        double MontoPPValue = Double.parseDouble(txtMonto_PP.getText());
        double MontoTCValue = Double.parseDouble(txtMonto_TC.getText());
        // Condicional TEA-------------------------------------------------------------------------------------------------------------------------------------------------

        double teaValue = Double.parseDouble(txt_TEA.getText());
        if (teaValue < 4 || teaValue > 100) {
            JOptionPane.showMessageDialog(this, "El valor de TEA debe estar entre 4 y 100.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Condicional CashSolicitado--------------------------------------------------------------------------------------------------------------------------------------
        double cashValue = Double.parseDouble(txt_CashSolicitado.getText());
        if (cashValue < 0 || cashValue > 500000) {
            JOptionPane.showMessageDialog(this, "El valor de Cash solicitado debe ser máximo 500000.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //Condicional Monto PP y TC-----------------------------------------------------------------------------------------------------------------------------------------
        // Verificar si ambos campos son cero al principio
        if (MontoPPValue == 0 && MontoTCValue == 0) {
            JOptionPane.showMessageDialog(this, "Al menos uno de los campos Monto PP o Monto TC debe tener un valor diferente de cero.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Verificar si solo uno de los campos es igual a cero
        if ((MontoPPValue == 0 && MontoTCValue != 0) || (MontoPPValue != 0 && MontoTCValue == 0)) {
            // Solo uno de los campos es igual a cero, no es necesario verificar los límites
            return true;
        }

        // Ambos campos tienen valores diferentes de cero, verificar si están dentro del rango
        if (MontoPPValue < 500 || MontoPPValue > 200000 || MontoTCValue < 500 || MontoTCValue > 200000) {
            String errorMessage = "El valor del Monto TC o PP debe ser mínimo 500 y máximo 200000.";
            JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }

    public void CalcularDatos() {
        CCst.DefinirMontoPP();
        CCst.DefinirMontoTotal();
        CCst.generarFechasDePago();
        CCst.DefinirPrimaSeguroFinal();
        CCst.DefinirMontoSeguroFinal();
        CCst.DefinirMAFInicial();
        CCst.DefinirVariablesCronogramaFinal();
        CCst.CalcularTCEA();
    }

    public void GuardarDatos() {
        CCst.setCuotas((int) spn_CantidadCuotas.getValue());
        CCst.setPeriodo_Gracia((int) spn_PeriodoGracia.getValue());
        CCst.setTEA(Double.parseDouble(txt_TEA.getText()) / 100);
        CCst.setMontoPP(Integer.parseInt(txtMonto_PP.getText()));
        CCst.setMontoTC(Integer.parseInt(txtMonto_TC.getText()));
        CCst.setMontoSolicitado(Integer.parseInt(txt_CashSolicitado.getText()));
        CCst.setTipo_Seguro((String) cmbbox_TipoSeguro.getSelectedItem());
    }

    public void VolverDatos() {
        spn_CantidadCuotas.setValue(CCst.getCuotas());
        spn_PeriodoGracia.setValue(CCst.getPeriodo_Gracia());
        txt_TEA.setText(df.format(CCst.getTEA() * 100));
        txtMonto_PP.setText(df.format(CCst.getMontoPP()));
        txtMonto_TC.setText(df.format(CCst.getMontoTC()));
        txt_CashSolicitado.setText(df.format(CCst.getMontoSolicitado()));
        cmbbox_TipoSeguro.setSelectedItem(CCst.getTipo_Seguro());
    }

    public void mostrarTipo(JPanel j) {

        j.setSize(852, 558);
        j.setLocation(0, 0);

        PnlGeneral1.removeAll();
        PnlGeneral1.add(j, BorderLayout.CENTER);
        PnlGeneral1.revalidate();
        PnlGeneral1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlGeneral1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbbox_TipoSeguro = new javax.swing.JComboBox<>();
        txt_TEA = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        spn_PeriodoGracia = new javax.swing.JSpinner();
        spn_CantidadCuotas = new javax.swing.JSpinner();
        txt_CashSolicitado = new javax.swing.JTextField();
        txtMonto_PP = new javax.swing.JTextField();
        txtMonto_TC = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btn_Cronograma = new javax.swing.JButton();
        btn_OfertaInicial = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btn_Limpiar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(852, 558));

        PnlGeneral1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(153, 221, 255));

        jLabel1.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel1.setText("Cantidad de cuotas:");

        jLabel2.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel2.setText("TEA:");

        jLabel3.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel3.setText("Periodo de gracia:");

        jLabel4.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel4.setText("Tipo de seguro:");

        jLabel6.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel6.setText("Cash Solicitado:");

        jLabel7.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel7.setText("Monto en TC:");

        jLabel8.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel8.setText("Monto en PP:");

        cmbbox_TipoSeguro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seguro de Vida", "Seguro de Desgravamen", "Seguro de Desgravamen con Devolución" }));

        txt_TEA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_TEA.setText("12");
        txt_TEA.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 18)); // NOI18N
        jLabel5.setText("%");

        spn_PeriodoGracia.setToolTipText("");
        spn_PeriodoGracia.setAutoscrolls(true);

        txt_CashSolicitado.setText("0");

        txtMonto_PP.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMonto_PP.setText("0");

        txtMonto_TC.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMonto_TC.setText("0");

        jPanel3.setBackground(new java.awt.Color(0, 67, 107));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Verdana", 3, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("APLICATIVO PARA CALCULAR");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 2, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("OFERTAS DE COMPRA Y VENTA DE PRESTAMOS PERSONALES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(26, 26, 26))
        );

        jPanel4.setBackground(new java.awt.Color(5, 130, 202));

        btn_Cronograma.setBackground(new java.awt.Color(0, 166, 251));
        btn_Cronograma.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        btn_Cronograma.setForeground(new java.awt.Color(0, 53, 84));
        btn_Cronograma.setText("Cronograma de Pagos");
        btn_Cronograma.setBorder(null);
        btn_Cronograma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CronogramaActionPerformed(evt);
            }
        });

        btn_OfertaInicial.setBackground(new java.awt.Color(0, 166, 251));
        btn_OfertaInicial.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        btn_OfertaInicial.setForeground(new java.awt.Color(0, 53, 84));
        btn_OfertaInicial.setText("Oferta Inicial");
        btn_OfertaInicial.setBorder(null);
        btn_OfertaInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OfertaInicialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_OfertaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Cronograma, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cronograma, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_OfertaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 0, 51));
        jLabel11.setText("(Min. 4 - Max. 100)");

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 0, 51));
        jLabel12.setText("(Min. 10 - Max. 60)");

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 0, 51));
        jLabel13.setText("(Min. 12 - Max. 60)");

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(153, 0, 51));
        jLabel14.setText("(Min. s/.0 - Max. s/.500.000)");

        jLabel15.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(153, 0, 51));
        jLabel15.setText("(Min. s/.500 - Max. s/.200.000)");

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 0, 10)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(153, 0, 51));
        jLabel16.setText("(Min. s/.500 - Max. s/.200.000)");

        jLabel17.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 153, 153));
        jLabel17.setText("NOTA:  solo uno de los 2 (PP o TC)");

        jLabel18.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(153, 153, 153));
        jLabel18.setText("puede quedar en s/. 0");

        btn_Limpiar.setBackground(new java.awt.Color(84, 187, 238));
        btn_Limpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/botonclean.png"))); // NOI18N
        btn_Limpiar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LimpiarActionPerformed(evt);
            }
        });

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 2, 24)); // NOI18N
        jLabel19.setText("Condiciones de la oferta comercial");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(txt_TEA, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel11))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(cmbbox_TipoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel12))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addComponent(spn_PeriodoGracia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel13))
                                    .addComponent(jLabel1))
                                .addGap(25, 25, 25)
                                .addComponent(spn_CantidadCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(91, 91, 91)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(txtMonto_PP, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(txtMonto_TC, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel19)))
                .addGap(1, 1, 1)
                .addComponent(btn_Limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel14))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addComponent(txt_CashSolicitado, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel18))))
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_TEA, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addComponent(jLabel11)
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbbox_TipoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel12))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spn_PeriodoGracia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel13))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spn_CantidadCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel16)))
                                .addGap(6, 6, 6)
                                .addComponent(txtMonto_PP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel15)))
                                .addGap(6, 6, 6)
                                .addComponent(txtMonto_TC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(btn_Limpiar)))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel14))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_CashSolicitado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel18)))
                .addGap(19, 19, 19)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout PnlGeneral1Layout = new javax.swing.GroupLayout(PnlGeneral1);
        PnlGeneral1.setLayout(PnlGeneral1Layout);
        PnlGeneral1Layout.setHorizontalGroup(
            PnlGeneral1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlGeneral1Layout.setVerticalGroup(
            PnlGeneral1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlGeneral1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlGeneral1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_OfertaInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OfertaInicialActionPerformed
        if (verificarDatos()) {
            GuardarDatos();
            CalcularDatos();
            PanelOfertaInicial pCOI = new PanelOfertaInicial();
            mostrarTipo(pCOI);
        }
    }//GEN-LAST:event_btn_OfertaInicialActionPerformed

    private void btn_CronogramaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CronogramaActionPerformed
        if (verificarDatos()) {
            GuardarDatos();
            CalcularDatos();
            PanelCronogramaPago pCPag = new PanelCronogramaPago();
            mostrarTipo(pCPag);
        }
    }//GEN-LAST:event_btn_CronogramaActionPerformed

    private void btn_LimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LimpiarActionPerformed
        spn_CantidadCuotas.setValue(30);
        spn_PeriodoGracia.setValue(12);
        txt_TEA.setText("12");
        txtMonto_PP.setText("0");
        txtMonto_TC.setText("0");
        txt_CashSolicitado.setText("0");
        cmbbox_TipoSeguro.setSelectedItem("Seguro de Vida");
    }//GEN-LAST:event_btn_LimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PnlGeneral1;
    private javax.swing.JButton btn_Cronograma;
    private javax.swing.JButton btn_Limpiar;
    private javax.swing.JButton btn_OfertaInicial;
    private javax.swing.JComboBox<String> cmbbox_TipoSeguro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSpinner spn_CantidadCuotas;
    private javax.swing.JSpinner spn_PeriodoGracia;
    private javax.swing.JTextField txtMonto_PP;
    private javax.swing.JTextField txtMonto_TC;
    private javax.swing.JTextField txt_CashSolicitado;
    private javax.swing.JTextField txt_TEA;
    // End of variables declaration//GEN-END:variables
}
