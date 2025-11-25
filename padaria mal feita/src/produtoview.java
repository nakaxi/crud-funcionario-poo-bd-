import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class produtoview extends Application {

    private produtocontrol control = new produtocontrol();
    private TableView<produto> tableView = new TableView<>();

    @Override
    public void start(Stage stage) {

        BorderPane panPrincipal = new BorderPane();
        GridPane panForm = new GridPane();

        Scene scn = new Scene(panPrincipal, 500, 400);

        TextField txtId = new TextField();
        TextField txtNome = new TextField();
        TextField txtPreco = new TextField();
        TextField txtSku = new TextField();

        txtId.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));
        txtPreco.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*(\\,\\d*)?") ? c : null));
        txtSku.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));

        Bindings.bindBidirectional(txtId.textProperty(), control.idProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtNome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtPreco.textProperty(), control.precoProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtSku.textProperty(), control.skuProperty(), new NumberStringConverter());

        tableView.setItems(control.buscarTodos());

        TableColumn<produto, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getIdp()))
        );

        TableColumn<produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNomep())
        );

        TableColumn<produto, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getPreco()))
        );
        TableColumn<produto, String> colSku = new TableColumn<>("Sku");
        colSku.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(String.valueOf(item.getValue().getSku()))
        );

        tableView.getColumns().addAll(colId, colNome, colPreco);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            control.fromEntity(novo);
        });

        panPrincipal.setCenter(tableView);

        panForm.add(new Label("ID: "), 0, 0);
        panForm.add(txtId, 1, 0);

        panForm.add(new Label("Nome: "), 0, 1);
        panForm.add(txtNome, 1, 1);

        panForm.add(new Label("Preço: "), 0, 2);
        panForm.add(txtPreco, 1, 2);

        panForm.add(new Label("Sku: "), 0, 3);
        panForm.add(txtSku, 1, 3);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            control.salvar();
            tableView.setItems(control.buscarTodos());
            tableView.refresh();
        });

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setOnAction(e -> {
            control.pesquisar();
            tableView.setItems(control.buscarTodos());
            tableView.refresh();
        });

        Button btnAtualizar = new Button("Atualizar");
        btnAtualizar.setOnAction(e -> {
            control.atualizar();
            tableView.setItems(control.buscarTodos());
            tableView.refresh();
        });

        Button btnDeletar = new Button("Deletar");
        btnDeletar.setOnAction(e -> {
            control.deletar();
            tableView.setItems(control.buscarTodos());
            tableView.refresh();
        });

        HBox panBotoes = new HBox(10, btnSalvar, btnPesquisar, btnAtualizar, btnDeletar);
        VBox panSuperior = new VBox(10, panForm, panBotoes);

        panPrincipal.setTop(panSuperior);

        stage.setScene(scn);
        stage.setTitle("produtos");
        stage.show();
    }
}
