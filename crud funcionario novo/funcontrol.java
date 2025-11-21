
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

        if (idExiste(id.get())) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("ID já existe!");
            alerta.setContentText("Escolha um ID diferente.");
            alerta.showAndWait();
            return;
        }

        if (funcao.get() == 1) {
            idCaixa.set(0);
            balcao.set(0);
        } else if (funcao.get() == 2) {
            balcao.set(0);
        } else if (funcao.get() == 3) {
            idCaixa.set(0);
        }

        if (f < 1 || f > 3) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setHeaderText("Função inválida");
            alert.setContentText("A função deve ser 1, 2 ou 3.");
            alert.showAndWait();
            return;
        }

        lista.add(toEntity());
    }

    public void pesquisar() {
        for (Funcionario f : lista) {
            if (f.getId() == id.get()) {
                fromEntity(f);
                break;
            }
        }
    }

    public void atualizar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar atualização");
        alerta.setHeaderText("Você deseja realmente atualizar este funcionário?");
        alerta.setContentText("Essa ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            for (Funcionario f : lista) {
                if (f.getId() == id.get()) {
                    f.setNome(nome.get());
                    f.setFuncao(funcao.get());
                    f.setBalcao(balcao.get());
                    f.setTelefone(telefone.get());
                    f.setIdCaixa(idCaixa.get());

                    if (funcao.get() == 1) {
                        idCaixa.set(0);
                        balcao.set(0);
                    } else if (funcao.get() == 2) {
                        balcao.set(0);
                    } else if (funcao.get() == 3) {
                        idCaixa.set(0);
                    }
                    break;
                }   
            }
        }
    }

    public void deletar() {
        Funcionario alvo = null;

        for (Funcionario f : lista) {
            if (f.getId() == id.get()) {
                alvo = f;
                break;
            }
        }
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar deletamento");
        alerta.setHeaderText("Você deseja realmente deletar este funcionário?");
        alerta.setContentText("Essa ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (alvo != null) {
                lista.remove(alvo);

                id.set(0);
                nome.set("");
                funcao.set(0);
                balcao.set(0);
                telefone.set("");
                idCaixa.set(0);
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

    public ObservableList<Funcionario> getLista() {
        return lista;
    }
}