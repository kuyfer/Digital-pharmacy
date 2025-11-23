// src/test/java/projet/digitalpharmacy/Services/ProductServiceTest.java
package projet.digitalpharmacy.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Respositories.ProductRepository;
import projet.digitalpharmacy.enums.ProductCategory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenCreateProduct_thenReturnSavedProduct() {
        // Given
        Product product = Product.builder()
                .nom("Aspirine")
                .prix(3.5)
                .quantiteStock(100)
                .codeCIP("3400931234593")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        when(productRepository.existsByCodeCIP(any())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product saved = productService.createProduct(product);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Aspirine");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void whenCreateProductWithExistingCodeCIP_thenThrowException() {
        // Given
        Product product = Product.builder()
                .nom("Aspirine")
                .prix(3.5)
                .quantiteStock(100)
                .codeCIP("3400931234593")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        when(productRepository.existsByCodeCIP(any())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("code CIP existe déjà");
    }

    @Test
    void whenUpdateStock_thenReturnUpdatedProduct() {
        // Given
        Long productId = 1L;
        Product existingProduct = Product.builder()
                .id(productId)
                .nom("Paracétamol")
                .prix(2.5)
                .quantiteStock(50)
                .codeCIP("3400931234594")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        Product updated = productService.updateStock(productId, 75);

        // Then
        assertThat(updated.getQuantiteStock()).isEqualTo(75);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void whenDecreaseStockWithInsufficientQuantity_thenThrowException() {
        // Given
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .nom("Ibuprofène")
                .prix(4.0)
                .quantiteStock(10)
                .codeCIP("3400931234595")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When & Then
        assertThatThrownBy(() -> productService.decreaseStock(productId, 15))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stock insuffisant");
    }
}