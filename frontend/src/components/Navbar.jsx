// Navbar.jsx
import { useState } from "react";
import logo from "../assets/logo.png";

// Navbar.jsx
const Navbar = () => {
  return (
    <header className="bg-gray-100 shadow-md">
      {/* Top Navbar */}
      <div className="container mx-auto px-4 py-2 flex items-center justify-between">
        {/* Logo */}
        <div>
          <img
            src={logo}
            alt="Logo"
            className="h-20"
          />
          </div>

        {/* Search Bar */}
        <div className="flex-grow mx-4">
          <input
            type="text"
            placeholder="Search for anything"
            className="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring focus:ring-gray-300"
          />
        </div>

        {/* Right Actions */}
        <div className="flex items-center space-x-4">
          <button className="text-sm font-medium text-gray-700 hover:text-gray-900">Login</button>
          <button className="text-sm font-medium text-white bg-blue-600 px-4 py-2 rounded-md hover:bg-blue-500">
            Signup
          </button>
          <button class="flex items-center px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 3h2l.4 2M7 13h10l3.6-8H5.4L7 13zm0 0l-1.5 4.5M7 13h10m-9.5 4.5a1.5 1.5 0 100 3 1.5 1.5 0 000-3zm10-1.5a1.5 1.5 0 100 3 1.5 1.5 0 000-3z" />
            </svg>
            
          </button>

        </div>
      </div>

      {/* Bottom Navbar */}
      <nav className="bg-white">
        <div className="container mx-auto px-4 py-2 flex overflow-x-auto">
          {[
            "Electronics",
            "Motors",
            "Fashion",
            "Collectibles and Art",
            "Sports",
            "Health & Beauty",
            "Industrial Equipment",
            "Home & Garden",
            "Deals",
            "Sell",
          ].map((category, index) => (
            <a
              key={index}
              href="#"
              className="text-sm text-gray-700 hover:text-gray-900 px-3 whitespace-nowrap"
            >
              {category}
            </a>
          ))}
        </div>
      </nav>
    </header>
  );
};

export default Navbar;
