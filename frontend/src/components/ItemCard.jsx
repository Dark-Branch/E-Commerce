import { FaStar } from "react-icons/fa";
import PropTypes from "prop-types";

const ItemCard = ({ item }) => {
    return (
        <div className="bg-white border rounded-md shadow-sm hover:shadow-md p-4 relative">
            <img
                src={item.image}
                alt={item.title}
                className="h-40 w-full object-cover rounded-md mb-3"
            />
            <h3 className="text-sm font-semibold text-gray-700">{item.title}</h3>
            <p className="text-gray-500 text-sm">${item.price}</p>

            <div className="absolute bottom-2 right-2 flex items-center space-x-1">
        <span className="text-purple-500 text-sm flex items-center">
          <FaStar className="mr-1" /> {item.rating}
        </span>
                <span className="text-gray-500 text-sm">({item.reviews} reviews)</span>
            </div>
        </div>
    );
};

ItemCard.propTypes = {
    item: PropTypes.shape({
        image: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        price: PropTypes.number.isRequired,
        rating: PropTypes.number.isRequired,
        reviews: PropTypes.number.isRequired,
    }).isRequired,
};

export default ItemCard;
