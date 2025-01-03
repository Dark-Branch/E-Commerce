import React from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
//import ItemCard from '../components/ItemCard';
import Topbar from '../components/Topbar';
import ItemSet from '../components/ItemSet';
import Banner from '../components/Banner';

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
];



const Homepage = () => {
  return (
    <div>
      <Topbar />
      <Navbar />
      <Banner/> 

      {/* UnderBanner Section */}
      <section className="bg-gradient-to-r from-indigo-600 via-purple-700 to-pink-600 py-12 text-center bg-opacity-90">
      <div className="max-w-screen-xl mx-auto px-4">
        {/* Headline */}
        <h2 className="text-3xl font-bold text-white mb-4">
          Discover Your Next Favorite Product Today!
        </h2>

        {/* Subheadline */}
        <p className="text-white text-lg mb-8">
          Amazing deals on the best products, just for you.
        </p>

        {/* Images and CTA */}
        <div className="flex justify-center items-center space-x-6 flex-wrap">
          {/* Profile Image */}
          <img
            src="https://via.placeholder.com/100"
            alt="Profile"
            className="rounded-full border-4 border-white shadow-lg"
          />
          {/* Banner Image */}
          <img
            src="https://via.placeholder.com/400x250"
            alt="Featured Product"
            className="rounded-lg shadow-lg opacity-90"
          />
          {/* Smaller Supporting Images */}
          <div className="flex flex-col space-y-4">
            <img
              src="https://via.placeholder.com/150x100"
              alt="Category 1"
              className="rounded-md shadow-md"
            />
            <img
              src="https://via.placeholder.com/150x100"
              alt="Category 2"
              className="rounded-md shadow-md"
            />
          </div>
        </div>

        {/* Call to Action */}
        {/* <div className="mt-8">
          <a
            href="#"
            className="bg-white text-purple-700 font-semibold py-2 px-6 rounded-full shadow-md hover:bg-purple-100 transition duration-300"
          >
            Shop Now
          </a>
        </div> */}
      </div>
    </section>
      
      <ItemSet title="Trending Treasures" items={TrendingItems} />   
      <ItemSet title="Toys and Games" items={TrendingItems} />   
      <ItemSet title="Top picks for Sri Lanka" items={TrendingItems} />   
      <ItemSet title="Wireless Tech" items={TrendingItems} />   
      <ItemSet title="Level up your beauty routine" items={TrendingItems} />  
      <ItemSet title="clothing,shoes and jewellery" items={TrendingItems} />  



      
      <Footer/>
    </div>
  );
};

export default Homepage;
