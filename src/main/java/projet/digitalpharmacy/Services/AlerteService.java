// src/main/java/projet/digitalpharmacy/Services/AlerteService.java
package projet.digitalpharmacy.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Respositories.ProductRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlerteService {

    private final ProductRepository productRepository;

    public List<Product> getProduitsPerimes() {
        log.info("Récupération des produits périmés");
        return productRepository.findProduitsPerimes();
    }

    public List<Product> getProduitsBientotPerimes() {
        LocalDate dateLimite = LocalDate.now().plusDays(30);
        log.info("Récupération des produits bientôt périmés (avant {})", dateLimite);
        return productRepository.findProduitsBientotPerimes(dateLimite);
    }

    public List<Product> getProduitsStockFaible() {
        Integer seuil = 10; // Seuil configurable
        log.info("Récupération des produits avec stock faible (seuil: {})", seuil);
        return productRepository.findByQuantiteStockLessThanEqual(seuil);
    }

    public List<Product> getToutesAlertes() {
        List<Product> alertes = getProduitsPerimes();
        alertes.addAll(getProduitsBientotPerimes());
        alertes.addAll(getProduitsStockFaible());

        log.info("Récupération de toutes les alertes: {} produits concernés", alertes.size());
        return alertes;
    }

    public String getStatistiquesAlertes() {
        int perimes = getProduitsPerimes().size();
        int bientotPerimes = getProduitsBientotPerimes().size();
        int stockFaible = getProduitsStockFaible().size();

        return String.format(
                "Alertes - Périmés: %d, Bientôt périmés: %d, Stock faible: %d",
                perimes, bientotPerimes, stockFaible
        );
    }
}