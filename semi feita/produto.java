

public class produto{
    private int idp;
    private String nomep;
    private double preco;

    public produto() {} 

    public produto(int idp, String nomep, double preco) { 
        this.idp = idp;
        this.nomep = nomep;
        this.preco = preco;
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
}