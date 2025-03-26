import React, { useState } from "react";
import PropTypes from "prop-types";

const Slide = ({ src, alt, title, description, ctaLink, ctaText, className }) => {
    const [imageLoaded, setImageLoaded] = useState(true);

    // Handle image load failure
    const handleImageError = () => {
        setImageLoaded(false);
    };

    return (
        <div className="swiper-slide relative">
            {/* Fixed container size with increased height */}
            <div className={`w-full h-[500px] ${className} relative`}>
                {imageLoaded ? (
                    <img
                        src={src}
                        alt={alt}
                        className="rounded-lg shadow-lg w-full h-full object-cover"
                        onError={handleImageError} // Handle image load failure
                    />
                ) : (
                    <div className="w-full h-full flex items-center justify-center bg-gray-200 text-gray-500">
                        Image Not Available
                    </div>
                )}
            </div>

            {/* Overlay with title and description */}
            <div className="absolute bottom-0 left-0 w-full bg-gradient-to-t from-black to-transparent p-6 text-white">
                <h3 className="text-3xl font-bold mb-3">{title}</h3>
                <p className="text-lg mb-6">{description}</p>

                {/* Enhanced Call-to-action button */}
                <a
                    href={ctaLink}
                    className="inline-block bg-purple-700 py-3 px-8 rounded-full text-white font-semibold shadow-lg hover:bg-purple-600 transition duration-300"
                >
                    {ctaText}
                </a>
            </div>
        </div>
    );
};

// Prop validation
Slide.propTypes = {
    src: PropTypes.string.isRequired,
    alt: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    ctaLink: PropTypes.string.isRequired,
    ctaText: PropTypes.string.isRequired,
    className: PropTypes.string,
};

export default Slide;
