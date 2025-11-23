// src/main/java/projet/digitalpharmacy/Respositories/ProductRepository.java
package projet.digitalpharmacy.Respositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.enums.ProductCategory;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "produits")
public interface ProductRepository extends JpaRepository<Product, Long> {
    //Recherche automatique exposée via REST
    @RestResource(path = "byNom")
    List<Product> findByNomContainingIgnoreCase(@Param("nom") String nom);

    @RestResource(path = "byCategorie")
    List<Product> findByCategorie(@Param("categorie") ProductCategory categorie);

    @RestResource(path = "perime")
    @Query("SELECT p FROM Product p WHERE p.dateExpiration < CURRENT_DATE  ")
    List<Product> findProduitsPerimes();

    //Produits bientôt périmés avec date limite
    @RestResource(path = "bientotPerimes")
    @Query("SELECT p FROM Product p WHERE p.dateExpiration BETWEEN CURRENT_DATE AND :dateLimite")
    List<Product> findProduitsBientotPerimes(@Param("dateLimite") LocalDate dateLimite);

    // Produits stock faible
    @RestResource(path = "stockFaible")
    List<Product> findByQuantiteStockLessThanEqual(@Param("seuil") Integer seuil);

    // Recherche avancée avec pagination
    Page<Product> findByPrixBetween(Double prixMin, Double prixMax, Pageable pageable);

    // Vérification existence avec code CIP
    boolean existsByCodeCIP(String codeCIP);
}
