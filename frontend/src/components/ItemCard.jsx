import React from 'react';
import { FaStar } from 'react-icons/fa';

const ItemCard = ({ items = [] }) => {
  return (
    <div className="bg-gray-50 p-6 rounded-lg shadow-md w-full mt-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
            {items.map((item, index) => (
                <div
                    key={index}
                    className="bg-white border rounded-md shadow-sm hover:shadow-md p-4 relative"
                >
                    <img
                        src={item.image}
                        alt={item.title}
                        className="h-40 w-full object-cover rounded-md mb-3"
                    />
                    <h3 className="text-sm font-semibold text-gray-700">{item.title}</h3>
                    <p className="text-gray-500 text-sm">${item.price}</p>
                    <div className="absolute bottom-2 right-2 flex items-center space-x-1">
                        <span className="text-purple-500 text-sm flex items-center glitter">
                            <FaStar className="mr-1" /> {item.rating}
                        </span>
                        <span className="text-gray-500 text-sm">({item.reviews} reviews)</span>
                    </div>
                </div>
            ))}
        </div>
    </div>
  );
};

export default ItemCard;