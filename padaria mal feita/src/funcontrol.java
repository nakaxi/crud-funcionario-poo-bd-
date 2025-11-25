
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class funcontrol {

    private ObservableList<Funcionario> lista = FXCollections.observableArrayList();

    private IntegerProperty id = new SimpleIntegerProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private DoubleProperty salario = new SimpleDoubleProperty(0.0);
    private StringProperty login = new SimpleStringProperty("");
    private StringProperty senha = new SimpleStringProperty("");
    private IntegerProperty funcao = new SimpleIntegerProperty(0);
    private IntegerProperty balcao = new SimpleIntegerProperty(0);
    private StringProperty telefone = new SimpleStringProperty("");
    private IntegerProperty idCaixa = new SimpleIntegerProperty(0);

    public Funcionario toEntity() {

        Funcionario f = new Funcionario();
        f.setId(id.get());
        f.setNome(nome.get());
        f.setSalario(salario.get());
        f.setLogin(login.get());
        f.setSenha(senha.get());
        f.setFuncao(funcao.get());
        f.setBalcao(balcao.get());
        f.setTelefone(telefone.get());
        f.setIdCaixa(idCaixa.get());
        return f;
    }

    public void fromEntity(Funcionario f) {

        if (f != null) {
            id.set(f.getId());
            nome.set(f.getNome());
            salario.set(f.getSalario());
            login.set(f.getLogin());
            senha.set(f.getSenha());
            funcao.set(f.getFuncao());
            balcao.set(f.getBalcao());
            telefone.set(f.getTelefone());
            idCaixa.set(f.getIdCaixa());
        }
    }

    public void salvar() {
        int f = funcao.get();

        if (loginExiste(login.get())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Login já existente!");
            alert.setContentText("Escolha outro login.");
            alert.showAndWait();
            return;
        }

        if (f < 1 || f > 3) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setHeaderText("Função inválida");
            alert.setContentText("A função deve ser 1, 2 ou 3.");
            alert.showAndWait();
            return;
        }

        String cargo;
        if (f == 1) {
            idCaixa.set(0);
            balcao.set(0);
            cargo = "Gerente";
        } else if (f == 2) {
            balcao.set(0);
            cargo = "Caixa";
        } else {
            idCaixa.set(0);
            cargo = "Atendente";
        }

        String loginFunc = login.get();
        String senhaFunc = senha.get();
        try (Connection con = conexao.get()) {
            String sql = "INSERT INTO Funcionario (nome, salario, telefone, cargo, loginFunc, senhaFunc, balcao, idCaixa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            var ps = con.prepareStatement(sql);
            ps.setString(1, nome.get());
            ps.setDouble(2, salario.get());
            ps.setString(3, telefone.get());
            ps.setString(4, cargo);
            ps.setString(5, loginFunc);
            ps.setString(6, senhaFunc);
            ps.setInt(7, balcao.get());
            ps.setInt(8, idCaixa.get());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    private boolean loginExiste(String loginFunc) {
        String sql = "SELECT COUNT(*) FROM Funcionario WHERE loginFunc = ?";
        try (Connection con = conexao.get();
            var ps = con.prepareStatement(sql)) {
            ps.setString(1, loginFunc);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return false;
    }

    public void pesquisar() {
        int idFuncionario = id.get();

        String sql = "SELECT * FROM Funcionario WHERE id = ?";

        try (Connection con = conexao.get();
            var ps = con.prepareStatement(sql)) {

            ps.setInt(1, idFuncionario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Funcionario f = new Funcionario();
                    f.setId(rs.getInt("id"));
                    f.setNome(rs.getString("nome"));
                    f.setTelefone(rs.getString("telefone"));
                    f.setSalario(rs.getDouble("salario"));
                    f.setLogin(rs.getString("loginFunc"));
                    f.setSenha(rs.getString("senhaFunc"));
                
                    String cargo = rs.getString("cargo");
                    switch (cargo) {
                        case "Gerente": f.setFuncao(1); f.setBalcao(0); f.setIdCaixa(0); break;
                        case "Caixa": f.setFuncao(2); f.setBalcao(0); break;
                        case "Atendente": f.setFuncao(3); f.setIdCaixa(0); break;
                        default: f.setFuncao(0); break;
                    }

                    f.setBalcao(rs.getInt("balcao"));
                    f.setIdCaixa(rs.getInt("idCaixa"));

                    fromEntity(f); // Atualiza os campos do formulário
                } else {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Pesquisar");
                    alerta.setHeaderText("Funcionário não encontrado");
                    alerta.showAndWait();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Erro ao pesquisar funcionário");
            erro.setContentText(e.getMessage());
            erro.showAndWait();
        }   
    }

    public void atualizar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar atualização");
        alerta.setHeaderText("Você deseja realmente atualizar este funcionário?");
        alerta.setContentText("Essa ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String sql = "UPDATE Funcionario SET nome = ?, salario = ?, telefone = ?, cargo = ?, " +
                     "loginFunc = ?, senhaFunc = ?, balcao = ?, idCaixa = ? WHERE id = ?";

            String cargo;
            switch (funcao.get()) {
                case 1: cargo = "Gerente"; idCaixa.set(0); balcao.set(0); break;
                case 2: cargo = "Caixa"; balcao.set(0); break;
                case 3: cargo = "Atendente"; idCaixa.set(0); break;
                default: cargo = ""; break;
            }

            try (Connection con = conexao.get();
                 var ps = con.prepareStatement(sql)) {

                ps.setString(1, nome.get());
                ps.setDouble(2, salario.get());
                ps.setString(3, telefone.get());
                ps.setString(4, cargo);
                ps.setString(5, login.get());
                ps.setString(6, senha.get());
                ps.setInt(7, balcao.get());
                ps.setInt(8, idCaixa.get());
                ps.setInt(9, id.get());

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setTitle("Atualizar");
                    ok.setHeaderText("Funcionário atualizado com sucesso");
                    ok.showAndWait();

                    // Atualiza a lista local
                    lista.removeIf(f -> f.getId() == id.get());
                    lista.add(toEntity());
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText("Funcionário não encontrado no banco");
                    erro.showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText("Erro ao atualizar funcionário");
                erro.setContentText(e.getMessage());
                erro.showAndWait();
            }
        }
    }

    public void deletar() {
        int idFuncionario = id.get();

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar deletamento");
        alerta.setHeaderText("Você deseja realmente deletar este funcionário?");
        alerta.setContentText("Essa ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection con = conexao.get()) {
                String sql = "DELETE FROM Funcionario WHERE id = ?";
                var ps = con.prepareStatement(sql);
                ps.setInt(1, idFuncionario);
                int linhasAfetadas = ps.executeUpdate();
                   
                if (linhasAfetadas > 0) {
                    System.out.println("Funcionário deletado do BD");
                    lista.removeIf(f -> f.getId() == idFuncionario);

                    id.set(0);
                    nome.set("");
                    funcao.set(0);
                    balcao.set(0);
                    telefone.set("");
                    idCaixa.set(0);
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText("Não foi possível deletar o funcionário do banco");
                    erro.showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText("Erro ao deletar funcionário");
                erro.setContentText(e.getMessage());
                erro.showAndWait();
            }
        }
    }

    private boolean idExiste(int idProcurado) {
        for (Funcionario f : lista) {
                if (f.getId() == idProcurado) {
                return true;
            }
        }
        return false;
    }


    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public DoubleProperty salarioProperty() { 
        return salario; 
    }

    public StringProperty loginProperty() { 
        return login; 
    }

    public StringProperty senhaProperty() { 
        return senha; 
    }


    public IntegerProperty funcaoProperty() {
        return funcao;
    }

    public IntegerProperty balcaoProperty() {
        return balcao;
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    public IntegerProperty idCaixaProperty() {
        return idCaixa;
    }

    //pega do bd joga na tabela
    public ObservableList<Funcionario> buscarTodos() {
        ObservableList<Funcionario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Funcionario";

        try (Connection conn = conexao.get();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setTelefone(rs.getString("telefone"));
                f.setSalario(rs.getDouble("salario"));
                f.setLogin(rs.getString("loginFunc"));
                f.setSenha(rs.getString("senhaFunc"));
                String cargo = rs.getString("cargo"); 
                int c;
                if (cargo != null) cargo = cargo.trim();
                    switch (cargo) {    
                        case "Gerente":
                            c = 1;
                            idCaixa.set(0);
                            balcao.set(0);
                            break;
                        case "Caixa":
                            c = 2;
                            balcao.set(0);
                            break;
                        case "Atendente":
                            c = 3;
                            idCaixa.set(0);
                            break;
                        default:
                            c = 0;
                            System.out.println("Cargo desconhecido: '" + cargo + "'");
                            break;
                    }
                f.setFuncao(c);
                f.setBalcao(rs.getInt("balcao"));
                f.setIdCaixa(rs.getInt("idCaixa"));

                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}