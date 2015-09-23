package net.tucompualdia.ajuste.modelos;

import com.orm.SugarRecord;

import java.text.DecimalFormat;

/**
 * Created by jairo.fernandez on 24/07/2015.
 */
public class Lamina extends SugarRecord<Lamina> {
    String tipoLamina;
    Float ancho;
    Float alto;
    Float valor;
    Float valorCm;

    public Lamina() {
    }

    public Lamina(String tipoLamina, Float ancho, Float alto, Float valor, Float valorCm) {
        this.tipoLamina = tipoLamina;
        this.ancho = ancho;
        this.alto = alto;
        this.valor = valor;
        this.valorCm = valorCm;
    }

    public String getTipoLamina() {
        return tipoLamina;
    }

    public void setTipoLamina(String tipoLamina) {
        this.tipoLamina = tipoLamina;
    }

    public Float getAncho() {
        return ancho;
    }

    public void setAncho(Float ancho) {
        this.ancho = ancho;
    }

    public Float getAlto() {
        return alto;
    }

    public void setAlto(Float alto) {
        this.alto = alto;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Float getValorCm() {
        return valorCm;
    }

    public void setValorCm(Float valorCm) {
        this.valorCm = valorCm;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return "" +
                "" + tipoLamina +
                " (" + ancho +
                "X" + alto +
                ")" + " $..."+ df.format(valor);
    }
}