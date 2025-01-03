import React, { useState } from "react";

const FilterOverlay = ({
  searchQuery,
  handleSearch,
  priceRange,
  handlePriceRangeChange,
}) => {
  const [isOverlayOpen, setIsOverlayOpen] = useState(false);

  const openOverlay = () => {
    setIsOverlayOpen(true);
  };

  const closeOverlay = () => {
    setIsOverlayOpen(false);
  };

  return (
    <>
      {/* Button to open the overlay */}
      <button
        onClick={openOverlay}
        className="text-white bg-indigo-600 px-4 py-2 rounded-lg"
      >
        Filters
      </button>

      {/* The Overlay */}
      <div
        id="myNav"
        className={`overlay ${isOverlayOpen ? "w-full" : "w-0"}`}
        style={{
          height: "100%",
          position: "fixed",
          top: 0,
          left: 0,
          zIndex: 10,
          backgroundColor: "rgba(0, 0, 0, 0.9)",
          overflowX: "hidden",
          transition: "0.5s",
        }}
      >
        {/* Close button */}
        <button
          onClick={closeOverlay}
          className="closebtn text-white absolute top-4 right-8 text-4xl"
        >
          &times;
        </button>

        {/* Filter Content */}
        <div className="overlay-content p-8 text-white">
          <h2 className="text-lg font-bold mb-4">Filter Products</h2>

          {/* Search Filter */}
          <div className="mb-4">
            <label className="block text-sm font-medium">Search</label>
            <input
              type="text"
              value={searchQuery}
              onChange={handleSearch}
              placeholder="Search products..."
              className="w-full px-3 py-2 border rounded text-black"
            />
          </div>

          {/* Price Range Filter */}
          <div className="mb-4">
            <label className="block text-sm font-medium">Price Range</label>
            <div className="flex space-x-2">
              <input
                type="number"
                value={priceRange[0]}
                onChange={(e) =>
                  handlePriceRangeChange(Number(e.target.value), priceRange[1])
                }
                placeholder="Min"
                className="w-1/2 px-2 py-1 border rounded text-black"
              />
              <input
                type="number"
                value={priceRange[1]}
                onChange={(e) =>
                  handlePriceRangeChange(priceRange[0], Number(e.target.value))
                }
                placeholder="Max"
                className="w-1/2 px-2 py-1 border rounded text-black"
              />
            </div>
          </div>
        </div>
      </div>

      {/* Overlay CSS */}
      <style jsx>{`
        .overlay {
          width: 0;
          height: 100%;
          transition: width 0.5s;
        }
        .overlay.w-full {
          width: 100%;
        }
        .overlay-content {
          position: relative;
          top: 25%;
          text-align: center;
        }
        .closebtn {
          font-size: 40px;
          cursor: pointer;
        }
      `}</style>
    </>
  );
};

export default FilterOverlay;
