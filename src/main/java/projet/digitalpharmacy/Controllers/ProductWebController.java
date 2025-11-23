package projet.digitalpharmacy.Controllers;
// src/main/java/projet/digitalpharmacy/controllers/ProductWebController.java

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projet.digitalpharmacy.Models.Product;
import projet.digitalpharmacy.Services.ProductService;
import projet.digitalpharmacy.enums.ProductCategory;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/web/produits")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> produits = productService.getAllProducts();
        model.addAttribute("produits", produits);
        model.addAttribute("categories", ProductCategory.values());
        return "products/list";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "products/view";
        } catch (RuntimeException e) {
            return "redirect:/web/produits?error=Produit non trouvé";
        }
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("isEdit", false);
        return "products/form";
    }

    @PostMapping("/nouveau")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", ProductCategory.values());
            model.addAttribute("isEdit", false);
            return "products/form";
        }

        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("success", "Produit créé avec succès!");
            return "redirect:/web/produits";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", ProductCategory.values());
            model.addAttribute("isEdit", false);
            return "products/form";
        }
    }

    @GetMapping("/{id}/modifier")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", ProductCategory.values());
            model.addAttribute("isEdit", true);
            return "products/form";
        } catch (RuntimeException e) {
            return "redirect:/web/produits?error=Produit non trouvé";
        }
    }

    @PostMapping("/{id}/modifier")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product productDetails,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", ProductCategory.values());
            model.addAttribute("isEdit", true);
            return "products/form";
        }

        try {
            productService.updateProduct(id, productDetails);
            redirectAttributes.addFlashAttribute("success", "Produit mis à jour avec succès!");
            return "redirect:/web/produits/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", ProductCategory.values());
            model.addAttribute("isEdit", true);
            return "products/form";
        }
    }

    @GetMapping("/{id}/supprimer")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Produit supprimé avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/web/produits";
    }

    // Gestion du stock
    @PostMapping("/{id}/augmenter-stock")
    public String increaseStock(@PathVariable Long id,
                                @RequestParam Integer quantite,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.increaseStock(id, quantite);
            redirectAttributes.addFlashAttribute("success",
                    "Stock augmenté de " + quantite + " unités!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/web/produits/" + id;
    }

    @PostMapping("/{id}/reduire-stock")
    public String decreaseStock(@PathVariable Long id,
                                @RequestParam Integer quantite,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.decreaseStock(id, quantite);
            redirectAttributes.addFlashAttribute("success",
                    "Stock réduit de " + quantite + " unités!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/web/produits/" + id;
    }
}