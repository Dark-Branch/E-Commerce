import axios from "axios";
import { API_BASE_URL } from "../config";

const API_URL = API_BASE_URL + "/auth";

export const signupUser = async (userData) => {
    try {
        const response = await axios.post(`${API_URL}/register`, userData);
        return response.data; 
    } catch (error) {
        throw error.response?.data || "Something went wrong"; 
    }
};

export const loginUser = async (userData) => {
    try {
        const response = await axios.post(`${API_URL}/login`, userData);
        return response.data;
    } catch (error) {
        throw error.response?.data || "Login failed. Please try again.";
    }
};
