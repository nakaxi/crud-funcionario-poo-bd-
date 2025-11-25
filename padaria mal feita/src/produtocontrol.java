import java.sql.Connection;
import java.sql.PreparedStatement;
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

public class produtocontrol {

    private ObservableList<produto> lista = FXCollections.observableArrayList();

    private IntegerProperty id = new SimpleIntegerProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private DoubleProperty preco = new SimpleDoubleProperty(0.0);
    private IntegerProperty sku = new SimpleIntegerProperty(0);

    public produto toEntity() {
        produto p = new produto();
        p.setIdp(id.get());
        p.setNomep(nome.get());
        p.setPreco(preco.get());
        p.setSku(sku.get());
        return p;
    }

    public void fromEntity(produto p) {
        if (p != null) {
            id.set(p.getIdp());
            nome.set(p.getNomep());
            preco.set(p.getPreco());
            sku.set(p.getSku());
        }
    }

    public void salvar() {
        try (Connection con = conexao.get()) {
            String sql = "INSERT INTO Produto (nomeProduto, valorProduto, SKUProduto ) VALUES (?, ?, ? )";
            var ps = con.prepareStatement(sql);
            ps.setString(1, nome.get());
            ps.setDouble(2, preco.get());
            ps.setInt(3, sku.get());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    public void pesquisar() {
        int idpro = id.get();
        String sql = "SELECT * FROM produto WHERE idProduto = ?";

        try (Connection con = conexao.get();
            var ps = con.prepareStatement(sql)) {

            ps.setInt(1, idpro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    produto f = new produto();
                    f.setIdp(rs.getInt("idProduto"));
                    f.setNomep(rs.getString("nomeProduto"));
                    f.setPreco(rs.getDouble("valorProduto"));
                    f.setSku(rs.getInt("SKUProduto"));

                    fromEntity(f); // Atualiza os campos do formulário
                } else {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setTitle("Pesquisar");
                    alerta.setHeaderText("Produto não encontrado");
                    alerta.showAndWait();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText("Erro ao pesquisar produto");
            erro.setContentText(e.getMessage());
            erro.showAndWait();
        }   
    }

    public void atualizar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar atualização");
        alerta.setHeaderText("Deseja realmente atualizar este produto?");
        alerta.setContentText("Essa ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String sql = "UPDATE Produto SET nomeProduto = ?, valorProduto = ?, SKUProduto = ? WHERE idProduto = ?";

            try (Connection con = conexao.get();
                var ps = con.prepareStatement(sql)) {

                ps.setString(1, nome.get());
                ps.setDouble(2, preco.get());
                ps.setInt(3, sku.get()); 
                ps.setInt(4, id.get());

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setTitle("Atualizar");
                    ok.setHeaderText("Produto atualizado com sucesso");
                    ok.showAndWait();

                    // Atualiza a lista local
                    lista.removeIf(p -> p.getIdp() == id.get());
                    lista.add(toEntity()); // toEntity() retorna um objeto Produto atualizado
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText("Produto não encontrado no banco");
                    erro.showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText("Erro ao atualizar produto");
                erro.setContentText(e.getMessage());
                erro.showAndWait();
            }
        }
    }
    

    public void deletar() {
        int idpro = id.get();

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar deletamento");
        alerta.setHeaderText("Você deseja realmente deletar este produto?");
        alerta.setContentText("Essa ação não pode ser desfeita.");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection con = conexao.get()) {
                String sql = "DELETE FROM produto WHERE idProduto = ?";
                var ps = con.prepareStatement(sql);
                ps.setInt(1, idpro);
                int linhasAfetadas = ps.executeUpdate();
                   
                if (linhasAfetadas > 0) {
                    System.out.println("Produto deletado do BD");
                    lista.removeIf(f -> f.getIdp() == idpro);

                    id.set(0);
                    nome.set("");
                    preco.set(0.0);
                    sku.set(0);
                } else {
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro");
                    erro.setHeaderText("Não foi possível deletar o produto do banco");
                    erro.showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText("Erro ao deletar produto");
                erro.setContentText(e.getMessage());
                erro.showAndWait();
            }
        }
    }

    private boolean idExiste(int idProcurado) {
        for (produto p : lista) {
            if (p.getIdp() == idProcurado) {
                return true;
            }
        }
        return false;
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public DoubleProperty precoProperty() { return preco; }
    public IntegerProperty skuProperty() { return sku; }

    public ObservableList<produto> buscarTodos() {
        ObservableList<produto> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM produto";

        try (Connection conn = conexao.get();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produto f = new produto();
                f.setIdp(rs.getInt("idProduto"));
                f.setNomep(rs.getString("nomeProduto"));
                f.setPreco(rs.getDouble("valorProduto"));
                f.setSku(rs.getInt("SKUProduto"));

                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    //puxar para caixa/atendente
    public produto buscarPorId(int id) {
        produto p = null;

        String sql = "SELECT idProduto, nomeProduto, SKUProduto, valorProduto "
                   + "FROM Produto WHERE idProduto = ?";

        try (Connection con = conexao.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new produto(
                    rs.getInt("idProduto"),
                    rs.getString("nomeProduto"),
                    rs.getDouble("valorProduto"),
                    rs.getInt("SKUProduto")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
}