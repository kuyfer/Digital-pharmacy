// src/main/resources/static/js/app.js

// Fonctions pour le tableau de bord des alertes
document.addEventListener('DOMContentLoaded', function() {
    console.log('Digital Pharmacy - Tableau de Bord initialisé');

    // Initialiser les tooltips Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Auto-refresh des données toutes les 30 secondes
    setInterval(refreshData, 30000);

    // Ajouter des animations aux cartes
    animateCards();
});

// Basculer la visibilité des sections
function toggleSection(sectionId) {
    const section = document.getElementById(`section-${sectionId}`);
    const button = event.currentTarget;
    const icon = button.querySelector('i');

    if (section.style.display === 'none') {
        section.style.display = 'block';
        icon.className = 'fas fa-chevron-down';
        button.classList.remove('collapsed');
    } else {
        section.style.display = 'none';
        icon.className = 'fas fa-chevron-right';
        button.classList.add('collapsed');
    }
}

// Actualiser les données
function refreshData() {
    console.log('Actualisation des données...');

    // Afficher un indicateur de chargement
    const refreshBtn = event?.currentTarget;
    if (refreshBtn) {
        const originalHTML = refreshBtn.innerHTML;
        refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Actualisation...';
        refreshBtn.disabled = true;

        setTimeout(() => {
            refreshBtn.innerHTML = originalHTML;
            refreshBtn.disabled = false;
        }, 2000);
    }

    // Simuler un rechargement (dans une vraie app, on ferait un appel AJAX)
    setTimeout(() => {
        showNotification('Données actualisées avec succès', 'success');
        addPulseAnimation();
    }, 1000);
}

// Animation des cartes
function animateCards() {
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('fade-in');
    });
}

// Ajouter une animation de pulsation aux statistiques critiques
function addPulseAnimation() {
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach(card => {
        const number = parseInt(card.querySelector('.stat-number').textContent);
        if (number > 0) {
            card.classList.add('pulse');
        } else {
            card.classList.remove('pulse');
        }
    });
}

// Afficher une notification
function showNotification(message, type = 'info') {
    // Créer l'élément de notification
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.cssText = `
        top: 20px;
        right: 20px;
        z-index: 9999;
        min-width: 300px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    `;

    notification.innerHTML = `
        <i class="fas fa-${getIconForType(type)} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(notification);

    // Auto-supprimer après 5 secondes
    setTimeout(() => {
        if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
        }
    }, 5000);
}

// Obtenir l'icône selon le type de message
function getIconForType(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'warning': 'exclamation-circle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

// Filtrer les produits dans les tableaux
function filterProducts(inputId, tableSelector) {
    const input = document.getElementById(inputId);
    const filter = input.value.toLowerCase();
    const table = document.querySelector(tableSelector);
    const rows = table.getElementsByTagName('tr');

    for (let i = 1; i < rows.length; i++) {
        const row = rows[i];
        const text = row.textContent.toLowerCase();
        if (text.indexOf(filter) > -1) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    }
}

// Exporter les données d'alerte
function exportAlertData(format = 'csv') {
    console.log(`Export des données d'alerte en format ${format}`);

    // Simuler l'export (dans une vraie app, on ferait un appel API)
    showNotification(`Export ${format} en cours...`, 'info');

    setTimeout(() => {
        showNotification('Export terminé avec succès!', 'success');

        // Simuler le téléchargement
        if (format === 'csv') {
            const blob = new Blob(['Simulated CSV Data'], { type: 'text/csv' });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'alertes_pharmacy.csv';
            a.click();
            window.URL.revokeObjectURL(url);
        }
    }, 1500);
}

// Mode sombre/clair (fonctionnalité avancée)
function toggleTheme() {
    const body = document.body;
    const currentTheme = body.getAttribute('data-theme') || 'light';
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';

    body.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);

    showNotification(`Thème ${newTheme} activé`, 'info');
}

// Charger le thème sauvegardé
function loadSavedTheme() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        document.body.setAttribute('data-theme', savedTheme);
    }
}

// Initialiser le thème au chargement
loadSavedTheme();

// Gestion des dates et calculs
function calculateDaysUntilExpiration(expirationDate) {
    const today = new Date();
    const expDate = new Date(expirationDate);
    const diffTime = expDate - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
}

// Marquer un produit comme traité
function markAsResolved(productId, alertType) {
    if (confirm('Marquer cette alerte comme résolue ?')) {
        // Simuler l'action (dans une vraie app, appel API)
        showNotification('Alerte marquée comme résolue', 'success');

        // Cacher l'élément
        const alertElement = document.querySelector(`[data-product-id="${productId}"][data-alert-type="${alertType}"]`);
        if (alertElement) {
            alertElement.style.opacity = '0.5';
            setTimeout(() => {
                alertElement.remove();
            }, 1000);
        }
    }
}

// Recherche avancée dans les alertes
function searchAlerts() {
    const searchTerm = document.getElementById('globalSearch').value.toLowerCase();
    const alertCards = document.querySelectorAll('.alert-card');
    let foundCount = 0;

    alertCards.forEach(card => {
        const cardText = card.textContent.toLowerCase();
        if (cardText.includes(searchTerm)) {
            card.style.display = 'block';
            foundCount++;
            // Highlight le terme recherché
            highlightText(card, searchTerm);
        } else {
            card.style.display = 'none';
        }
    });

    showNotification(`${foundCount} alertes trouvées pour "${searchTerm}"`, 'info');
}

// Surligner le texte recherché
function highlightText(element, searchTerm) {
    const innerHTML = element.innerHTML;
    const index = innerHTML.toLowerCase().indexOf(searchTerm.toLowerCase());
    if (index >= 0) {
        element.innerHTML =
            innerHTML.substring(0, index) +
            '<mark>' + innerHTML.substring(index, index + searchTerm.length) + '</mark>' +
            innerHTML.substring(index + searchTerm.length);
    }
}