// src/main/java/projet/digitalpharmacy/Models/Product.java
package projet.digitalpharmacy.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projet.digitalpharmacy.enums.ProductCategory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    //attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private Double prix;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 0, message = "La quantité ne peut être négative")
    @Column(name = "quantite_stock")
    private Integer quantiteStock;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @NotBlank(message = "Le code CIP est obligatoire")
    @Column(name = "code_cip", unique = true, nullable = false)
    private String codeCIP;

    @Column(name = "prescription_requise")
    private Boolean prescriptionRequise = false;

    private String laboratoire;

    @Column(name = "numero_lot")
    private String numeroLot;

    @Enumerated(EnumType.STRING)
    private ProductCategory categorie;

    // Methodes metiers
    public boolean estPerime() {
        return dateExpiration != null && dateExpiration.isBefore(LocalDate.now());
    }

    public boolean estStockFaible() {
        return quantiteStock != null && quantiteStock <= 10;
    }

    public boolean estBientotPerime() {
        return dateExpiration != null &&
                ChronoUnit.DAYS.between(LocalDate.now(), dateExpiration) <= 30;
    }

    public void decreaseStock(Integer quantite) {
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        if (this.quantiteStock < quantite) {
            throw new IllegalStateException("Stock insuffisant");
        }
        this.quantiteStock -= quantite;
    }

    public void increaseStock(Integer quantite) {
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        this.quantiteStock += quantite;
    }


}
