import React from 'react';

const FrequentlyViewed = ({ items }) => {
  return (
    <div className="bg-gray-50 p-6 rounded-lg shadow-md w-full mt-6">
      <h2 className="text-lg font-bold text-gray-800 mb-4">Customers Frequently Viewed</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
        {items.map((item, index) => (
          <div
            key={index}
            className="bg-white border rounded-md shadow-sm hover:shadow-md p-4"
          >
            <img
              src={item.image}
              alt={item.title}
              className="h-40 w-full object-cover rounded-md mb-3"
            />
            <h3 className="text-sm font-semibold text-gray-700">{item.title}</h3>
            <p className="text-gray-500 text-sm">${item.price}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FrequentlyViewed;
