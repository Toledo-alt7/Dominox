package br.maua.dominox;

import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class DataBase {

    // Registo de usuários
    public void cadastrarUsuario(SignUpPage sp) {
        String user = sp.userField.getText().trim();
        String pass = String.valueOf(sp.passField.getPassword());
        String confirmPass = String.valueOf(sp.confirmPassField.getPassword()); // Usar isso para validar na UI
        
        // Lógica de validação do domínio do email institucional
        String tipoUsuario;
        
        if (user.endsWith("@aluno.cps.sp.gov.br")) {
            tipoUsuario = "ALUNO";
        } else if (user.endsWith("@cps.sp.gov.br")) {
            tipoUsuario = "PROFESSOR";
        } else {
            // Se não terminar com nenhum dos dois domínios oficiais, bloqueia o cadastro
            sp.messageLabel.setForeground(Color.RED);
            sp.messageLabel.setText("Erro: Email inválido. Apenas emails com '@cps.sp.gov.br' ou '@aluno.cps.sp.gov.br' são válidos");
            return; // O 'return' encerra o método aqui e impede o acesso ao banco
        }
        try (Connection conn = ConnectionDB.getConexao()) {
            if (conn == null) {
                sp.messageLabel.setText("Erro: Sem conexão com o banco.");
                return;
            }
            setDB(conn); 

            // Cadastra no DB incluindo o tipo_usuario
            String sql = "INSERT INTO usuario (email, senha, tipo_usuario, ativo, acertos, erros) VALUES (?, ?, ?, true)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.setString(3, tipoUsuario);//utilizar depois para criar o modo professor: poderá ver um desempenho médio dos alunos que utilizaram o computador

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    sp.messageLabel.setForeground(new Color(0, 128, 0));
                    sp.messageLabel.setText("Usuário cadastrado com sucesso!");
                    Timer timer = new Timer(1500, e -> sp.frame.dispose());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                sp.messageLabel.setForeground(Color.RED);
                sp.messageLabel.setText("Erro: Este usuário já existe.");
            } else {
                e.printStackTrace();
            }
        }
    }
        public int buscarIdUsuario(String email) { /* Estou utilizando para gravar o usuário caso ele saia da fase antes de concluí-la.
        Referência: DominoxJogo em salvarProgresso [linha: +/- 477]
        */
        String sql =
            "SELECT id_usuario FROM usuario WHERE email = ?";

        try(Connection conn = ConnectionDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Validação de login e roteamento
    public String realizarLogin(String email, String senha) {
        // Retorna o cargo ("ALUNO" ou "PROFESSOR") para a interface decidir qual tela abrir
        String tipoUsuario = null; 
        String sql = "SELECT tipo_usuario FROM usuario WHERE email = ? AND senha = ? AND ativo = true";
        try (Connection conn = ConnectionDB.getConexao()) {
            if (conn != null) {
                setDB(conn); // Garante que o banco exista caso o usuário vá direto pro login
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, senha);
                    
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            tipoUsuario = rs.getString("tipo_usuario");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao validar login.");
            e.printStackTrace();
        }
        return tipoUsuario; // Retornará null se as credenciais estiverem incorretas
    }
    public List<RelatorioAluno> getRelatorioProfessor() {
        List<RelatorioAluno> lista = new ArrayList<>();
        String sql = """
                SELECT
                    u.id_usuario,
                    u.email,
                    COUNT(h.id_tentativa) partidas, -- "COALESCE" retorna o primeiro valor não nulo de uma lista de expressões
                    SUM(CASE WHEN h.concluida = true THEN 1 ELSE 0 END) concluidas,
                    COALESCE(SUM(h.acertos),0) acertos,
                    COALESCE(SUM(h.erros),0) erros,
                    COALESCE(AVG(h.pontuacao),0) media_pontos,
                    COALESCE(AVG(h.tempo_segundos),0) media_tempo
                FROM usuario u
                LEFT JOIN historico_fases h ON u.id_usuario = h.id_usuario
                WHERE u.tipo_usuario = 'ALUNO'
                GROUP BY u.id_usuario
                ORDER BY u.email
                """;

        try(Connection conn = ConnectionDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                RelatorioAluno r = new RelatorioAluno();
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setEmail(rs.getString("email"));
                r.setPartidas(rs.getInt("partidas"));
                r.setConcluidas(rs.getInt("concluidas"));
                r.setAcertos(rs.getInt("acertos"));
                r.setErros(rs.getInt("erros"));
                r.setMediaPontuacao(rs.getDouble("media_pontos"));
                r.setMediaTempo(rs.getDouble("media_tempo"));
                lista.add(r);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método novo para buscar as fases detalhadas de um aluno específico
    public List<Object[]> getHistoricoDetalhadoAluno(int idUsuario) {
        List<Object[]> detalhes = new ArrayList<>();
        String sql = "SELECT fase_numero, pontuacao, tempo_segundos, acertos, erros FROM historico_fases WHERE id_usuario = ? ORDER BY data_tentativa ASC";
        
        try (Connection conn = ConnectionDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int fase = rs.getInt("fase_numero");
                    int pontos = rs.getInt("pontuacao");
                    int tempoSegs = rs.getInt("tempo_segundos");
                    int acertos = rs.getInt("acertos");
                    int erros = rs.getInt("erros"); // Que será mapeado para a coluna 'X' na UI
                    
                    // Formata o tempo no padrão MM:SS
                    String tempoFormatado = String.format("%02d:%02d", tempoSegs / 60, tempoSegs % 60);
                    
                    detalhes.add(new Object[]{ fase, pontos, tempoFormatado, acertos, erros });
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return detalhes;
    }
    // Estruturação do banco de dados (Schemas e Tabelas)
    public void setDB(Connection conn) throws SQLException {
        // Agrupamos tudo em um único Statement para ficar mais limpo
        try (Statement stmt = conn.createStatement()) {
            
            // Schema principal (com suporte a acentuação e subscritos químicos)
            stmt.executeUpdate("CREATE SCHEMA IF NOT EXISTS Dominox CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
            stmt.executeUpdate("USE Dominox;");

            // Tabela de Usuários atualizada
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS usuario (" +
                    "id_usuario INT AUTO_INCREMENT PRIMARY KEY, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "senha VARCHAR(100) NOT NULL, " +
                    "tipo_usuario ENUM('ALUNO', 'PROFESSOR') DEFAULT 'ALUNO', " +
                    "ativo BOOLEAN DEFAULT TRUE);");

            // Tabelas das Fases do Jogo
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS fases (" +
                    "numero INT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL);");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pecas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "fase_numero INT, " +
                    "lado_a VARCHAR(50) NOT NULL, " +
                    "lado_b VARCHAR(50) NOT NULL, " +
                    "FOREIGN KEY (fase_numero) REFERENCES fases(numero) ON DELETE CASCADE);");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS conexoes (" +
                    "fase_numero INT, " +
                    "termo_a VARCHAR(50) NOT NULL, " +
                    "termo_b VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY (fase_numero, termo_a, termo_b), " +
                    "FOREIGN KEY (fase_numero) REFERENCES fases(numero) ON DELETE CASCADE);");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS historico_fases (" +
                    "id_tentativa INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_usuario INT,"+
                    "fase_numero INT,"+

                    "pontuacao INT,"+
                    "tempo_segundos INT,"+

                    "acertos INT DEFAULT 0,"+
                    "erros INT DEFAULT 0,"+

                    "dicas_usadas INT DEFAULT 0,"+

                    "concluida BOOLEAN DEFAULT FALSE,"+

                    "data_tentativa DATETIME DEFAULT CURRENT_TIMESTAMP,"+

                    "FOREIGN KEY (id_usuario)"+
                        "REFERENCES usuario(id_usuario),"+

                    "FOREIGN KEY (fase_numero)"+
                        "REFERENCES fases(numero));");

            // Injeção da query (Evita dados duplicados)
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM fases")) {
                rs.next();
                if (rs.getInt("total") == 0) {
                    System.out.println("Banco novo detectado. Populando com as fases e peças...");
                    popularDadosDoJogo(stmt);
                }
            }
        }
    }
    public void registrarResultado(int idUsuario, int faseNumero, int pontuacao, int tempoSegundos, boolean concluida) {

        String sql =
            "INSERT INTO historico_fases " +
            "(id_usuario, fase_numero, pontuacao, tempo_segundos, concluida) " +
            "VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = ConnectionDB.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, faseNumero);
            stmt.setInt(3, pontuacao);
            stmt.setInt(4, tempoSegundos);
            stmt.setBoolean(5, concluida);
            stmt.executeUpdate();
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    

    //Inserção de dados
    private void popularDadosDoJogo(Statement stmt) throws SQLException {
        String querySQL = """
                    -- FASE 1
                    INSERT INTO fases (numero, nome) VALUES (1, 'Ácidos e Bases - 1');
                    INSERT INTO pecas (fase_numero, lado_a, lado_b) VALUES
                    (1, 'Ácido', 'Base'), (1, 'NaOH', 'Monoácido'), (1, 'HCN', 'Forte'), (1, 'H₂SO₄', 'Dibase'),
                    (1, 'H₃PO₄', 'Al(OH)₃'), (1, 'Mg(OH)₂', 'Ácido'), (1, 'NaOH', 'Hidrácido'), (1, 'Al(OH)₃', 'Oxiácido'),
                    (1, 'Mg(OH)₂', 'Forte'), (1, 'H₂SO₄', 'Monobase'), (1, 'H₃PO₄', 'Tribase'), (1, 'HCN', 'Moderado'),
                    (1, 'Fraco', 'Monobase'), (1, 'Ácido', 'Dibase'), (1, 'Ácido', 'Tribase'), (1, 'Ácido', 'Fraco'),
                    (1, 'Base', 'Moderado'), (1, 'Base', 'Hidrácido'), (1, 'Base', 'Oxiácido'), (1, 'Monoácido', 'Monobase'),
                    (1, 'Monoácido', 'Forte'), (1, 'Diácido', 'Dibase'), (1, 'Triácido', 'Tribase'), (1, 'Diácido', 'Hidrácido'),
                    (1, 'Diácido', 'Moderado'), (1, 'Triácido', 'Forte'), (1, 'Triácido', 'Fraco'), (1, 'Oxiácido', 'Fraco');

                    INSERT INTO conexoes (fase_numero, termo_a, termo_b) VALUES
                    (1, 'Ácido', 'Monoácido'), (1, 'Ácido', 'Diácido'), (1, 'Ácido', 'Triácido'), (1, 'Ácido', 'Forte'), 
                    (1, 'Ácido', 'Moderado'), (1, 'Ácido', 'Fraco'), (1, 'Ácido', 'Hidrácido'), (1, 'Ácido', 'Oxiácido'), 
                    (1, 'Ácido', 'H₂SO₄'), (1, 'Ácido', 'H₃PO₄'), (1, 'Ácido', 'HCN'), (1, 'Base', 'Monobase'), 
                    (1, 'Base', 'Dibase'), (1, 'Base', 'Tribase'), (1, 'Base', 'Forte'), (1, 'Base', 'Fraco'), 
                    (1, 'Base', 'NaOH'), (1, 'Base', 'Al(OH)₃'), (1, 'Base', 'Mg(OH)₂'), (1, 'Monoácido', 'HCN'), 
                    (1, 'Monobase', 'NaOH'), (1, 'Diácido', 'H₂SO₄'), (1, 'Dibase', 'Mg(OH)₂'), (1, 'Triácido', 'H₃PO₄'), 
                    (1, 'Tribase', 'Al(OH)₃'), (1, 'Forte', 'NaOH'), (1, 'Forte', 'H₂SO₄'), (1, 'Moderado', 'H₃PO₄'), 
                    (1, 'Fraco', 'Al(OH)₃'), (1, 'Fraco', 'Mg(OH)₂'), (1, 'Fraco', 'HCN'), (1, 'Hidrácido', 'HCN'), 
                    (1, 'Oxiácido', 'H₂SO₄'), (1, 'Oxiácido', 'H₃PO₄');

                    -- FASE 2
                    INSERT INTO fases (numero, nome) VALUES (2, 'Ácidos e Bases - 2');
                    INSERT INTO pecas (fase_numero, lado_a, lado_b) VALUES
                    (2, 'Ácido', 'Base'), (2, 'HCl', 'Tetra-'), (2, 'H₂S', 'Forte'), (2, 'H₃PO₄', 'Di-'),
                    (2, 'H₄P₂O₇', 'Al(OH)₃'), (2, 'NaOH', 'Ácido'), (2, 'Ca(OH)₂', 'Hidrácido'), (2, 'Sn(OH)₄', 'Oxiácido'),
                    (2, 'Sn(OH)₄', 'Forte'), (2, 'HCl', 'Mono-'), (2, 'H₂S', 'Tri-'), (2, 'H₃PO₄', 'Moderado'),
                    (2, 'H₄P₂O₇', 'Mono-'), (2, 'Ácido', 'NaOH'), (2, 'Ca(OH)₂', 'Moderado'), (2, 'Al(OH)₃', 'Mono-'),
                    (2, 'Fraco', 'Tetra-'), (2, 'Fraco', 'Oxiácido'), (2, 'Ácido', 'Tri-'), (2, 'Base', 'Moderado'),
                    (2, 'Base', 'Hidrácido'), (2, 'Base', 'Oxiácido'), (2, 'Forte', 'Hidrácido'), (2, 'Tetra-', 'Forte'),
                    (2, 'Fraco', 'Di-'), (2, 'Forte', 'Tri-'), (2, 'Fraco', 'Ácido'), (2, 'Ácido', 'Di-');

                    INSERT INTO conexoes (fase_numero, termo_a, termo_b) VALUES
                    (2, 'Ácido', 'Mono-'), (2, 'Ácido', 'Di-'), (2, 'Ácido', 'Tri-'), (2, 'Ácido', 'Tetra-'), 
                    (2, 'Ácido', 'Fraco'), (2, 'Ácido', 'Moderado'), (2, 'Ácido', 'Forte'), (2, 'Ácido', 'Hidrácido'), 
                    (2, 'Ácido', 'Oxiácido'), (2, 'Ácido', 'HCl'), (2, 'Ácido', 'H₂S'), (2, 'Ácido', 'H₃PO₄'), 
                    (2, 'Ácido', 'H₄P₂O₇'), (2, 'Base', 'Mono-'), (2, 'Base', 'Di-'), (2, 'Base', 'Tri-'), 
                    (2, 'Base', 'Tetra-'), (2, 'Base', 'Fraco'), (2, 'Base', 'Forte'), (2, 'Base', 'NaOH'), 
                    (2, 'Base', 'Ca(OH)₂'), (2, 'Base', 'Al(OH)₃'), (2, 'Base', 'Sn(OH)₄'), (2, 'Mono-', 'NaOH'), 
                    (2, 'Mono-', 'HCl'), (2, 'Di-', 'Ca(OH)₂'), (2, 'Di-', 'H₂S'), (2, 'Tri-', 'Al(OH)₃'), 
                    (2, 'Tri-', 'H₃PO₄'), (2, 'Tetra-', 'Sn(OH)₄'), (2, 'Tetra-', 'H₄P₂O₇'), (2, 'Fraco', 'H₂S'), 
                    (2, 'Fraco', 'Al(OH)₃'), (2, 'Fraco', 'Sn(OH)₄'), (2, 'Forte', 'HCl'), (2, 'Forte', 'NaOH'), 
                    (2, 'Forte', 'H₄P₂O₇'), (2, 'Forte', 'Ca(OH)₂'), (2, 'Moderado', 'H₃PO₄'), (2, 'Oxiácido', 'H₃PO₄'), 
                    (2, 'Oxiácido', 'H₄P₂O₇'), (2, 'Hidrácido', 'HCl'), (2, 'Hidrácido', 'H₂S');
                    
                    
                    -- FASE 3: Sais
                    INSERT INTO fases (numero, nome) VALUES (3, 'Sais');

                    INSERT INTO pecas (fase_numero, lado_a, lado_b) VALUES
                    (3, 'NaCl', 'Oxissal'), (3, 'NaCl', 'Insolúvel'), (3, 'CaCO₃', 'Solúvel'), (3, 'CaCO₃', 'Halóide'),
                    (3, 'BaSO₄', 'Solúvel'), (3, 'BaSO₄', 'Halóide'), (3, 'NH₄Cl', 'Oxissal'), (3, 'NH₄Cl', 'Insolúvel'),
                    (3, 'Na₂CO₃', 'Insolúvel'), (3, 'Na₂CO₃', 'Halóide'), (3, 'PbCl₂', 'Solúvel'), (3, 'PbCl₂', 'Oxissal'),
                    (3, 'Solúvel', 'Neutro'), (3, 'Solúvel', 'Ácido'), (3, 'Solúvel', 'Oxissal'), (3, 'Halóide', 'Neutro'),
                    (3, 'Insolúvel', 'Ácido'), (3, 'Insolúvel', 'Básico'), (3, 'Neutro', 'Oxissal'), (3, 'Neutro', 'Básico'),
                    (3, 'Neutro', 'Halóide'), (3, 'Básico', 'Oxissal'), (3, 'Ácido', 'Oxissal'), (3, 'Ácido', 'Básico'),
                    (3, 'Ácido', 'Halóide'), (3, 'Halóide', 'Oxissal'), (3, 'Básico', 'Halóide'), (3, 'Neutro', 'Insolúvel');

                    INSERT INTO conexoes (fase_numero, termo_a, termo_b) VALUES
                    (3, 'Oxissal', 'CaCO₃'), (3, 'Oxissal', 'BaSO₄'), (3, 'Oxissal', 'Na₂CO₃'), (3, 'Halóide', 'NaCl'), 
                    (3, 'Halóide', 'NH₄Cl'), (3, 'Halóide', 'PbCl₂'), (3, 'Solúvel', 'Na₂CO₃'), (3, 'Solúvel', 'NaCl'), 
                    (3, 'Solúvel', 'NH₄Cl'), (3, 'Insolúvel', 'CaCO₃'), (3, 'Insolúvel', 'BaSO₄'), (3, 'Insolúvel', 'PbCl₂'), 
                    (3, 'Neutro', 'BaSO₄'), (3, 'Neutro', 'NaCl'), (3, 'Ácido', 'NH₄Cl'), (3, 'Ácido', 'PbCl₂'), 
                    (3, 'Básico', 'CaCO₃'), (3, 'Básico', 'Na₂CO₃');

                    
                    -- FASE 4: Óxidos
                    INSERT INTO fases (numero, nome) VALUES (4, 'Óxidos');

                    INSERT INTO pecas (fase_numero, lado_a, lado_b) VALUES
                    (4, 'CO₂', 'Básico'), (4, 'CO₂', 'Iônico'), (4, 'SO₃', 'Neutro'), (4, 'SO₃', 'Anfótero'),
                    (4, 'CaO', 'Ácido'), (4, 'CaO', 'Covalente'), (4, 'Na₂O', 'Neutro'), (4, 'Na₂O', 'Anfótero'),
                    (4, 'CO', 'Ácido'), (4, 'CO', 'Iônico'), (4, 'Al₂O₃', 'Ácido'), (4, 'Al₂O₃', 'Covalente'),
                    (4, 'Ácido', 'Básico'), (4, 'Básico', 'Básico'), (4, 'Ácido', 'Neutro'), (4, 'Ácido', 'Anfótero'),
                    (4, 'Ácido', 'Iônico'), (4, 'Ácido', 'Covalente'), (4, 'Neutro', 'Básico'), (4, 'Anfótero', 'Básico'),
                    (4, 'Básico', 'Iônico'), (4, 'Básico', 'Covalente'), (4, 'Neutro', 'Anfótero'), (4, 'Neutro', 'Iônico'),
                    (4, 'Anfótero', 'Iônico'), (4, 'Neutro', 'Covalente'), (4, 'Anfótero', 'Covalente'), (4, 'Iônico', 'Covalente');

                    INSERT INTO conexoes (fase_numero, termo_a, termo_b) VALUES
                    (4, 'Ácido', 'CO₂'), (4, 'Ácido', 'SO₃'), (4, 'Básico', 'CaO'), (4, 'Básico', 'Na₂O'), 
                    (4, 'Neutro', 'CO'), (4, 'Anfótero', 'Al₂O₃'), (4, 'Iônico', 'CaO'), (4, 'Iônico', 'Na₂O'), 
                    (4, 'Iônico', 'Al₂O₃'), (4, 'Covalente', 'CO₂'), (4, 'Covalente', 'SO₃'), (4, 'Covalente', 'CO');

                    
                    -- FASE 5: Geral
                    INSERT INTO fases (numero, nome) VALUES (5, 'Geral');

                    INSERT INTO pecas (fase_numero, lado_a, lado_b) VALUES
                    (5, 'HCl', 'Base (Básico)'), (5, 'HCl', 'Insolúvel'), (5, 'HCN', 'Sal'), (5, 'HCN', 'Covalente'),
                    (5, 'NaOH', 'Ácido'), (5, 'NaOH', 'Fraco'), (5, 'Al(OH)₃', 'Óxido'), (5, 'Al(OH)₃', 'Forte'),
                    (5, 'NaCl', 'Base (Básico)'), (5, 'NaCl', 'Insolúvel'), (5, 'CaCO₃', 'Óxido'), (5, 'CaCO₃', 'Solúvel'),
                    (5, 'CO₂', 'Sal'), (5, 'CO₂', 'Iônico'), (5, 'CaO', 'Ácido'), (5, 'CaO', 'Covalente'),
                    (5, 'Ácido', 'Base (Básico)'), (5, 'Ácido', 'Sal'), (5, 'Ácido', 'Óxido'), (5, 'Base (Básico)', 'Sal'),
                    (5, 'Base (Básico)', 'Óxido'), (5, 'Sal', 'Óxido'), (5, 'Forte', 'Fraco'), (5, 'Forte', 'Solúvel'),
                    (5, 'Forte', 'Iônico'), (5, 'Fraco', 'Insolúvel'), (5, 'Fraco', 'Covalente'), (5, 'Solúvel', 'Iônico');

                    INSERT INTO conexoes (fase_numero, termo_a, termo_b) VALUES
                    (5, 'Ácido', 'Sal'), (5, 'Ácido', 'Óxido'), (5, 'Ácido', 'Forte'), (5, 'Ácido', 'Fraco'), 
                    (5, 'Ácido', 'HCl'), (5, 'Ácido', 'HCN'), (5, 'Ácido', 'CO₂'), (5, 'Base (Básico)', 'Sal'), 
                    (5, 'Base (Básico)', 'Óxido'), (5, 'Base (Básico)', 'Forte'), (5, 'Base (Básico)', 'Fraco'), 
                    (5, 'Base (Básico)', 'NaOH'), (5, 'Base (Básico)', 'Al(OH)₃'), (5, 'Base (Básico)', 'CaO'), 
                    (5, 'Base (Básico)', 'CaCO₃'), (5, 'Sal', 'NaCl'), (5, 'Sal', 'CaCO₃'), (5, 'Sal', 'Solúvel'), 
                    (5, 'Sal', 'Insolúvel'), (5, 'Óxido', 'CO₂'), (5, 'Óxido', 'CaO'), (5, 'Óxido', 'Iônico'), 
                    (5, 'Óxido', 'Covalente'), (5, 'Forte', 'HCl'), (5, 'Forte', 'NaOH'), (5, 'Fraco', 'Al(OH)₃'), 
                    (5, 'Fraco', 'HCN'), (5, 'Solúvel', 'NaCl'), (5, 'Insolúvel', 'CaCO₃'), (5, 'Covalente', 'CO₂'), 
                    (5, 'Iônico', 'CaO');
                    """;
        
        stmt.execute(querySQL);
    }
    
}
