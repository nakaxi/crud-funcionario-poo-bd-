
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class login extends Application{
    
   private funcontrol control = new funcontrol();

    @Override
    public void start(Stage stage) {
        TextField txtUsuario = new TextField();
        PasswordField txtSenha = new PasswordField();

        Button btn = new Button("Entrar");
        btn.setOnAction(e -> validarLogin(stage, txtUsuario.getText(), txtSenha.getText()));

        VBox v = new VBox(10, new Label("Usuário"), txtUsuario, new Label("Senha"), txtSenha, btn);

        v.setPadding(new Insets(20));

        stage.setScene(new Scene(v, 250, 220));
        stage.setTitle("Login");
        stage.show();
    }

    private void validarLogin(Stage stage, String usuario, String senha) {

        for (Funcionario f : control.getLista()) {
            if (f.getLogin().equals(usuario) && f.getSenha().equals(senha)) {

                System.out.println("Login OK: " + f.getNome());
                
                try {
                    switch (f.getFuncao()) {
                        case 1:
                            MenuGerente menu = new MenuGerente(stage, f); 
                            menu.start(new Stage());
                            stage.hide();
                            break;

                        case 2:
                            caixancomanda caixa = new caixancomanda(stage);
                            caixa.start(new Stage());
                            stage.hide();
                            break;

                        case 3:
                            balcaoncomanda atende = new balcaoncomanda(stage);
                            atende.start(new Stage());
                            stage.hide();
                            break;

                        default:
                            System.out.println("Função desconhecida!");
                            break;
                }

                // Fecha a tela de login
                stage.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }   

            return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.ERROR, "Login inválido!", ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        Application.launch(login.class, args);
    }

}