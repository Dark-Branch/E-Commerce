// Banner.jsx
import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Navigation, Pagination, Autoplay } from "swiper";
import Slide from "./Slide";

const Banner = () => {
  return (
      <section className="bg-gradient-to-r from-indigo-600 via-purple-700 to-pink-600 py-12 text-center bg-opacity-90">
        <div className="max-w-screen-xl mx-auto px-4">
          {/* Headline */}
          <h2 className="text-3xl font-bold text-white mb-6">
            Exclusive Offers! <br /> Grab Your Favorite Products Now!
          </h2>

        {/* Swiper Carousel */}
          <Swiper
              modules={[Navigation, Pagination, Autoplay]}
              spaceBetween={30}
              slidesPerView={1}
              navigation
              pagination={{ clickable: true }}
              autoplay={{ delay: 3000, disableOnInteraction: false }}
              className="w-full mx-auto"
          >
          <SwiperSlide>
            <Slide
                src="images/jacket.avif"
                alt="Product 1"
                title="Stylish Jacket"
                price="$99.99"
                link="#"
                className="rounded-lg"
            />
          </SwiperSlide>
          <SwiperSlide>
            <Slide
                src="https://via.placeholder.com/1200x600?text=Product+2"
                alt="Product 2"
                title="Wireless Headphones"
                price="$79.99"
                link="#"
                className="rounded-lg"
            />
          </SwiperSlide>
          <SwiperSlide>
            <Slide
                src="https://via.placeholder.com/1200x600?text=Product+3"
                alt="Product 3"
                title="Smart Watch"
                price="$149.99"
                link="#"
                className="rounded-lg"
            />
          </SwiperSlide>
          <SwiperSlide>
            <Slide
                src="https://via.placeholder.com/1200x600?text=Product+4"
                alt="Product 4"
                title="Fitness Tracker"
                price="$49.99"
                link="#"
                className="rounded-lg"
            />
          </SwiperSlide>
        </Swiper>

          {/* Call to Action */}
          <div className="mt-8">
            <a
                href="#"
                className="bg-white text-purple-700 font-semibold py-2 px-6 rounded-full shadow-md hover:bg-purple-100 transition duration-300"
            >
              Explore All Products
            </a>
          </div>
        </div>
      </section>
  );
};

export default Banner;
