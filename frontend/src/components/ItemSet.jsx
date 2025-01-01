import React from 'react';
import ItemCard from './ItemCard';

const ItemSet = ({ items, title }) => {
  return (
    <section className="py-8">
      <div className="mb-4">
        <h2 className="text-2xl font-bold mt-2 text-left">{title}</h2>
      </div>
      <ItemCard items={items} />
      <div className="text-center mt-4">
        <button className="bg-purple-500 text-white px-4 py-2 rounded-md transition-colors duration-300">
          Explore More...
        </button>
      </div>
    </section>
  );
};

export default ItemSet;
