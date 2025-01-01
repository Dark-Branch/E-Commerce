import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Navigation, Pagination, Autoplay } from "swiper";

const Banner = () => {
  return (
    <section className="bg-gradient-to-r from-indigo-600 via-purple-700 to-pink-600 py-12 text-center bg-opacity-90">
      <div className="max-w-screen-xl mx-auto px-4">
        {/* Headline */}
        <h2 className="text-3xl font-bold text-white mb-6">
          Loyality or Respect?  L  O  V  E  ...
        </h2>

        {/* Swiper Carousel */}
        <Swiper
          modules={[Navigation, Pagination, Autoplay]}
          spaceBetween={30}
          slidesPerView={1}
          navigation
          pagination={{ clickable: true }}
          autoplay={{ delay: 3000, disableOnInteraction: false }}
          className="w-full max-w-3xl mx-auto"
        >
          <SwiperSlide>
            <img
              src="https://via.placeholder.com/600x300"
              alt="Product 1"
              className="rounded-lg shadow-lg"
            />
          </SwiperSlide>
          <SwiperSlide>
            <img
              src="https://via.placeholder.com/600x300?text=Product+2"
              alt="Product 2"
              className="rounded-lg shadow-lg"
            />
          </SwiperSlide>
          <SwiperSlide>
            <img
              src="https://via.placeholder.com/600x300"
              alt="Product 1"
              className="rounded-lg shadow-lg"
            />
          </SwiperSlide>
          <SwiperSlide>
            <img
              src="https://via.placeholder.com/600x300?text=Product+2"
              alt="Product 2"
              className="rounded-lg shadow-lg"
            />
          </SwiperSlide>
        </Swiper>

        {/* Call to Action */}
        <div className="mt-8">
          <a
            href="#"
            className="bg-white text-purple-700 font-semibold py-2 px-6 rounded-full shadow-md hover:bg-purple-100 transition duration-300"
          >
            Shop Now
          </a>
        </div>
      </div>
    </section>
  );
};

export default Banner;
