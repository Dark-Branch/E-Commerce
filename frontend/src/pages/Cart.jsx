import React, { useState } from "react";
import { useNavigate } from "react-router-dom";  // Import useNavigate
import Topbar from "../components/Topbar";
import Navbar from "../components/Navbar";
import { removeFromCart, getCart } from "../api/cart";

const ShoppingCartPage = () => {
  const [cartItems, setCartItems] = useState([
    { id: 1, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
    { id: 2, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
    { id: 3, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
    { id: 4, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
  ]);

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const data = await getCart(); // Fetch cart items
        setCartItems(data); // Update state
      } catch (error) {
        console.error("Failed to fetch cart", error);
      }
    };

    fetchCart();
  }, [cartItems]);

  const navigate = useNavigate();  // Initialize useNavigate

  // Handle the navigation when the button is clicked
  const handleGoToCheckout = () => {
    

    navigate("/checkout",{state:cartItems});  // Navigate to the checkout page
  };

  const updateQuantity = (id, delta) => {
    setCartItems((prevItems) =>
      prevItems.map((item) =>
        item.id === id
          ? { ...item, quantity: Math.max(1, item.quantity + delta) }
          : item
      )
    );
  };

  const removeItem = (id) => {
    try {
      removeFromCart(id);
    } catch (err) {
      console.error(err);
    }
    setCartItems((prevItems) => prevItems.filter((item) => item.id !== id));
  };

  const subtotal = cartItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );
  const shipping = 148.49;

  const [selectedItems, setSelectedItems] = useState([]);  // Initialize selectedItems state

  const handleCheckboxTick = (itemID) => {  
    if (selectedItems.includes(itemID)) {  // If itemID is already in selectedItems
      setSelectedItems(selectedItems.filter((id) => id !== itemID));  // Remove itemID from selectedItems
    } else {
      setSelectedItems([...selectedItems, itemID]);  // Add itemID to selectedItems
    }
    console.log(selectedItems);  // Log selectedItems
  }



  return (
    <div>
      
        <Topbar />
        <Navbar />
      
        <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
        <div className="bg-gray-100 container m-10 rounded-md shadow-md">
          <h1 className="text-3xl pl-5 pt-6 mb-4">Shopping Cart</h1>
          <div className="mx-5">
            <hr className="border-t border-gray-300" />
          </div>

          <div className="m-20">
            {cartItems.map((item) => (
              <div
                key={item.id}
                className="flex items-center justify-between border-b py-4"
              >
                <input onClick={() => handleCheckboxTick(item.id)} type="checkbox" className="cursor-pointer m-5" />
                <div className="flex items-center gap-4">
                  <div className="w-16 h-16 bg-gray-200 rounded-md">
                    {/* set link for the image */}
                    {/* <img src={item.imgLink} className="h-10"/> */}
                  </div>
                  <span className="text-sm font-medium">{item.name}</span>
                </div>
                <span className="text-sm">${item.price.toFixed(2)}</span>
                <div className="flex items-center gap-2">
                  <button
                    onClick={() => updateQuantity(item.id, 1)}
                    className="px-2 py-1 bg-gray-200 rounded-md"
                  >
                    +
                  </button>
                  <span className="text-sm font-medium">{item.quantity}</span>
                  <button
                    onClick={() => updateQuantity(item.id, -1)}
                    className="px-2 py-1 bg-gray-200 rounded-md"
                  >
                    -
                  </button>
                </div>
                <button className="text-blue-600 text-sm bg-gray-200 p-2 rounded-md m-2">
                  Buy now
                </button>
                <button
                  onClick={() => removeItem(item.id)}
                  className="text-red-600 text-sm bg-gray-200 p-2 rounded-md m-2"
                >
                  Remove
                </button>
              </div>
            ))}
          </div>

          {/* Summary Section */}
          <div className="flex justify-center w-full">
            <div className="mt-6 w-1/2 p-4 ">
              <div className="flex justify-between text-sm mb-2">
                <span>Items ({cartItems.length})</span>
                <span>${subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sm mb-2">
                <span>Shipping to 50212</span>
                <span>${shipping.toFixed(2)}</span>
              </div>
              <div className="flex justify-between font-semibold text-lg mb-4">
                <span>Subtotal</span>
                <span>${(subtotal + shipping).toFixed(2)}</span>
              </div>
              <div className="flex justify-center">
                <button
                  onClick={handleGoToCheckout}  // Set onClick to call handleGoToCheckout
                  className="p-5 bg-purple-500 text-white py-2 rounded-md text-center"
                >
                  Go to checkout
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      </div>
  );
};

export default ShoppingCartPage;
