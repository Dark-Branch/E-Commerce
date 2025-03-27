import PropTypes from "prop-types";
import ItemCard from "./ItemCard";

const ItemSet = ({ items, title }) => {
    return (
        <section className="py-8 px-6 sm:px-12 lg:px-20 max-w-screen-2xl mx-auto">
            <div className="mb-4">
                <h2 className="text-2xl font-bold mt-2 text-left">{title}</h2>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-5">
                {items.map((item, index) => (
                    <ItemCard key={index} item={item} />
                ))}
            </div>

            <div className="text-center mt-6">
                <button className="bg-purple-500 text-white px-6 py-3 rounded-md transition-colors duration-300 hover:bg-purple-600">
                    Explore More...
                </button>
            </div>
        </section>
    );
};

ItemSet.propTypes = {
    title: PropTypes.string.isRequired,
    items: PropTypes.arrayOf(
        PropTypes.shape({
            image: PropTypes.string.isRequired,
            title: PropTypes.string.isRequired,
            price: PropTypes.number.isRequired,
            rating: PropTypes.number.isRequired,
            reviews: PropTypes.number.isRequired,
        })
    ).isRequired,
};

export default ItemSet;
