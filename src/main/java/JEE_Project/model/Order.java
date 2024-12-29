package JEE_Project.model; 

import java.util.Date;

public class Order extends Product{
    private int orderId; // Correspond à id
    private int utilisateurId; // Correspond à utilisateur_id
    private Date date; // Correspond à date
    private String status; // Correspond à status ('en cours' ou 'livré')

    // Constructeur par défaut
    public Order() {
    }

	public Order(int orderId, int utilisateurId, Date date, String status) {
		super();
		this.orderId = orderId;
		this.utilisateurId = utilisateurId;
		this.date = date;
		this.status = status;
	}

	public Order(int utilisateurId, Date date, String status) {
		super();
		this.utilisateurId = utilisateurId;
		this.date = date;
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getUtilisateurId() {
		return utilisateurId;
	}

	public void setUtilisateurId(int utilisateurId) {
		this.utilisateurId = utilisateurId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", utilisateurId=" + utilisateurId + ", date=" + date + ", status="
				+ status + "]";
	}

}
