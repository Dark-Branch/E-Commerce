import React, { useState } from 'react';

const TopReviews = ({ reviews }) => {
  const [visibleReviews, setVisibleReviews] = useState(6);

  const handleSeeMore = () => {
    setVisibleReviews(reviews.length); // Show all reviews
  };

  return (
    <div className="bg-gray-50 p-6 rounded-lg shadow-md w-full mt-6">
      <h2 className="text-lg font-bold text-gray-800 mb-4">Top Reviews</h2>
      <div className="space-y-6">
        {reviews.slice(0, visibleReviews).map((review, index) => (
          <div
            key={index}
            className="border rounded-md p-4 shadow-sm hover:shadow-md"
          >
            <p className="text-gray-600 text-sm">{review.text}</p>
            <div className="flex justify-between items-center mt-3">
              <span className="text-sm text-gray-500">
                {review.reviewer} - {review.date}
              </span>
              <span className="text-yellow-500 text-sm">⭐ {review.rating}</span>
            </div>
          </div>
        ))}
      </div>
      {visibleReviews < reviews.length && (
        <div className="mt-4">
          <button
            onClick={handleSeeMore}
            className="text-blue-500 hover:underline text-sm"
          >
            See more reviews &gt;
          </button>
        </div>
      )}
    </div>
  );
};

export default TopReviews;
