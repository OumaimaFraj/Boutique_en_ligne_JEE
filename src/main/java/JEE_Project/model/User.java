package JEE_Project.model;
//**************USER MODEL*********************************
public class User {
  // Attributs correspondant aux colonnes de la table 'utilisateur'
  private int id;
  private String nom;
  private String email;
  private String motDePasse;
  private String role;

  // Constructeur sans arguments
  public User() {
  }

  // Constructeur avec tous les arguments
  public User(int id, String nom, String email, String motDePasse, String role) {
      this.id = id;
      this.nom = nom;
      this.email = email;
      this.motDePasse = motDePasse;
      this.role = role;
  }

  // Getters et setters pour chaque attribut
  public int getId() {
      return id;
  }

  public void setId(int id) {
      this.id = id;
  }

  public String getNom() {
      return nom;
  }

  public void setNom(String nom) {
      this.nom = nom;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public String getMotDePasse() {
      return motDePasse;
  }

  public void setMotDePasse(String motDePasse) {
      this.motDePasse = motDePasse;
  }

  public String getRole() {
      return role;
  }

  public void setRole(String role) {
      this.role = role;
  }

  // Méthode toString pour faciliter le débogage
  @Override
  public String toString() {
      return "User [id=" + id + ", nom=" + nom + ", email=" + email + 
             ", motDePasse=" + motDePasse + ", role=" + role + "]";
  }
}
