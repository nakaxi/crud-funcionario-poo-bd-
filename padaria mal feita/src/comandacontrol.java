import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class comandacontrol {

    public static comanda buscarPorNumero(int numero) {
        comanda c = null;
        String sqlComanda = "SELECT * FROM Comanda WHERE numeroComanda = ?";
        String sqlItens = "SELECT p.idProduto, p.nomeProduto, p.valorProduto, p.SKUProduto, ip.qtdUnidade " +
                          "FROM ItemPedido ip " +
                          "JOIN Produto p ON ip.idProdutoFK = p.idProduto " +
                          "JOIN Comanda c ON ip.idComandaFK = c.idComanda " +
                          "WHERE c.numeroComanda = ?";

        try (Connection con = conexao.get();
             PreparedStatement psComanda = con.prepareStatement(sqlComanda);
             PreparedStatement psItens = con.prepareStatement(sqlItens)) {

            psComanda.setInt(1, numero);
            ResultSet rsComanda = psComanda.executeQuery();

            if (rsComanda.next()) {
                c = new comanda(
                    rsComanda.getInt("idComanda"),
                    rsComanda.getInt("numeroComanda"),
                    rsComanda.getInt("statusComanda"),
                    rsComanda.getDouble("valorComanda")
                );

                // buscar itens
                psItens.setInt(1, numero);
                ResultSet rsItens = psItens.executeQuery();
                while (rsItens.next()) {
                    produto p = new produto();
                    p.setIdp(rsItens.getInt("idProduto"));
                    p.setNomep(rsItens.getString("nomeProduto"));
                    p.setPreco(rsItens.getDouble("valorProduto"));
                    p.setSku(rsItens.getInt("SKUProduto"));

                    int qtd = rsItens.getInt("qtdUnidade");
                    c.adicionarProduto(p, qtd);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c; 
    }

    public List<itencomanda> buscarItensDaComanda(int idComanda) {
        String sql = """
            SELECT p.idProduto, p.nomeProduto, p.SKUProduto, p.valorProduto, i.qtdUnidade
            FROM ItemPedido i
            JOIN Produto p ON p.idProduto = i.idProdutoFK
            WHERE i.idComandaFK = ?
        """;

        List<itencomanda> lista = new ArrayList<>();

        try (Connection con = conexao.get();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idComanda);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                produto p = new produto(
                    rs.getInt("idProduto"),
                    rs.getString("nomeProduto"),
                    rs.getDouble("valorProduto"),
                    rs.getInt("SKUProduto")
                );

                int qtd = rs.getInt("qtdUnidade");

                lista.add(new itencomanda(p, qtd));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void salvarItem(int idComanda, produto p, int quantidade) {

        String sqlExiste = "SELECT qtdUnidade FROM ItemPedido WHERE idProdutoFK = ? AND idComandaFK = ?";
        String sqlInsert = "INSERT INTO ItemPedido(idProdutoFK, idComandaFK, dataCompra, subTotal, qtdUnidade) VALUES(?, ?, DATEADD(SECOND,-1,GETDATE()), ?, ?)";
        String sqlUpdate = "UPDATE ItemPedido SET qtdUnidade = ?, subTotal = ? WHERE idProdutoFK = ? AND idComandaFK = ?";

        try (Connection con = conexao.get()) {

            PreparedStatement ps1 = con.prepareStatement(sqlExiste);
            ps1.setInt(1, p.getIdp());
            ps1.setInt(2, idComanda);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                // já existe → atualizar quantidade
                int atual = rs.getInt("qtdUnidade");
                int nova = atual + quantidade;

                PreparedStatement ps2 = con.prepareStatement(sqlUpdate);
                ps2.setInt(1, nova);
                ps2.setDouble(2, nova * p.getPreco());
                ps2.setInt(3, p.getIdp());
                ps2.setInt(4, idComanda);
                ps2.executeUpdate();

            } else {
                // não existe → inserir item novo
                PreparedStatement ps2 = con.prepareStatement(sqlInsert);
                ps2.setInt(1, p.getIdp());
                ps2.setInt(2, idComanda);
                ps2.setDouble(3, p.getPreco() * quantidade);
                ps2.setInt(4, quantidade);
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerItem(int idComanda, produto p, int quantidade) {

        String sqlSelect = "SELECT qtdUnidade FROM ItemPedido WHERE idProdutoFK = ? AND idComandaFK = ?";
        String sqlDelete = "DELETE FROM ItemPedido WHERE idProdutoFK = ? AND idComandaFK = ?";
        String sqlUpdate = "UPDATE ItemPedido SET qtdUnidade = ?, subTotal = ? WHERE idProdutoFK = ? AND idComandaFK = ?";

        try (Connection con = conexao.get()) {

            PreparedStatement ps1 = con.prepareStatement(sqlSelect);
            ps1.setInt(1, p.getIdp());
            ps1.setInt(2, idComanda);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) return;

            int atual = rs.getInt("qtdUnidade");
            int nova = atual - quantidade;

            if (nova <= 0) {
                PreparedStatement ps2 = con.prepareStatement(sqlDelete);
                ps2.setInt(1, p.getIdp());
                ps2.setInt(2, idComanda);
                ps2.executeUpdate();
            } else {
                PreparedStatement ps2 = con.prepareStatement(sqlUpdate);
                ps2.setInt(1, nova);
                ps2.setDouble(2, nova * p.getPreco());
                ps2.setInt(3, p.getIdp());
                ps2.setInt(4, idComanda);
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void zerarComanda(int idComanda) {
       String sqlDeleteItens = "DELETE FROM ItemPedido WHERE idComandaFK = ?";
    String sqlZeraValor = "UPDATE Comanda SET valorComanda = 0 WHERE idComanda = ?";

    try (Connection con = conexao.get()) {

        PreparedStatement ps1 = con.prepareStatement(sqlDeleteItens);
        ps1.setInt(1, idComanda);
        ps1.executeUpdate();

        PreparedStatement ps2 = con.prepareStatement(sqlZeraValor);
        ps2.setInt(1, idComanda);
        ps2.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
