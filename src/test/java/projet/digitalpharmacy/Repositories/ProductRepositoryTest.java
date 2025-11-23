// src/test/java/projet/digitalpharmacy/Respositories/ProductRepositoryTest.java
package projet.digitalpharmacy.Repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Respositories.ProductRepository;
import projet.digitalpharmacy.enums.ProductCategory;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenFindByNom_thenReturnProducts() {
        // Given
        Product product = Product.builder()
                .nom("Doliprane 1000mg")
                .prix(5.99)
                .quantiteStock(50)
                .codeCIP("3400931234567")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        entityManager.persist(product);
        entityManager.flush();

        // When
        List<Product> found = productRepository.findByNomContainingIgnoreCase("doliprane");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getNom()).isEqualTo(product.getNom());
    }

    @Test
    void whenFindProduitsPerimes_thenReturnExpiredProducts() {
        // Given
        Product perime = Product.builder()
                .nom("Produit Périmé")
                .prix(10.0)
                .quantiteStock(5)
                .codeCIP("3400931234589")
                .dateExpiration(LocalDate.now().minusDays(1))
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        Product nonPerime = Product.builder()
                .nom("Produit Valide")
                .prix(15.0)
                .quantiteStock(20)
                .codeCIP("3400931234590")
                .dateExpiration(LocalDate.now().plusDays(30))
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        entityManager.persist(perime);
        entityManager.persist(nonPerime);
        entityManager.flush();

        // When
        List<Product> perimes = productRepository.findProduitsPerimes();

        // Then
        assertThat(perimes).hasSize(1);
        assertThat(perimes.get(0).getNom()).isEqualTo("Produit Périmé");
    }

    @Test
    void whenFindByStockFaible_thenReturnLowStockProducts() {
        // Given
        Product stockFaible = Product.builder()
                .nom("Produit Stock Faible")
                .prix(8.0)
                .quantiteStock(5)
                .codeCIP("3400931234591")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        Product stockNormal = Product.builder()
                .nom("Produit Stock Normal")
                .prix(12.0)
                .quantiteStock(25)
                .codeCIP("3400931234592")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        entityManager.persist(stockFaible);
        entityManager.persist(stockNormal);
        entityManager.flush();

        // When
        List<Product> stockFaibleList = productRepository.findByQuantiteStockLessThanEqual(10);

        // Then
        assertThat(stockFaibleList).hasSize(1);
        assertThat(stockFaibleList.get(0).getNom()).isEqualTo("Produit Stock Faible");
    }
}