import java.util.ArrayList;
import java.util.List;

public class comanda{
    
    private int id;
    private double valortotal;
    private int status; // 0 = aberta, 1 = paga, 2 = cancelada
    private List<itencomanda> itens = new ArrayList<>();

    public comanda(int id) {
        this.id = id;
        this.status = 0; 
    }

    public int getId() {
        return id;
    }

    public double getValortotal() {
        atualizarValortotal();
        return valortotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<itencomanda> getItens() {
        return itens;
    }

    public void adicionarProduto(produto p, int quantidade) {
        for (itencomanda item : itens) {
            if (item.getProduto().getIdp() == p.getIdp()) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                atualizarValortotal();
                return;
            }
        }
        itens.add(new itencomanda(p, quantidade));
        atualizarValortotal();
    }

    public void removerProduto(produto p, int quantidade) {
        itencomanda alvo = null;
        for (itencomanda item : itens) {
            if (item.getProduto().getIdp() == p.getIdp()) {
                int novaQtd = item.getQuantidade() - quantidade;
                if (novaQtd > 0) {
                    item.setQuantidade(novaQtd);
                } else {
                    alvo = item;
                }
                break;
            }
        }
        if (alvo != null) itens.remove(alvo);
        atualizarValortotal();
    }

    private void atualizarValortotal() {
        valortotal = 0;
        for (itencomanda item : itens) {
            valortotal += item.getPrecoTotal();
        }
    }

    public void zerarComanda() {
        itens.clear();
        valortotal = 0;
        status = 0;
    }
}