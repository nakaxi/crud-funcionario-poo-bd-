import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class balcaoncomanda extends Application{
    
    private Stage loginStage; // opcional para voltar
    private comanda comandaAtual;

    public balcaoncomanda() {}

    public balcaoncomanda(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label lbl = new Label("Informe o numero da comanda:");
        TextField txtNum = new TextField();
        txtNum.setPromptText("Numero Comanda");

        Button btnAbrir = new Button("Abrir Comanda");
        Button btnVoltar = new Button("Voltar");

        root.getChildren().addAll(lbl, txtNum, btnAbrir, btnVoltar);

        btnAbrir.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(txtNum.getText());
                comanda c = comandacontrol.buscarPorNumero(numero);
                if (c != null) {
                    comandaAtual = c;
                    new atendeview(comandaAtual, stage).abrir();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Comanda não encontrada!", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Informe um numero válido!", ButtonType.OK);
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