package br.maua.dominox;

import java.util.HashMap;
import java.util.Map;

public class FaseRegistry {
    private static Map<Integer, Fase> fases = new HashMap<>();
    static{
        fases.put(1, new Fase1AcidoBase());
        fases.put(2, new Fase2AcidoBase());
        fases.put(3, new Fase3Sais());
        fases.put(4, new Fase4Oxidos());
        fases.put(5, new Fase5Geral());
    }

    public static Fase getFase(int numeroFase) {
        Fase fase = fases.get(numeroFase);
        return fase;
    }
}
