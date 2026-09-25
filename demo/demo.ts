interface Product {
    ref: string;
    label: string;
    price: number;
    category: string;
}

interface LoyaltyCardData {
    points: number;
    isVip: boolean;
}

interface CheckoutResponse {
    receipt: string;
    total: number;
}

const API_BASE_URL = "http://127.0.0.1:8080/api";

async function fetchProducts(): Promise<Product[]> {
    const response = await fetch(`${API_BASE_URL}/products`);
    if (!response.ok) {
        throw new Error(`Failed to fetch products: ${response.status}`);
    }
    return await response.json();
}

async function addToCart(ref: string, quantity: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/cart/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ref, quantity })
    });

    if (!response.ok) {
        console.error(`Failed to add product: ${ref}`);
    }
}

async function performCheckout(card?: LoyaltyCardData): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/checkout`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: card ? JSON.stringify(card) : JSON.stringify({})
    });

    if (!response.ok) {
        throw new Error("Checkout failed");
    }

    const data: CheckoutResponse = await response.json();

    console.log(data.receipt);
    console.log("\n");
}

async function clearCart(): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/cart/clear`, {
        method: "POST"
    });

    if (!response.ok) {
        console.error("Failed to clear cart");
    }
}

async function runDemo() {
    try {
        console.log("Fetching catalog from Java server...\n");
        const products = await fetchProducts();

        if (products.length < 18) {
            console.error("Not enough products in catalog.");
            return;
        }

        console.log("--- Processing Cart 1 (Loyalty Card: 20 points) ---");
        await clearCart();
        await addToCart(products[0].ref, 2);
        await addToCart(products[1].ref, 1);
        await performCheckout({ points: 20, isVip: false });

        console.log("--- Processing Cart 2 (Loyalty Card: 250 points) ---");
        await clearCart();
        await addToCart(products[2].ref, 5);
        await performCheckout({ points: 250, isVip: false });

        console.log("--- Processing Cart 3 (Loyalty Card VIP: 1100 points) ---");
        await clearCart();
        await addToCart(products[0].ref, 1);
        await addToCart(products[2].ref, 5);
        await addToCart(products[5].ref, 6);
        await performCheckout({ points: 1100, isVip: true });

        console.log("--- Processing Cart 4 (Loyalty Card VIP: 115 points) ---");
        await clearCart();
        await addToCart(products[1].ref, 3);
        await addToCart(products[2].ref, 5);
        await addToCart(products[5].ref, 6);
        await performCheckout({ points: 115, isVip: true });

        console.log("--- Processing Cart 5 (No Loyalty Card) ---");
        await clearCart();
        await addToCart(products[1].ref, 3);
        await addToCart(products[3].ref, 1);
        await addToCart(products[17].ref, 2);
        await performCheckout();

        console.log("--- Processing Cart 6 (No Loyalty Card) ---");
        await clearCart();
        await addToCart(products[0].ref, 1);
        await addToCart(products[4].ref, 2);
        await performCheckout();

    } catch (error) {
        console.error("Demo execution error:", error);
    }
}

runDemo();