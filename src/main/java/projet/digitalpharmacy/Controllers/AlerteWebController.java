package projet.digitalpharmacy.Controllers;

// src/main/java/projet/digitalpharmacy/controllers/AlerteWebController.java


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import projet.digitalpharmacy.Services.AlerteService;
import projet.digitalpharmacy.Services.ProductService;

@Controller
@RequestMapping("/web/alertes")
@RequiredArgsConstructor
public class AlerteWebController {

    private final AlerteService alerteService;
    private final ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("produitsPerimes", alerteService.getProduitsPerimes());
        model.addAttribute("produitsBientotPerimes", alerteService.getProduitsBientotPerimes());
        model.addAttribute("produitsStockFaible", alerteService.getProduitsStockFaible());
        return "alertes/dashboard";
    }

    @GetMapping("/details/{id}")
    public String detailsAlerte(@PathVariable Long id, Model model) {
        var product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "alertes/details";
    }
}