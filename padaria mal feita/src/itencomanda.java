

public class itencomanda{
    private produto produto;
    private int quantidade;

    public itencomanda(produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoTotal() {
        return produto.getPreco() * quantidade;
    }
}