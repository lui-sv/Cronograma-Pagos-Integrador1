/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author Administrador
 */
abstract class ClaseRegistro {
    double TEA, MontoTC, MontoPP,MontoPPFinal,MontoSolicitado, MontoTotal;
    String Tipo_Seguro;
    int Periodo_Gracia, Cuotas;

    public double getMontoPPFinal() {
        return MontoPPFinal;
    }

    public void setMontoPPFinal(double MontoPPFinal) {
        this.MontoPPFinal = MontoPPFinal;
    }
    
    public double getTEA() {
        return TEA;
    }

    public void setTEA(double TEA) {
        this.TEA = TEA;
    }

    public double getMontoTC() {
        return MontoTC;
    }

    public void setMontoTC(double MontoTC) {
        this.MontoTC = MontoTC;
    }

    public double getMontoPP() {
        return MontoPP;
    }

    public void setMontoPP(double MontoPP) {
        this.MontoPP = MontoPP;
    }

    public double getMontoSolicitado() {
        return MontoSolicitado;
    }

    public void setMontoSolicitado(double MontoSolicitado) {
        this.MontoSolicitado = MontoSolicitado;
    }

    public double getMontoTotal() {
        return MontoTotal;
    }

    public void setMontoTotal(double MontoTotal) {
        this.MontoTotal = MontoTotal;
    }

    public String getTipo_Seguro() {
        return Tipo_Seguro;
    }

    public void setTipo_Seguro(String Tipo_Seguro) {
        this.Tipo_Seguro = Tipo_Seguro;
    }

    public int getPeriodo_Gracia() {
        return Periodo_Gracia;
    }

    public void setPeriodo_Gracia(int Periodo_Gracia) {
        this.Periodo_Gracia = Periodo_Gracia;
    }

    public int getCuotas() {
        return Cuotas;
    }

    public void setCuotas(int Cuotas) {
        this.Cuotas = Cuotas;
    }
    public void DefinirMontoPP(){
        this.MontoPPFinal = getMontoPP()+(getMontoPP()*0.02);
    }
     public void DefinirMontoTotal(){   
        this.MontoTotal = getMontoPPFinal()+getMontoSolicitado()+getMontoTC();
    }
    
    
}
