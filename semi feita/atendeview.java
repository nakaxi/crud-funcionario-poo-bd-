import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class atendeview {

    private comanda comandaAtual; 
    private Stage loginStage;
    public atendeview(comanda comandaAtual, Stage loginStage) {
        this.comandaAtual = comandaAtual;
        this.loginStage = loginStage;
    }

    private TableView<itencomanda> tableView = new TableView<>();
    private Label lblTotal = new Label("Valor total: R$ 0.0");

    private javafx.collections.ObservableList<itencomanda> itensObservable;

    // produtos chumbados tirar dps
    private List<produto> produtos = new ArrayList<>();

    public void abrir() {

        Stage stage = new Stage();
        
        //aparentemente isso permite autalizar a tabela qnd um iten é mudado
        itensObservable = FXCollections.observableArrayList(comandaAtual.getItens());
        tableView.setItems(itensObservable);

        // produtos chumbado pra remover dps
        produtos.add(new produto(1, "Café", 5.0));
        produtos.add(new produto(2, "Pão", 2.5));
        produtos.add(new produto(3, "Suco", 7.0));

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // area pra adicionar produto
        HBox addBox = new HBox(5);
        TextField txtId = new TextField();
        txtId.setPromptText("ID Produto");
        TextField txtQnt = new TextField();
        txtQnt.setPromptText("Quantidade");
        Button btnAdd = new Button("Adicionar");
        addBox.getChildren().addAll(txtId, txtQnt, btnAdd);

        // tabela
        TableColumn<itencomanda, String> colNome = new TableColumn<>("Produto");
        colNome.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleStringProperty(cell.getValue().getProduto().getNomep())
        );
        colNome.setPrefWidth(150);

        TableColumn<itencomanda, Integer> colQnt = new TableColumn<>("Qtd");
        colQnt.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getQuantidade()).asObject()
        );
        colQnt.setPrefWidth(50);

        TableColumn<itencomanda, Double> colPreco = new TableColumn<>("Preço Total");
        colPreco.setCellValueFactory(cell -> 
            new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getPrecoTotal()).asObject()
        );
        colPreco.setPrefWidth(100);

        TableColumn<itencomanda, Void> colRemover = new TableColumn<>("Remover");
        colRemover.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("X");
            {
                btn.setOnAction(e -> {
                    itencomanda item = getTableView().getItems().get(getIndex());
                    comandaAtual.removerProduto(item.getProduto(), 1);
                    atualizarTabela();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        });
        colRemover.setPrefWidth(70);

        tableView.getColumns().addAll(colNome, colQnt, colPreco, colRemover);

        btnAdd.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                int qnt = Integer.parseInt(txtQnt.getText());
                produto p = produtos.stream().filter(pr -> pr.getIdp() == id).findFirst().orElse(null);
                if (p != null) {
                    comandaAtual.adicionarProduto(p, qnt);
                    atualizarTabela();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Produto não encontrado!", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Informe ID e quantidade válidos!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button btnZerar = new Button("Zerar Comanda");
        btnZerar.setOnAction(e -> {
            comandaAtual.zerarComanda();
            atualizarTabela();
        });

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> {
            stage.close();
            if (loginStage != null) loginStage.show();
        });
        HBox hboxVoltar = new HBox(btnVoltar);
        hboxVoltar.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(addBox, tableView, lblTotal, btnZerar, btnVoltar, hboxVoltar);

        Scene scene = new Scene(root, 450, 400);
        stage.setScene(scene);
        stage.setTitle("Atendente");
        stage.show();
    }

    private void atualizarTabela() {
        itensObservable.setAll(comandaAtual.getItens()); // atualiza o conteúdo da lista ligada
        lblTotal.setText("Valor total: R$ " + comandaAtual.getValortotal());
    }
}
