import java.util.ArrayList;
import java.util.List;

public class comanda{
    
    private int idComanda;
    private int num;
    private double valortotal;
    private int status;
    private List<itencomanda> itens = new ArrayList<>();

    public comanda(int idComanda, int num, int status, double valorInicial) {
    this.idComanda = idComanda;
    this.num = num;
    this.status = status;
    this.valortotal = valorInicial;
    
}
    public comanda(int num) {
    this.num = num;
    this.status = 1;
    }

    public int getIdComanda() {
    return idComanda;
    }      

    public int getNum() {
        return num;
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