/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

abstract class ClaseFechas extends ClaseRegistro {

    LocalDate fechadesembolso;
    LocalDate fecha1erpago;
    LocalDate[] fechasDePago;
    double[] diasNormalMensuales, diasacumuladosMensuales;

    public double[] getDiasNormalMensuales() {
        return diasNormalMensuales;
    }

    public void setDiasNormalMensuales(double[] diasNormalMensuales) {
        this.diasNormalMensuales = diasNormalMensuales;
    }

    public double[] getDiasacumuladosMensuales() {
        return diasacumuladosMensuales;
    }

    public void setDiasacumuladosMensuales(double[] diasacumuladosMensuales) {
        this.diasacumuladosMensuales = diasacumuladosMensuales;
    }

    public LocalDate getFechadesembolso() {
        return fechadesembolso;
    }

    public void setFechadesembolso(LocalDate fechadesembolso) {
        this.fechadesembolso = fechadesembolso;
    }

    public LocalDate getFecha1erpago() {
        return fecha1erpago;
    }

    public void setFecha1erpago(LocalDate fecha1erpago) {
        this.fecha1erpago = fecha1erpago;
    }

    public LocalDate[] getFechasDePago() {
        return fechasDePago;
    }

    public void setFechasDePago(LocalDate[] fechasDePago) {
        this.fechasDePago = fechasDePago;
    }

    public LocalDate generarFecha1erPago() {
        CalcularFechaDesembolso();
        return fechadesembolso.plusDays(Periodo_Gracia);
    }

    public void generarFechasDePago() {
       fecha1erpago = generarFecha1erPago();
        fechasDePago = new LocalDate[Cuotas];
        for (int i = 0; i < Cuotas; i++) {
            fechasDePago[i] = getFecha1erpago().plusMonths(i);

            if (fechasDePago[i].getDayOfWeek() == DayOfWeek.SUNDAY) {
                fechasDePago[i] = fechasDePago[i].plusDays(1);
            }

        }
        generarDiasNyDiasA();
    }

    public void generarDiasNyDiasA() {
        diasNormalMensuales = new double[Cuotas];
        diasacumuladosMensuales = new double[Cuotas];
        for (int i = 0; i < Cuotas; i++) {
            if (i == 0) {
                diasNormalMensuales[i] = getPeriodo_Gracia();
            } else {
                diasNormalMensuales[i] = (int) ChronoUnit.DAYS.between(fechasDePago[i - 1], fechasDePago[i]);
            }

            if (i > 0) {
                diasacumuladosMensuales[i] = diasacumuladosMensuales[i - 1] + diasNormalMensuales[i];
            } else {
                diasacumuladosMensuales[i] = diasNormalMensuales[i];
            }
        }
    }

    public void CalcularFechaDesembolso() {
       fechadesembolso = LocalDate.now();
    }

}
