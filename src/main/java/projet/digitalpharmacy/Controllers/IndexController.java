// src/main/java/projet/digitalpharmacy/controllers/IndexController.java
package projet.digitalpharmacy.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projet.digitalpharmacy.Services.AlerteService;
import projet.digitalpharmacy.Services.ProductService;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ProductService productService;
    private final AlerteService alerteService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            long totalProducts = productService.getAllProducts().size();
            String statsAlertes = alerteService.getStatistiquesAlertes();

            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("statsAlertes", statsAlertes);
            model.addAttribute("alertes", alerteService.getToutesAlertes());

            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des données: " + e.getMessage());
            return "index";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            model.addAttribute("produitsPerimes", alerteService.getProduitsPerimes());
            model.addAttribute("produitsBientotPerimes", alerteService.getProduitsBientotPerimes());
            model.addAttribute("produitsStockFaible", alerteService.getProduitsStockFaible());

            return "alertes/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            return "alertes/dashboard";
        }
    }
}