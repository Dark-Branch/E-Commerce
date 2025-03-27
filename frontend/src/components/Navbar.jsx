import { useState } from "react";

const categories = [
  "Electronics",
  "Motors",
  "Fashion",
  "Collectibles & Art",
  "Sports",
  "Health & Beauty",
  "Industrial Equipment",
  "Home & Garden",
  "Deals",
  "Sell",
];

const Navbar = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  return (
      <nav className="bg-gradient-to-r from-indigo-300 via-purple-300 to-pink-300 text-gray-900 z-50 sticky top-20">
        <div className="max-w-screen-xl mx-auto flex flex-wrap md:flex-nowrap text-sm items-center justify-between py-2 px-4">
          <div className="flex items-center">
            <button onClick={toggleMobileMenu} className="md:hidden text-gray-900 p-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" className="h-6 w-6">
                {isMobileMenuOpen ? (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16m-7 6h14" />
                )}
              </svg>
            </button>
          </div>

          <div className={`w-full md:w-auto flex flex-col md:flex-row md:items-center space-y-2 md:space-y-0 md:space-x-6 ${isMobileMenuOpen ? "flex" : "hidden md:flex"}`}>
            {categories.map((category, index) => (
                <div key={index} className="relative group">
                  <a href="#" className="hover:bg-gray-200 px-4 py-2 rounded-md inline-block">
                    {category}
                  </a>
                </div>
            ))}
          </div>
        </div>
      </nav>
  );
};

export default Navbar;
