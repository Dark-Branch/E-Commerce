import React from 'react';

const ProductDetail = ({ product }) => {
  return (
    <div className="bg-gray-50 p-6 rounded-lg shadow-md w-full">
      {/* Product Information */}
      <div className="flex flex-col md:flex-row gap-6">
        {/* Image Section */}
        <div className="flex-shrink-0">
          <div className="bg-gray-200 w-64 h-64 rounded-md flex items-center justify-center">
            <img
              src={product.image || '/placeholder.jpg'}
              alt={product.title}
              className="object-cover h-full w-full rounded-md"
            />
          </div>
        </div>

        {/* Details Section */}
        <div className="flex-1">
          <h1 className="text-xl font-bold text-gray-800">{product.title}</h1>
          <p className="text-gray-600 mt-2">{product.description}</p>
          <div className="flex items-center mt-4">
            <span className="text-yellow-500 text-lg mr-2">⭐ {product.rating}</span>
            <span className="text-gray-500">({product.reviews} reviews)</span>
          </div>
          <div className="mt-4">
            <span className="text-2xl font-semibold text-gray-800">${product.price}</span>
          </div>
          <div className="flex gap-4 mt-6">
            <button className="bg-blue-500 text-white px-6 py-2 rounded-md hover:bg-blue-600">
              Buy it Now
            </button>
            <button className="bg-gray-200 text-gray-800 px-6 py-2 rounded-md hover:bg-gray-300">
              Add to Cart
            </button>
          </div>
        </div>
      </div>

      {/* Item Specifics */}
      <div className="mt-8">
        <h2 className="text-lg font-bold text-gray-800">Item specifics</h2>
        <table className="w-full mt-4 text-left text-gray-600">
          <tbody>
            {Object.keys(product.details).map((key) => (
              <tr key={key}>
                <th className="py-2 pr-4 font-semibold">{key}:</th>
                <td>{product.details[key]}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ProductDetail;
