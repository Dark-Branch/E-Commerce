import React from 'react'
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import ProductDetail from '../components/ProductDetail';
import FrequentlyViewed from '../components/FrequentlyViewed';
import TopReviews from '../components/TopReviews';
import CommentAndRateSection from '../components/CommentAndRateSection';

const product = {
  image: '/path-to-image.jpg',
  title: 'Lenovo ThinkBook 16 G6 16"',
  description: 'FHD+ Laptop Computer, 13th Gen Intel 14-Core i7-13700H, 64GB DDR5 RAM...',
  rating: 3.6,
  reviews: 30,
  price: 514.99,
  details: {
    Condition: 'Excellent - Refurbished',
    Processor: 'Intel Core i7 13th Gen',
    RAM: '64GB DDR5',
    'Graphics Processing Type': 'Integrated/On-Board Graphics',
    'Screen Size': '16 in',
    Brand: 'Lenovo',
    'Operating System': 'Windows 11 Pro',
  },
};

const frequentlyViewed = [
  {
    image: '/images/product1.jpg',
    title: 'Gaming PC 1',
    price: 489.99,
  },
  {
    image: '/images/product2.jpg',
    title: 'Gaming PC 2',
    price: 529.99,
  },
  {
    image: '/images/product3.jpg',
    title: 'Gaming PC 3',
    price: 549.99,
  },
  {
    image: '/images/product4.jpg',
    title: 'Gaming PC 4',
    price: 499.99,
  },
];

const reviews = [
  { text: 'Great gaming computer!', reviewer: 'User1', date: 'Dec 1, 2024', rating: 5 },
  { text: 'Perfect for my needs.', reviewer: 'User2', date: 'Dec 2, 2024', rating: 4 },
  { text: 'Good performance overall.', reviewer: 'User3', date: 'Dec 3, 2024', rating: 4 },
  { text: 'A bit overpriced but worth it.', reviewer: 'User4', date: 'Dec 4, 2024', rating: 3 },
  { text: 'Excellent customer service!', reviewer: 'User5', date: 'Dec 5, 2024', rating: 5 },
  { text: 'Would buy again.', reviewer: 'User6', date: 'Dec 6, 2024', rating: 5 },
  { text: 'Could be better.', reviewer: 'User7', date: 'Dec 7, 2024', rating: 3 },
  { text: 'Loving it so far!', reviewer: 'User8', date: 'Dec 8, 2024', rating: 5 },
];

<TopReviews reviews={reviews} />




const ProductDetails = () => {
  return (
    <div>
      <Navbar/>
      <div className="p-4">
      <ProductDetail product={product} />
    </div>
    <FrequentlyViewed items={frequentlyViewed} />
      <TopReviews reviews={reviews} />
      <CommentAndRateSection />

      <Footer/>
    </div>
  )
}

export default ProductDetails
