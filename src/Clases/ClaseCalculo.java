/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Administrador
 */
public class ClaseCalculo extends ClaseFechas {

    private double MontoCuota, MontoSeguroFinal, PrimaSeguro, MAFInicial;
    private double TCEA, TEATotal, PrimaSeguroFinal;
    private double[] montoseguroMensual, saldoInicialMensual, MontoCuotasMensuales, AmortizacionMontosMensuales;
    private double[]  MontoInteresesMensuales, SaldoFinalMensuales, factorcronogramaMensuales;

    public double[] getFactorcronogramaMensuales() {
        return factorcronogramaMensuales;
    }

    public void setFactorcronogramaMensuales(double[] factorcronogramaMensuales) {
        this.factorcronogramaMensuales = factorcronogramaMensuales;
    }

    public double getPrimaSeguroFinal() {
        return PrimaSeguroFinal;
    }

    public void setPrimaSeguroFinal(double PrimaSeguroFinal) {
        this.PrimaSeguroFinal = PrimaSeguroFinal;
    }

    public double[] getSaldoInicialMensual() {
        return saldoInicialMensual;
    }

    public void setSaldoInicialMensual(double[] saldoInicialMensual) {
        this.saldoInicialMensual = saldoInicialMensual;
    }

    public double getMontoCuota() {
        return MontoCuota;
    }

    public double[] getMontoCuotasMensuales() {
        return MontoCuotasMensuales;
    }

    public void setMontoCuotasMensuales(double[] MontoCuotasMensuales) {
        this.MontoCuotasMensuales = MontoCuotasMensuales;
    }

    public double[] getAmortizacionMontosMensuales() {
        return AmortizacionMontosMensuales;
    }

    public void setAmortizacionMontosMensuales(double[] AmortizacionMontosMensuales) {
        this.AmortizacionMontosMensuales = AmortizacionMontosMensuales;
    }

    public double[] getMontoInteresesMensuales() {
        return MontoInteresesMensuales;
    }

    public void setMontoInteresesMensuales(double[] MontoInteresesMensuales) {
        this.MontoInteresesMensuales = MontoInteresesMensuales;
    }

    public double[] getSaldoFinalMensuales() {
        return SaldoFinalMensuales;
    }

    public void setSaldoFinalMensuales(double[] SaldoFinalMensuales) {
        this.SaldoFinalMensuales = SaldoFinalMensuales;
    }

    public void setMontoCuota(double MontoCuota) {
        this.MontoCuota = MontoCuota;
    }

    public double getTCEA() {
        return TCEA;
    }

    public void setTCEA(double TCEA) {
        this.TCEA = TCEA;
    }

    public double getMontoSeguroFinal() {
        return MontoSeguroFinal;
    }

    public void setMontoSeguroFinal(double MontoSeguroFinal) {
        this.MontoSeguroFinal = MontoSeguroFinal;
    }

    public double getMAFInicial() {
        return MAFInicial;
    }

    public void setMAFInicial(double MAFInicial) {
        this.MAFInicial = MAFInicial;
    }

    public void DefinirPrimaSeguro() {
        double prima;
        switch (getTipo_Seguro()) {
            case "Seguro de Desgravamen":
                prima = 0.001995;
                break;
            case "Seguro de Desgravamen con Devolución":
                prima = 0.002594;
                break;
            default:
                prima = 0;
                System.out.println("ERROR");
        }
        this.PrimaSeguro = prima;
    }

    public void DefinirTEATotal() {
        double factor1 = Math.pow((1 + getTEA()), (30.0 / 360)) - 1;
        double factor2 = factor1 + PrimaSeguro;
        double TEAFinal = Math.pow((1 + factor2), 12) - 1;
        TEATotal = TEAFinal;
    }

    public void CalcularTCEA() {
       TCEA = TEA+PrimaSeguroFinal;
    }


    public void DefinirVariablesCronogramaFinal() {
        generarFechasDePago();
        DefinirCuotaMensual(DefinirFactoresCronograma(TEA), MAFInicial);
        //---------------------------------------------------
        DefinirMontosdelCronograma(MAFInicial, false);
    }

    public double DefinirFactoresCronograma(double TEA) {
        double suma = 0;
        factorcronogramaMensuales = new double[Cuotas];
        for (int i = 0; i < Cuotas; i++) {

            factorcronogramaMensuales[i] = 1 / Math.pow((1 + TEA), (getDiasacumuladosMensuales()[i] / 360));
            suma += factorcronogramaMensuales[i];
        }
        return suma;
    }

    public void DefinirCuotaMensual(double FC, double monto) {
        setMontoCuota(monto / FC);
    }

    public void DefinirMontosdelCronograma(double montototal, boolean IsSeguro) {
        double montoseguro;
        saldoInicialMensual = new double[Cuotas];
        SaldoFinalMensuales = new double[Cuotas];
        MontoInteresesMensuales = new double[Cuotas];
        montoseguroMensual = new double[Cuotas];
        MontoCuotasMensuales = new double[Cuotas];
        AmortizacionMontosMensuales = new double[Cuotas];
        for (int i = 0; i < Cuotas; i++) {
            if (i == 0) {
                saldoInicialMensual[i] = montototal;
            } else {
                saldoInicialMensual[i] = SaldoFinalMensuales[i - 1];
            }
            MontoInteresesMensuales[i] = (Math.pow((1 + getTEA()), (getDiasNormalMensuales()[i] / 360)) - 1) * saldoInicialMensual[i];
            if (IsSeguro) {
                montoseguroMensual[i] = (Math.pow((1 + PrimaSeguro), (getDiasNormalMensuales()[i] / 30)) - 1) * saldoInicialMensual[i];
                montoseguro = montoseguroMensual[i];
            } else {
                montoseguro = 0;
            }
            if (i == Cuotas - 1) {
                MontoCuotasMensuales[i] = saldoInicialMensual[i] + montoseguro + MontoInteresesMensuales[i];
            } else {
                MontoCuotasMensuales[i] = getMontoCuota();
            }
            AmortizacionMontosMensuales[i] = MontoCuotasMensuales[i] - montoseguro - MontoInteresesMensuales[i];
            SaldoFinalMensuales[i] = saldoInicialMensual[i] - AmortizacionMontosMensuales[i];

        }

    }

    public double DefinirVNA() {
        double vna = 0;
        for (int i = 0; i < montoseguroMensual.length; i++) {
            vna += montoseguroMensual[i] / Math.pow(1 + 0.002871, i + 1);
        }
        return vna;
    }

    public void DefinirPrimaSeguroFinal() {
        if (Tipo_Seguro.equals("Seguro de Vida")) {
            setPrimaSeguroFinal(0.037000);
        } else {
            DefinirPrimaSeguro();
            DefinirTEATotal();
            generarFechasDePago();
            DefinirCuotaMensual(DefinirFactoresCronograma(TEATotal), MontoTotal);
            DefinirMontosdelCronograma(MontoTotal, true);
            setPrimaSeguroFinal(DefinirVNA() / getMontoTotal());
        }
    }

    public void DefinirMontoSeguroFinal() {
        this.MontoSeguroFinal = getMontoTotal() * getPrimaSeguroFinal();
    }

    public void DefinirMAFInicial() {
        this.MAFInicial = getMontoSeguroFinal() + getMontoTotal();
    }

}
