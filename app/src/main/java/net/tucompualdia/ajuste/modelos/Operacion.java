package net.tucompualdia.ajuste.modelos;

import java.text.DecimalFormat;

/**
 * Creado por Jairo Fernández para Tu compu al día 26/07/15.
 */
public class Operacion {
    String descripcion;
    Float valor;

    public Operacion() {
    }

    public Operacion(String descripcion, Float valor) {
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return "" + descripcion +
                " =  $... " + df.format(valor);
    }
}
