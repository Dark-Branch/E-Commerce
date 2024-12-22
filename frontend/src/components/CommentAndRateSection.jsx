import React, { useState } from 'react';

const CommentAndRateSection = () => {
  const [isCommentPopupOpen, setCommentPopupOpen] = useState(false);
  const [isRatePopupOpen, setRatePopupOpen] = useState(false);

  const handleCommentClick = () => {
    setCommentPopupOpen(true);
  };

  const handleRateClick = () => {
    setRatePopupOpen(true);
  };

  const closePopup = () => {
    setCommentPopupOpen(false);
    setRatePopupOpen(false);
  };

  return (
    <div className="mt-8">
      <h3 className="text-lg font-bold text-gray-800 mb-4">Write a Review</h3>
      <div className="flex space-x-4">
        <button
          onClick={handleCommentClick}
          className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
        >
          Write a Comment
        </button>
        <button
          onClick={handleRateClick}
          className="bg-yellow-500 text-white px-4 py-2 rounded-lg hover:bg-yellow-600 transition"
        >
          Rate
        </button>
      </div>

      {/* Comment Popup */}
      {isCommentPopupOpen && (
        <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h4 className="text-lg font-bold text-gray-800 mb-4">Write a Comment</h4>
            <textarea
              placeholder="Write your comment here..."
              className="w-full h-32 p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <div className="flex justify-end mt-4 space-x-2">
              <button
                onClick={closePopup}
                className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition"
              >
                Cancel
              </button>
              <button
                onClick={closePopup}
                className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
              >
                Submit
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Rate Popup */}
      {isRatePopupOpen && (
        <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h4 className="text-lg font-bold text-gray-800 mb-4">Rate the Product</h4>
            <div className="flex justify-center space-x-2 mb-4">
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  className="text-gray-400 hover:text-yellow-500 text-3xl"
                >
                  ★
                </button>
              ))}
            </div>
            <div className="flex justify-end mt-4 space-x-2">
              <button
                onClick={closePopup}
                className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition"
              >
                Cancel
              </button>
              <button
                onClick={closePopup}
                className="bg-yellow-500 text-white px-4 py-2 rounded-lg hover:bg-yellow-600 transition"
              >
                Submit
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CommentAndRateSection;
