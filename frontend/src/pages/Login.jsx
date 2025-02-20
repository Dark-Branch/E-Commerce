import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { loginUser } from "../api/auth"; 
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const schema = yup.object().shape({
    email: yup.string().email("Invalid email format").required("Email is required"),
    password: yup.string().min(6, "Password must be at least 6 characters").required("Password is required"),
});

function Login() {
    const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(schema),
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate(); 

    const onSubmit = async (data) => {
        setLoading(true);
        setError("");
    
        try {
            const response = await loginUser(data); // Call login API
            sessionStorage.setItem("token", response.token); // Store JWT token
    
            navigate("/"); // Redirect to dashboard after login
        } catch (err) {
            setError(err.message || "Login failed");
        } finally {
            setLoading(false);
        }
    };
    

    return (
        <div className="flex items-center justify-center mt-10">
            <form onSubmit={handleSubmit(onSubmit)} className="bg-white p-6 rounded shadow-md w-80">
                <h2 className="text-xl font-bold mb-4">Login</h2>

                {error && <p className="text-red-500 text-sm mb-2">{error}</p>}

                <div className="mb-4">
                    <label className="block text-sm font-medium">Email</label>
                    <input {...register("email")} type="email" className="w-full px-3 py-2 border rounded" />
                    <p className="text-red-500 text-xs mt-1">{errors.email?.message}</p>
                </div>
                <div className="mb-4">
                    <label className="block text-sm font-medium">Password</label>
                    <input {...register("password")} type="password" className="w-full px-3 py-2 border rounded" />
                    <p className="text-red-500 text-xs mt-1">{errors.password?.message}</p>
                </div>

                <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded" disabled={loading}>
                    {loading ? "Logging in..." : "Login"}
                </button>
            </form>
        </div>
    );
}

export default Login;
