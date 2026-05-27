package br.maua.dominox;
import java.util.*;

public class ChemistryRules {

    // Pre-built adjacency map: value -> set of values it can connect with
    private static final Map<String, Set<String>> CONNECTIONS = new HashMap<>();

    static {
        // Self-connections (same value connects with itself)
        selfConnect("Ácido");
        selfConnect("Base");
        selfConnect("Monoácido");
        selfConnect("Monobase");
        selfConnect("Diácido");
        selfConnect("Dibase");
        selfConnect("Triácido");
        selfConnect("Tribase");
        selfConnect("Forte");
        selfConnect("Moderado");
        selfConnect("Fraco");
        selfConnect("Hidrácido");
        selfConnect("Oxiácido");
        selfConnect("NaOH");
        selfConnect("Al(OH)3");
        selfConnect("Mg(OH)2");
        selfConnect("H2SO4");
        selfConnect("H3PO4");
        selfConnect("HCN");

        // --- Ácido ---
        connect("Ácido", "Monoácido");
        connect("Ácido", "Diácido");
        connect("Ácido", "Triácido");
        connect("Ácido", "Forte");
        connect("Ácido", "Moderado");
        connect("Ácido", "Fraco");
        connect("Ácido", "Hidrácido");
        connect("Ácido", "Oxiácido");
        connect("Ácido", "H2SO4");
        connect("Ácido", "H3PO4");
        connect("Ácido", "HCN");

        // --- Base ---
        connect("Base", "Monobase");
        connect("Base", "Dibase");
        connect("Base", "Tribase");
        connect("Base", "Forte");
        connect("Base", "Fraco");
        connect("Base", "NaOH");
        connect("Base", "Al(OH)3");
        connect("Base", "Mg(OH)2");

        // --- Monoácido ---
        connect("Monoácido", "HCN");

        // --- Monobase ---
        connect("Monobase", "NaOH");

        // --- Diácido ---
        connect("Diácido", "H2SO4");

        // --- Dibase ---
        connect("Dibase", "Mg(OH)2");

        // --- Triácido ---
        connect("Triácido", "H3PO4");

        // --- Tribase ---
        connect("Tribase", "Al(OH)3");

        // --- Forte ---
        connect("Forte", "NaOH");
        connect("Forte", "H2SO4");

        // --- Moderado ---
        connect("Moderado", "H3PO4");

        // --- Fraco ---
        connect("Fraco", "Al(OH)3");
        connect("Fraco", "Mg(OH)2");
        connect("Fraco", "HCN");

        // --- Hidrácido ---
        connect("Hidrácido", "HCN");

        // --- Oxiácido ---
        connect("Oxiácido", "H2SO4");
        connect("Oxiácido", "H3PO4");
    }

    private static void selfConnect(String a) {
        CONNECTIONS.computeIfAbsent(a, k -> new HashSet<>()).add(a);
    }

    private static void connect(String a, String b) {
        CONNECTIONS.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        CONNECTIONS.computeIfAbsent(b, k -> new HashSet<>()).add(a);
    }

    /** Returns true if valueA can connect with valueB. */
    public static boolean canConnect(String a, String b) {
        if (a == null || b == null) return false;
        if (a.equals(b)) return true; // same value always connects
        Set<String> neighbors = CONNECTIONS.get(a);
        return neighbors != null && neighbors.contains(b);
    }

    /**
     * Check if a domino side can connect to the board end value.
     * Either the left or right side of the domino must connect with boardEnd.
     */
    public static boolean dominoFits(Domino d, String boardEnd) {
        return canConnect(d.getLeft(), boardEnd) || canConnect(d.getRight(), boardEnd);
    }


    public static boolean leftFacesOut(Domino d, String boardEnd) {
        // If right connects to board, left faces out
        if (canConnect(d.getRight(), boardEnd)) return true;
        return false;
    }
}