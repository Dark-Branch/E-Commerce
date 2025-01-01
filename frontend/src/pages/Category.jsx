import React, { useState } from "react";
import Navbar from "../components/Navbar";
import Topbar from "../components/Topbar";
import Footer from "../components/Footer";
import ItemSet from "../components/ItemSet";
import FilterOverlay from "../components/FilterOverlay";

const TrendingItems = [
  {
    image: "https://via.placeholder.com/150",
    title: "Wireless Headphones",
    price: 99.99,
    rating: 4.5,
    reviews: 120,
  },
  {
    image: "https://via.placeholder.com/150",
    title: "Smartphone",
    price: 699.99,
    rating: 4.8,
    reviews: 95,
  },
  {
    image: "https://via.placeholder.com/150",
    title: "Gaming Mouse",
    price: 49.99,
    rating: 4.2,
    reviews: 45,
  },
  {
    image: "https://via.placeholder.com/150",
    title: "Laptop Backpack",
    price: 79.99,
    rating: 4.6,
    reviews: 150,
  },
  // Add more items for testing pagination
];

const ITEMS_PER_PAGE = 20;

const CategoryPage = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [searchQuery, setSearchQuery] = useState("");
  const [priceRange, setPriceRange] = useState([0, 1000]);

  // Filtering and pagination logic
  const filteredItems = TrendingItems.filter(
    (item) =>
      item.title.toLowerCase().includes(searchQuery.toLowerCase()) &&
      item.price >= priceRange[0] &&
      item.price <= priceRange[1]
  );

  const totalPages = Math.ceil(filteredItems.length / ITEMS_PER_PAGE);

  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const currentItems = filteredItems.slice(startIndex, startIndex + ITEMS_PER_PAGE);

  // Handlers
  const handleSearch = (event) => {
    setSearchQuery(event.target.value);
    setCurrentPage(1); // Reset to page 1 after filtering
  };

  const handlePriceRangeChange = (min, max) => {
    setPriceRange([min, max]);
    setCurrentPage(1); // Reset to page 1 after filtering
  };

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handlePageClick = (page) => {
    setCurrentPage(page);
  };

  return (
    <div>

      <Topbar />
      <Navbar />

      <br />

      {/* Filter Overlay */}
      <FilterOverlay
        searchQuery={searchQuery}
        handleSearch={handleSearch}
        priceRange={priceRange}
        handlePriceRangeChange={handlePriceRangeChange}
      />

      {/* Main Content */}
      <main className="p-4">
        <ItemSet title="ToysToysToysToysToys" items={currentItems} />

        {/* Pagination */}
        <div className="flex justify-center items-center space-x-4 mt-8">
          <button
            onClick={handlePrevPage}
            disabled={currentPage === 1}
            className="px-4 py-2 bg-gray-300 hover:bg-gray-400 text-black rounded disabled:opacity-50"
          >
            Previous
          </button>
          {Array.from({ length: totalPages }, (_, index) => (
            <button
              key={index}
              onClick={() => handlePageClick(index + 1)}
              className={`px-3 py-1 rounded ${
                currentPage === index + 1
                  ? "bg-blue-600 text-white"
                  : "bg-gray-300 hover:bg-gray-400 text-black"
              }`}
            >
              {index + 1}
            </button>
          ))}
          <button
            onClick={handleNextPage}
            disabled={currentPage === totalPages}
            className="px-4 py-2 bg-gray-300 hover:bg-gray-400 text-black rounded disabled:opacity-50"
          >
            Next
          </button>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default CategoryPage;
