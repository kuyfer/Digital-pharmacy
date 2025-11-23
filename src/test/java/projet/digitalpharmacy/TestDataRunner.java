// src/test/java/projet/digitalpharmacy/TestDataRunner.java (Optionnel)
package projet.digitalpharmacy;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Respositories.ProductRepository;
import projet.digitalpharmacy.enums.ProductCategory;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Données de test
        Product p1 = Product.builder()
                .nom("Doliprane 1000mg")
                .description("Antidouleur et antipyrétique")
                .prix(5.99)
                .quantiteStock(150)
                .dateExpiration(LocalDate.now().plusMonths(24))
                .codeCIP("3400931234567")
                .prescriptionRequise(false)
                .laboratoire("Sanofi")
                .numeroLot("LOT2025001")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        Product p2 = Product.builder()
                .nom("Smecta")
                .description("Traitement des diarrhées")
                .prix(8.50)
                .quantiteStock(5) // Stock faible
                .dateExpiration(LocalDate.now().minusDays(1)) // Périmé
                .codeCIP("3400931234568")
                .prescriptionRequise(false)
                .laboratoire("Ipsen")
                .numeroLot("LOT2024001")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        productRepository.save(p1);
        productRepository.save(p2);

        System.out.println("✅ Données de test chargées avec succès!");
    }
}