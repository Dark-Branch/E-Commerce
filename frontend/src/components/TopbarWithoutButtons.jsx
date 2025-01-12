import Logo from '../assets/logo.png';
import search from '../assets/search-alt-2-svgrepo-com.svg';

import { Link } from 'react-router-dom';
import { useState,useEffect } from 'react';

const Topbar = () => {
  const [searchValue, setSearchValue] = useState("");
  const [placeholder, setPlaceholder] = useState("Search...");

  const handleSearch = (e) => {
    setSearchValue(e.target.value);
  };

  useEffect(() => {
    fetch("http://localhost:8000/Topbar")
      .then((res) => res.json())
      .then((data) => {
        setPlaceholder(data.placeHolder || "Search...");
      });
  }, []);

  return (
    <header className="bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 p-4 sticky top-0 z-10">
      <div className="flex items-center justify-between">
        {/* Left: Logo */}
        <div className="flex items-center">
          <Link to="/">
            <img src={Logo} alt="Logo" className="h-12 w-auto" />
          </Link>
        </div>

        {/* Center: Search Bar (visible on all screen sizes) */}
        <div className="flex-grow mx-4 flex justify-center items-center">
          <input
            type="text"
            placeholder={placeholder}
            value={searchValue}
            onChange={handleSearch}
            className="w-full max-w-lg p-2 px-5 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500"
          />
          <button className="flex items-center bg-white p-2 rounded-full w-9 mx-3">
            <img src={search} alt="Search Icon" />
          </button>
        </div>

       
      </div>
    </header>
  );
};

export default Topbar;
