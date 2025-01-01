import cart from '../assets/cart-shopping-fast-svgrepo-com.svg';
import Ecom from '../assets/cart-svgrepo-com.svg';
//import search from '../assets/search-alt-2-svgrepo-com.svg';
import { useState } from 'react';

const Topbar = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  return (
    <header className="bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 p-4 sticky top-0 z-50">
      <div className=" flex items-center relative ">
        {/* Left: Logo */}
        <div className="flex items-center">
          <img src={Ecom} alt="Logo" className="h-8 w-auto" />
        </div>

        {/* Center: Search Bar */}
        <div className="flex-grow mx-4 hidden md:flex justify-center">
          <input
            type="text"
            placeholder="Search..."
            className="w-full max-w-lg p-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500"
          />
        </div>

        {/* Right: Login, Signup, and Cart */}
        <div className="absolute right-0 flex  space-x-4 md:space-x-6">
          <div className="hidden md:flex items-center space-x-4">
            <a href="/login" className="text-white px-4 py-2 rounded-md hover:bg-purple-600 transition-colors duration-300">
              Login
            </a>
            <a href="/signup" className="text-white px-4 py-2 rounded-md hover:bg-purple-600 transition-colors duration-300">
              Sign Up
            </a>
          </div>

          <a href="/cart" className="text-white text-2xl relative">
            <img src={cart} alt="Cart" className="h-8 w-auto hidden md:flex" />
            {/* Optional: Add a cart count badge */}
            <span className="hidden md:flex absolute top-0 right-0 items-center justify-center px-2 py-1 text-xs font-bold leading-none text-red-100 transform translate-x-1/2 -translate-y-1/2 bg-red-600 rounded-full">
              3
            </span>
          </a>

          {/* Mobile Menu Toggle */}
          <button onClick={toggleMobileMenu} className="md:hidden text-white">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              className="h-6 w-6"
            >
              {isMobileMenuOpen ? (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M6 18L18 6M6 6l12 12"
                />
              ) : (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M4 6h16M4 12h16m-7 6h14"
                />
              )}
            </svg>
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {isMobileMenuOpen && (
        <div className="md:hidden bg-purple-300 p-4 flex flex-col items-center">
          <input
            type="text"
            placeholder="Search..."
            className="w-full mb-4 p-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500"
          />
          <a href="/login" className="text-white w-full text-center py-2 rounded-md hover:bg-purple-600 transition-colors duration-300 mb-2">
            Login
          </a>
          <a href="/signup" className="text-white w-full text-center py-2 rounded-md hover:bg-purple-600 transition-colors duration-300">
            Sign Up
          </a>
        </div>
      )}
    </header>
  );
};

export default Topbar;