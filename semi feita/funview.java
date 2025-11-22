
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class funview extends Application{

    private funcontrol control = new funcontrol();
    private TableView<Funcionario> tableView = new TableView<>();
    
    public void start(Stage stage) {
        
        BorderPane panPrincipal = new BorderPane();
        GridPane panForm = new GridPane();

        Scene scn = new Scene(panPrincipal, 600, 500);
        
        TextField txtid = new TextField();
        TextField txtnome = new TextField();
        TextField txtbalcao = new TextField(); 
        TextField txtsalario = new TextField();  
        TextField txtlogin = new TextField();
        TextField txtsenha = new TextField();  
        TextField txttelefone = new TextField();
        TextField txtidCaixa = new TextField();
        ComboBox<String> cbFuncao = new ComboBox<>();
        cbFuncao.getItems().addAll("Gerente", "Atendente", "Caixa");

        cbFuncao.valueProperty().addListener((obs, antigo, novo) -> {
        if (novo != null) {
            switch (novo) {
                case "Gerente": control.funcaoProperty().set(1); break;
                case "Atendente": control.funcaoProperty().set(2); break;
                case "Caixa": control.funcaoProperty().set(3); break;
            }
        }
        });

        txtid.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
        txtsalario.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*(\\.\\d*)?") ? c : null));
        txtbalcao.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
        txtidCaixa.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));

        Bindings.bindBidirectional(txtid.textProperty(), control.idProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtnome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtsalario.textProperty(), control.salarioProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtlogin.textProperty(), control.loginProperty());
        Bindings.bindBidirectional(txtsenha.textProperty(), control.senhaProperty());
        Bindings.bindBidirectional(txtbalcao.textProperty(), control.balcaoProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txttelefone.textProperty(), control.telefoneProperty());
        Bindings.bindBidirectional(txtidCaixa.textProperty(), control.idCaixaProperty(), new NumberStringConverter());

        tableView.setItems( control.getLista() );

        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory( 
            item -> new ReadOnlyStringWrapper( item.getValue().getNome() )
        );
        TableColumn<Funcionario, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getId()))
        );

        tableView.getSelectionModel().selectedItemProperty().addListener(
            (obj, antigo, novo) -> {
            control.fromEntity(novo);

                if (novo != null) {
                    switch (novo.getFuncao()) {
                        case 1: cbFuncao.setValue("Gerente"); break;
                        case 2: cbFuncao.setValue("Atendente"); break;
                        case 3: cbFuncao.setValue("Caixa"); break;
                    }
                }
            }
        );


        tableView.getColumns().add( colId );
        tableView.getColumns().add( colNome );

        panPrincipal.setCenter(tableView);
        
        panForm.add(new Label("ID: "), 0, 0);
        panForm.add(txtid, 1, 0);

        panForm.add(new Label("Nome: "), 0, 1);
        panForm.add(txtnome, 1, 1);

        panForm.add(new Label("Telefone: "), 0, 2);
        panForm.add(txttelefone, 1, 2);

        panForm.add(new Label("Salário: "), 0, 3);
        panForm.add(txtsalario, 1, 3);

        panForm.add(new Label("Login: "), 0, 4);
        panForm.add(txtlogin, 1, 4);

        panForm.add(new Label("Senha: "), 0, 5);
        panForm.add(txtsenha, 1, 5);

        panForm.add(new Label("Função: "), 0, 6);
        panForm.add(cbFuncao, 1, 6);

        panForm.add(new Label("Balcão: "), 0, 7);
        panForm.add(txtbalcao, 1, 7);

        panForm.add(new Label("ID Caixa: "), 0, 8);
        panForm.add(txtidCaixa, 1, 8);


        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction( e -> control.salvar() );
        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction( e -> control.pesquisar() );
        Button btnDeletar = new Button("Deletar");
        btnDeletar.setOnAction(e -> {
            control.deletar();
            tableView.refresh();
        });
        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setOnAction(e -> {
            control.atualizar();
            tableView.refresh();
        });
        
        VBox panSuperior = new VBox();
        HBox panBotoes = new HBox();
        panBotoes.getChildren().addAll(btnSalvar, btnPesquisar, btnAtualizar, btnDeletar);       
        panPrincipal.setBottom( panBotoes );
        panSuperior.getChildren().addAll( panForm, panBotoes );
        panPrincipal.setTop(panSuperior);

        stage.setScene(scn);
        stage.setTitle("Funcionarios");
        stage.show();
    }
}
