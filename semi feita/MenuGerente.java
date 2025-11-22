import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuGerente extends Application {

    private Funcionario gerente;

    private Stage loginStage;

    public MenuGerente(Stage loginStage, Funcionario gerente) {
        this.loginStage = loginStage;
        this.gerente = gerente;
    }



    @Override
    public void start(Stage stage) {

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Button btnCaixa = new Button("Caixa");
        Button btnAtendente = new Button("Atendente");
        Button btnFuncionarios = new Button("CRUD Funcionários");

        double largura = 300;
        double altura = 50;
        btnCaixa.setPrefSize(largura, altura);
        btnAtendente.setPrefSize(largura, altura);
        btnFuncionarios.setPrefSize(largura, altura);
        Button btnVoltar = new Button("Voltar");

        btnCaixa.setOnAction(e -> {
            try {
                new caixancomanda().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnAtendente.setOnAction(e -> {
            try {
                new balcaoncomanda().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnFuncionarios.setOnAction(e -> {
            try {
                new funview().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnVoltar.setOnAction(e -> {
            stage.close();         
            loginStage.show();       
        });

        root.getChildren().addAll(btnCaixa, btnAtendente, btnFuncionarios, btnVoltar);

        Scene scene = new Scene(root, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Menu Gerente");
        stage.show();
    }
}
