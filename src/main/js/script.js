

const categoryButtons = document.querySelectorAll('.category-btn');
const searchInput = document.getElementById('search-input');
const restaurantGrid = document.getElementById('restaurant-grid');
const modal = document.getElementById('menu-modal');
const menuTitle = document.getElementById('menu-title');
const menuItems = document.getElementById('menu-items');
const closeBtn = document.querySelector('.close-btn');

function createRestaurantCard(restaurant) {
    return `
        <div class="restaurant-card">
            <img src="${restaurant.image}" alt="${restaurant.name}" class="restaurant-image">
            <div class="restaurant-info">
                <h2 class="restaurant-name">${restaurant.name}</h2>
                <div class="restaurant-rating">★ ${restaurant.rating}</div>
                <span class="restaurant-cuisine">${restaurant.cuisine}</span>
            </div>
            <div class="restaurant-footer">
                <span class="delivery-time">${restaurant.deliveryTime}</span>
                <button class="order-btn" onclick="openMenu(${restaurant.id})">View Menu</button>
            </div>
        </div>
    `;
}

function renderRestaurants(filteredRestaurants) {
    restaurantGrid.innerHTML = filteredRestaurants.map(createRestaurantCard).join('');
}

function filterRestaurants(category, searchTerm) {
    return restaurants.filter(restaurant =>
        (category === 'All' || restaurant.cuisine === category) &&
        restaurant.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
}

function openMenu(restaurantId) {
    const restaurant = restaurants.find(r => r.id === restaurantId);
    menuTitle.textContent = `${restaurant.name} - Menu`;
    menuItems.innerHTML = restaurant.menu.map(item => `<p>${item}</p>`).join('');
    modal.style.display = 'block';
}

categoryButtons.forEach(button => {
    button.addEventListener('click', () => {
        categoryButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
        renderRestaurants(filterRestaurants(button.dataset.category, searchInput.value));
    });
});

searchInput.addEventListener('input', (e) => {
    renderRestaurants(filterRestaurants(document.querySelector('.category-btn.active').dataset.category, e.target.value));
});

closeBtn.addEventListener('click', () => modal.style.display = 'none');
window.addEventListener('click', (event) => {
    if (event.target === modal) modal.style.display = 'none';
});

document.addEventListener('DOMContentLoaded', () => renderRestaurants(restaurants));



document.addEventListener("DOMContentLoaded", async function () {
    const restaurantGrid = document.getElementById("restaurant-grid");

    // Функция для загрузки ресторанов
    async function loadRestaurants() {
        try {
            const response = await fetch('/api/restaurants');
            const restaurants = await response.json();

            restaurantGrid.innerHTML = ''; // Очистите сетку перед загрузкой

            restaurants.forEach(restaurant => {
                const restaurantCard = document.createElement("div");
                restaurantCard.className = "restaurant-card";
                restaurantCard.innerHTML = `
                    <img class="restaurant-image" src="restaurant-image.jpg" alt="${restaurant.name}">
                    <div class="restaurant-info">
                        <h3 class="restaurant-name">${restaurant.name}</h3>
                        <p class="restaurant-address">${restaurant.address}</p>
                        <p class="restaurant-cuisine">${restaurant.cuisine}</p>
                        <div class="restaurant-rating">⭐ ${restaurant.rating}</div>
                    </div>
                    <div class="restaurant-footer">
                        <span class="delivery-time">Delivery: ${restaurant.deliveryTime} min</span>
                        <button class="order-btn" onclick="openMenuModal('${restaurant.name}')">View Menu</button>
                    </div>
                `;
                restaurantGrid.appendChild(restaurantCard);
            });
        } catch (error) {
            console.error("Error loading restaurants:", error);
        }
    }

    loadRestaurants();
});

document.querySelectorAll('.subcategory-btn').forEach(button => {
    button.addEventListener('click', () => {
        const selectedSubcategory = button.dataset.subcategory;
        filterItemsBySubcategory(selectedSubcategory);
    });
});

function filterItemsBySubcategory(subcategory) {
    const items = document.querySelectorAll('.restaurant-card');
    items.forEach(item => {
        if (item.classList.contains(subcategory)) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}
document.querySelectorAll('.category-group').forEach(category => {
    category.addEventListener('click', () => {
        const categoryName = category.getAttribute('data-category');
        let pageUrl;

        switch (categoryName) {
            case 'Supermarkets':
                pageUrl = 'supermarkets-menu.html';
                break;
            case 'Pharmacies':
                pageUrl = 'pharmacies-menu.html';
                break;
            case 'Restaurants':
                pageUrl = 'restaurants-menu.html';
                break;
            case 'PetStores':
                pageUrl = 'petstores-menu.html';
                break;
            default:
                pageUrl = '#';
        }

        if (pageUrl !== '#') {
            window.location.href = pageUrl;
        }
    });
});

