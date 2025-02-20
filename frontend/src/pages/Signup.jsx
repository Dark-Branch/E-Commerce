import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { signupUser } from "../api/auth"; // Import API function
import { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate
import Topbar from '../components/TopbarWithoutButtons';

const schema = yup.object().shape({
    userName: yup.string().min(3, "Username must be at least 3 characters").max(50).required("Username is required"),
    email: yup.string().email("Invalid email").required("Email is required"),
    password: yup.string().min(6, "Password must be at least 6 characters").required("Password is required"),
    confirmPassword: yup.string()
        .oneOf([yup.ref("password"), null], "Passwords must match")
        .required("Confirm password is required"),
});

function Signup() {
    const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(schema),
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const navigate = useNavigate(); // Initialize the navigate function

    const onSubmit = async (data) => {
        setLoading(true);
        setError("");
        setSuccess("");
        try {
            await signupUser(data); // Call the API to create an account
            setSuccess("Account created successfully!");
            
            // Redirect to login after 2 seconds
            setTimeout(() => {
                navigate("/login");
            }, 1000);
        } catch (err) {
            setError(err.message || "Signup failed");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <Topbar />
            <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
                <div className="bg-white shadow-lg rounded-lg p-8 max-w-md w-full">
                    <h2 className="text-2xl font-bold text-center mb-6 text-gray-800">Sign Up</h2>
                    {error && <p className="text-red-500 text-sm mb-2">{error}</p>}
                    {success && <p className="text-green-500 text-sm mb-2">{success}</p>}
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Username</label>
                            <input {...register("userName")} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
                            <p className="text-red-500 text-xs mt-1">{errors.userName?.message}</p>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Email</label>
                            <input {...register("email")} type="email" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
                            <p className="text-red-500 text-xs mt-1">{errors.email?.message}</p>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Password</label>
                            <input {...register("password")} type="password" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
                            <p className="text-red-500 text-xs mt-1">{errors.password?.message}</p>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Confirm Password</label>
                            <input {...register("confirmPassword")} type="password" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
                            <p className="text-red-500 text-xs mt-1">{errors.confirmPassword?.message}</p>
                        </div>

                        <button type="submit" className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500" disabled={loading}>
                            {loading ? "Signing Up..." : "Sign Up"}
                        </button>
                    </form>
                    <div className="text-center mt-4">
                        <p className="text-sm text-gray-600">Already have an account? <a href="/login" className="text-indigo-600 hover:underline">Log in here</a></p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Signup;
