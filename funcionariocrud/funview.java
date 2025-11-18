
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class funview extends Application{

    private funcontrol control = new funcontrol();
    private TableView<Funcionario> tableView = new TableView<>();
    
    public void start(Stage stage) {
        
        BorderPane panPrincipal = new BorderPane();
        GridPane panForm = new GridPane();

        Scene scn = new Scene(panPrincipal, 400, 300);
        
        TextField txtid = new TextField();
        TextField txtnome = new TextField();
        TextField txtfuncao = new TextField();
        TextField txtbalcao = new TextField();        
        TextField txttelefone = new TextField();
        TextField txtidCaixa = new TextField();

        txtid.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
        txtfuncao.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
        txtbalcao.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
        txtidCaixa.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));

        Bindings.bindBidirectional(txtid.textProperty(), control.idProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtnome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtfuncao.textProperty(), control.funcaoProperty(), new NumberStringConverter());
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
           (obj, antigo, novo) -> control.fromEntity(novo)
        );

        tableView.getColumns().add( colId );
        tableView.getColumns().add( colNome );

        panPrincipal.setCenter(tableView);
        
        panForm.add(new Label("ID: "), 0, 0);
        panForm.add(txtid, 1, 0);

        panForm.add(new Label("Nome: "), 0, 1);
        panForm.add(txtnome, 1, 1);

        panForm.add(new Label("Função: "), 0, 2);
        panForm.add(txtfuncao, 1, 2);

        panForm.add(new Label("Balcão: "), 0, 3);
        panForm.add(txtbalcao, 1, 3);

        panForm.add(new Label("Telefone: "), 0, 4);
        panForm.add(txttelefone, 1, 4);

        panForm.add(new Label("ID Caixa: "), 0, 5);
        panForm.add(txtidCaixa, 1, 5);


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

    public static void main(String[] args) {
        Application.launch(funview.class, args);
    }
}
