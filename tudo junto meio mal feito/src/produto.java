

public class produto{
    private int idp;
    private String nomep;
    private double preco;
    private int sku;

    public produto() {} 

    public produto(int idp, String nomep, double preco, int sku ) { 
        this.idp = idp;
        this.nomep = nomep;
        this.preco = preco;
        this.sku = sku;
    }

    public int getIdp() {
        return idp;
    }
    public void setIdp(int idp) {
        this.idp = idp;
    }

    public String getNomep() {
        return nomep;
    }
    public void setNomep(String nomep) {
        this.nomep = nomep;
    }

    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

        public int getSku() {
        return sku;
    }
    public void setSku(int sku) {
        this.sku = sku;
    }
}