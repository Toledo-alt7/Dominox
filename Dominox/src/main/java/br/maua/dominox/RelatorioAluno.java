package br.maua.dominox;

public class RelatorioAluno {
    private int idUsuario;
    private String email;
    private int partidas;
    private int concluidas;
    private int acertos;
    private int erros;
    private double mediaPontuacao;
    private double mediaTempo;

    public RelatorioAluno() {}

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getPartidas() { return partidas; }
    public void setPartidas(int partidas) { this.partidas = partidas; }

    public int getConcluidas() { return concluidas; }
    public void setConcluidas(int concluidas) { this.concluidas = concluidas; }

    public int getAcertos() { return acertos; }
    public void setAcertos(int acertos) { this.acertos = acertos; }

    public int getErros() { return erros; }
    public void setErros(int erros) { this.erros = erros; }

    public double getMediaPontuacao() { return mediaPontuacao; }
    public void setMediaPontuacao(double mediaPontuacao) { this.mediaPontuacao = mediaPontuacao; }

    public double getMediaTempo() { return mediaTempo; }
    public void setMediaTempo(double mediaTempo) { this.mediaTempo = mediaTempo; }
}
