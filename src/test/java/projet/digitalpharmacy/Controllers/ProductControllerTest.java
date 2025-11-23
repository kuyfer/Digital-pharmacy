// src/test/java/projet/digitalpharmacy/Controllers/ProductControllerTest.java
package projet.digitalpharmacy.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Services.AlerteService;
import projet.digitalpharmacy.Services.ProductService;
import projet.digitalpharmacy.enums.ProductCategory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private AlerteService alerteService;

    @Test
    void whenUpdateStock_thenReturnUpdatedProduct() throws Exception {
        // Given
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .nom("Vitamine C")
                .prix(12.0)
                .quantiteStock(100)
                .codeCIP("3400931234596")
                .categorie(ProductCategory.PARAPHARMACIE)
                .build();

        Map<String, Integer> request = new HashMap<>();
        request.put("quantite", 100);

        when(productService.updateStock(anyLong(), any(Integer.class))).thenReturn(product);

        // When & Then
        mockMvc.perform(put("/api/produits/{id}/stock", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Vitamine C"))
                .andExpect(jsonPath("$.quantiteStock").value(100));
    }

    @Test
    void whenGetProduitsPerimes_thenReturnExpiredProducts() throws Exception {
        // Given
        Product perime1 = Product.builder()
                .id(1L)
                .nom("Produit Périmé 1")
                .prix(5.0)
                .quantiteStock(5)
                .codeCIP("3400931234597")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        Product perime2 = Product.builder()
                .id(2L)
                .nom("Produit Périmé 2")
                .prix(8.0)
                .quantiteStock(3)
                .codeCIP("3400931234598")
                .categorie(ProductCategory.MEDICAMENT)
                .build();

        List<Product> perimes = Arrays.asList(perime1, perime2);

        when(alerteService.getProduitsPerimes()).thenReturn(perimes);

        // When & Then
        mockMvc.perform(get("/api/produits/alertes/perimes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nom").value("Produit Périmé 1"))
                .andExpect(jsonPath("$[1].nom").value("Produit Périmé 2"));
    }

    @Test
    void whenGetStatistiquesAlertes_thenReturnStatistics() throws Exception {
        // Given
        String statistiques = "Alertes - Périmés: 2, Bientôt périmés: 3, Stock faible: 5";

        when(alerteService.getStatistiquesAlertes()).thenReturn(statistiques);

        // When & Then
        mockMvc.perform(get("/api/produits/alertes/statistiques"))
                .andExpect(status().isOk())
                .andExpect(content().string(statistiques));
    }
}