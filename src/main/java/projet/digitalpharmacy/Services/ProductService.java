// src/main/java/projet/digitalpharmacy/Services/ProductService.java
package projet.digitalpharmacy.Services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Respositories.ProductRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    public Product createProduct(Product product) {
        log.info("Création d'un nouveau produit: {}", product.getNom());

        // Validation du code CIP unique
        if (productRepository.existsByCodeCIP(product.getCodeCIP())) {
            throw new IllegalArgumentException("Un produit avec ce code CIP existe déjà");
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + id));

        // Vérification unicité code CIP (sauf pour le produit actuel)
        if (!product.getCodeCIP().equals(productDetails.getCodeCIP()) &&
                productRepository.existsByCodeCIP(productDetails.getCodeCIP())) {
            throw new IllegalArgumentException("Un autre produit utilise déjà ce code CIP");
        }

        product.setNom(productDetails.getNom());
        product.setDescription(productDetails.getDescription());
        product.setPrix(productDetails.getPrix());
        product.setQuantiteStock(productDetails.getQuantiteStock());
        product.setDateExpiration(productDetails.getDateExpiration());
        product.setCodeCIP(productDetails.getCodeCIP());
        product.setPrescriptionRequise(productDetails.getPrescriptionRequise());
        product.setLaboratoire(productDetails.getLaboratoire());
        product.setNumeroLot(productDetails.getNumeroLot());
        product.setCategorie(productDetails.getCategorie());

        log.info("Mise à jour du produit: {}", product.getNom());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + id));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + id));

        log.info("Suppression du produit: {}", product.getNom());
        productRepository.delete(product);
    }

    // Gestion spécifique du stock
    public Product updateStock(Long id, Integer nouvelleQuantite) {
        Product product = getProductById(id);

        if (nouvelleQuantite < 0) {
            throw new IllegalArgumentException("La quantité ne peut être négative");
        }

        product.setQuantiteStock(nouvelleQuantite);
        log.info("Mise à jour stock produit {}: {} unités", product.getNom(), nouvelleQuantite);

        return productRepository.save(product);
    }

    public Product increaseStock(Long id, Integer quantite) {
        Product product = getProductById(id);
        product.increaseStock(quantite);
        log.info("Augmentation stock produit {}: +{} unités", product.getNom(), quantite);
        return productRepository.save(product);
    }

    public Product decreaseStock(Long id, Integer quantite) {
        Product product = getProductById(id);
        product.decreaseStock(quantite);
        log.info("Réduction stock produit {}: -{} unités", product.getNom(), quantite);
        return productRepository.save(product);
    }
}
