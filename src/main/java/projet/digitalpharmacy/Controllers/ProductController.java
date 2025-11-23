package projet.digitalpharmacy.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Services.AlerteService;
import projet.digitalpharmacy.Services.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final AlerteService alerteService;

    //Endpoints custom for stock management
    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long id,
            @RequestBody Map<String,Integer> request){
        Integer quantity = request.get("quantity");
        Product product = productService.updateStock(id, quantity);
        return ResponseEntity.ok(product);

    }

    @PostMapping("/{id}/augmenter-stock")
    public ResponseEntity<Product> increaseStock(
            @PathVariable Long id,
            @RequestBody Map<String,Integer> request){
        Integer quantity = request.get("quantity");
        Product product = productService.increaseStock(id, quantity);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{id}/reduire-stock")
    public ResponseEntity<Product> decreaseStock(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {

        Integer quantite = request.get("quantite");
        Product product = productService.decreaseStock(id, quantite);
        return ResponseEntity.ok(product);
    }

    //Endpoints For alerts
    @GetMapping("/alertes/perimes")
    public ResponseEntity<List<Product>> getProduitsPerimes()
    {
        return ResponseEntity.ok(alerteService.getProduitsPerimes());
    }

    @GetMapping("/alertes/bientot-perimes")
    public ResponseEntity<List<Product>> getProduitsBientotPerimes()
    {
        return ResponseEntity.ok(alerteService.getProduitsBientotPerimes());
    }

    @GetMapping("/alertes/stock-faible")
    public ResponseEntity<List<Product>> getProduitsStockFaible() {
        return ResponseEntity.ok(alerteService.getProduitsStockFaible());
    }

    @GetMapping("/alertes/toutes")
    public ResponseEntity<List<Product>> getToutesAlertes()
    {
        return ResponseEntity.ok(alerteService.getToutesAlertes());
    }

    @GetMapping("/alertes/statistiques")
    public ResponseEntity<String> getStatistiquesAlertes() {
        return ResponseEntity.ok(alerteService.getStatistiquesAlertes());
    }

}
