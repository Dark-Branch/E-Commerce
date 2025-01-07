import React from "react";
import { useForm, useFieldArray } from "react-hook-form";
import axios from "axios";
import Topbar from "../components/Topbar";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

const Seller = () => {
  const {
    register,
    handleSubmit,
    reset,
    control,
    formState: { errors },
  } = useForm();

  const { fields, append, remove } = useFieldArray({
    control,
    name: "specs", // for product specifications
  });

  const onSubmit = async (data) => {
    try {
      // Prepare form data
      const formData = new FormData();

      // Add text fields
      Object.entries(data).forEach(([key, value]) => {
        if (key !== "additionalImages") {
          formData.append(key, value);
        }
      });

      // Add additional image files
      const files = data.additionalImages;
      if (files && files.length > 0) {
        Array.from(files).forEach((file, index) => {
          formData.append(`additionalImages[${index}]`, file);
        });
      }

      // POST request to your backend API
      const response = await axios.post("https://example.com/api/products", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      alert("Product added successfully!");
      reset();
    } catch (error) {
      console.error(error);
      alert("Failed to add product. Please try again.");
    }
  };

  return (
    <div className="bg-gray-100 min-h-screen">
      <Topbar />
      <Navbar />

      <div className="max-w-screen-xl mx-auto px-4 py-12">
        <h1 className="text-4xl font-bold text-center mb-8 text-gray-800">Add a New Product</h1>
        <div className="bg-white p-6 rounded-lg shadow-lg max-w-2xl mx-auto">
          <form onSubmit={handleSubmit(onSubmit)}>
            {/* Product Name */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Product Name</label>
              <input
                type="text"
                {...register("name", { required: "Product name is required" })}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
              {errors.name && <p className="text-red-500 text-sm">{errors.name.message}</p>}
            </div>

            {/* Price */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Price (Rs)</label>
              <input
                type="number"
                {...register("price", { required: "Price is required", min: 0 })}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
              {errors.price && <p className="text-red-500 text-sm">{errors.price.message}</p>}
            </div>

            {/* Main Image */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Main Image</label>
              <input
                type="file"
                {...register("mainImg", { required: "Main image is required" })}
                className="w-full border-gray-300 rounded px-3 py-2"
                accept="image/*"
              />
              {errors.mainImg && <p className="text-red-500 text-sm">{errors.mainImg.message}</p>}
            </div>

            {/* Brand */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Brand</label>
              <input
                type="text"
                {...register("brand", { required: "Brand is required" })}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
              {errors.brand && <p className="text-red-500 text-sm">{errors.brand.message}</p>}
            </div>

            {/* Category and Sub-Category */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Category</label>
              <input
                type="text"
                {...register("category", { required: "Category is required" })}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
              {errors.category && <p className="text-red-500 text-sm">{errors.category.message}</p>}
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Sub-Category</label>
              <input
                type="text"
                {...register("subCategory")}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
            </div>

            {/* Tags */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Tags (comma-separated)</label>
              <input
                type="text"
                {...register("tags", { maxLength: 50 })}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
              <p className="text-gray-500 text-sm">E.g., wireless, headphones, tech</p>
            </div>

            {/* Inventory */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Inventory Count</label>
              <input
                type="number"
                {...register("inventory", { required: "Inventory count is required" })}
                className="w-full border-gray-300 rounded px-3 py-2"
              />
              {errors.inventory && (
                <p className="text-red-500 text-sm">{errors.inventory.message}</p>
              )}
            </div>

            {/* Description */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Product Description</label>
              <textarea
                {...register("description", { required: "Product description is required", maxLength: 1000 })}
                className="w-full border-gray-300 rounded px-3 py-2"
                rows="5"
              ></textarea>
              {errors.description && <p className="text-red-500 text-sm">{errors.description.message}</p>}
              <p className="text-gray-500 text-sm">
                Provide a detailed description of the product. Max 1000 characters.
              </p>
            </div>


            {/* Additional Images */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Additional Images</label>
              {fields.map((item, index) => (
                <div key={item.id} className="mb-2 flex space-x-2">
                  <input
                    type="text"
                    placeholder="Label"
                    {...register(`additionalImages.${index}.label`, { required: true })}
                    className="flex-1 border-gray-300 rounded px-3 py-2"
                  />
                  <input
                    type="file"
                    {...register(`additionalImages.${index}.file`, { required: true })}
                    className="flex-1 border-gray-300 rounded px-3 py-2"
                    accept="image/*"
                  />
                  <button
                    type="button"
                    onClick={() => remove(index)}
                    className="text-red-500 font-bold"
                  >
                    X
                  </button>
                </div>
              ))}
              <button
                type="button"
                onClick={() => append({ label: "", file: "" })}
                className="text-blue-500 font-bold"
              >
                + Add Image
              </button>
            </div>


            {/* Specifications */}
            <div className="mb-4">
              <label className="block text-gray-700 font-medium mb-2">Specifications</label>
              {fields.map((item, index) => (
                <div key={item.id} className="mb-2 flex space-x-2">
                  <input
                    placeholder="Spec"
                    {...register(`specs.${index}.spec`, { required: true })}
                    className="flex-1 border-gray-300 rounded px-3 py-2"
                  />
                  <input
                    placeholder="Value"
                    {...register(`specs.${index}.value`, { required: true })}
                    className="flex-1 border-gray-300 rounded px-3 py-2"
                  />
                  <button
                    type="button"
                    onClick={() => remove(index)}
                    className="text-red-500 font-bold"
                  >
                    X
                  </button>
                </div>
              ))}
              <button
                type="button"
                onClick={() => append({ spec: "", value: "" })}
                className="text-blue-500 font-bold"
              >
                + Add Spec
              </button>
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              className="w-full bg-indigo-600 text-white py-2 px-4 rounded hover:bg-indigo-700"
            >
              Add Product
            </button>
          </form>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default Seller;
