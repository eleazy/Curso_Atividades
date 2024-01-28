package modelos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Estadia {
    private Responsavel responsavel;
    private Crianca crianca;
    private int tempoUtilizado;

    public Estadia(Responsavel responsavel, Crianca crianca, int tempoUtilizado) {
        this.responsavel = responsavel;
        this.crianca = crianca;
        this.tempoUtilizado = tempoUtilizado;
    }

    public double calcularCustoEstadia() {
        double custoPorMinuto = 1.50;
        double custoTotal = custoPorMinuto * tempoUtilizado;

        if (tempoUtilizado > 20) {
            custoTotal -= custoTotal * 0.05;
        }
        if (tempoUtilizado > 40) {
            custoTotal -= custoTotal * 0.10;
        }
        if (tempoUtilizado > 60) {
            custoTotal -= custoTotal * 0.15;
        }

        BigDecimal dec = BigDecimal.valueOf(custoTotal);
        BigDecimal custoTotalFinal = dec.setScale(2, RoundingMode.HALF_UP);

        return custoTotalFinal.doubleValue();
    }

}