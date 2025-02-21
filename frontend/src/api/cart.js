import axios from "axios";

const API_URL = "http://localhost:8080/cart";
const token = sessionStorage.getItem("token");

export const getCart = async () => {
    try {
        const response = await axios.get(API_URL, {
            headers: {
                Authorization: `Bearer ${token}`, // Pass token in the Authorization header
            },
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || "Something went wrong";
    }
};

export const addToCart = async (productId) => {
    try {
        const response = await axios.post(`${API_URL}/add/${productId}`, {}, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || "Something went wrong";
    }
};

export const removeFromCart = async (productId) => {
    try {
        const response = await axios.delete(`${API_URL}/remove/${productId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || "Something went wrong";
    }
};

export const checkoutCart = async () => {
    try {
        const response = await axios.post(`${API_URL}/checkout`, {}, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response?.data || "Something went wrong";
    }
}

