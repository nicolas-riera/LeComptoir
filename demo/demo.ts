interface Product {
    ref: string;
    label: string;
    price: number;
    category: string;
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

async function performCheckout(receiptNumber: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/checkout`, {
        method: "POST"
    });

    if (!response.ok) {
        throw new Error("Checkout failed");
    }

    const data: CheckoutResponse = await response.json();

    console.log(data.receipt);
    console.log("\n");
}

async function runDemo() {
    try {
        console.log("Fetching catalog from Java server...\n");
        const products = await fetchProducts();

        if (products.length < 3) {
            console.error("Not enough products in catalog.");
            return;
        }

        // Cart 1
        console.log("--- Processing Cart 1 ---");
        await addToCart(products[0].ref, 2);
        await addToCart(products[1].ref, 1);
        await performCheckout(1);

        // Cart 2
        console.log("--- Processing Cart 2 ---");
        await addToCart(products[2].ref, 5);
        await performCheckout(2);

        // Cart 3
        console.log("--- Processing Cart 3 ---");
        await addToCart(products[0].ref, 1);
        await addToCart(products[2].ref, 2);
        await performCheckout(3);

    } catch (error) {
        console.error("Demo execution error:", error);
    }
}

runDemo();