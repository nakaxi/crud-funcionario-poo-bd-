import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class caixancomanda extends Application{

    private Stage loginStage;
    private comanda comandaAtual;

    public caixancomanda() {}

    public caixancomanda(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label lbl = new Label("Informe o ID da comanda:");
        TextField txtId = new TextField();
        txtId.setPromptText("ID Comanda");

        Button btnAbrir = new Button("Abrir Comanda");
        Button btnVoltar = new Button("Voltar");

        root.getChildren().addAll(lbl, txtId, btnAbrir, btnVoltar);

        btnAbrir.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                comandaAtual = new comanda(id); // cria comanda com esse ID
                new caixaview(comandaAtual, stage).abrir();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Informe um ID válido!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        btnVoltar.setOnAction(e -> {
            stage.close();
            if (loginStage != null) loginStage.show();
        });

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Número da Comanda");
        stage.show();
    }

}