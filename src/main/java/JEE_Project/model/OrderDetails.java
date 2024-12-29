package JEE_Project.model;

import java.math.BigDecimal;

public class OrderDetails {

    private int id;
    private int commandeId;   // Référence à l'id de la commande
    private int produitId;    // Référence à l'id du produit
    private int quantite;     // Quantité du produit
    private BigDecimal prixTotal; // Prix total pour cette ligne (quantité * prix)
    private String produitNom;  // Nouveau champ pour le nom du produit


    // Constructeur sans arguments
    public OrderDetails() {}

    // Constructeur avec paramètres
    public OrderDetails(int id, int commandeId, int produitId, int quantite, BigDecimal prixTotal, String produitNom) {
        this.id = id;
        this.commandeId = commandeId;
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
        this.produitNom = produitNom;
    }

    // Getters et setters pour chaque attribut
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }
    public String getProduitNom() {
        return produitNom;
    }

    public void setProduitNom(String produitNom) {
        this.produitNom = produitNom;
    }
    // Méthode toString pour afficher les détails
    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", commandeId=" + commandeId + ", produitId=" + produitId + ", quantite="
                + quantite + ", prixTotal=" + prixTotal + "]";
    }
}
