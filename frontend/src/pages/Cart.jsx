import React, { useState } from "react";
import Topbar from "../components/Topbar";
import Navbar from "../components/Navbar";

const ShoppingCartPage = () => {
  const [cartItems, setCartItems] = useState([
    { id: 1, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
    { id: 2, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
    { id: 3, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
    { id: 4, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
  ]);

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
    setCartItems((prevItems) => prevItems.filter((item) => item.id !== id));
  };

  const subtotal = cartItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );
  const shipping = 148.49;

  return (
    <>
      <header >
          <Topbar/>
          <Navbar/>
      </header>
    <div className="min-h-screen bg-white absolute w-full flex justify-center items-center">
      <div className="bg-gray-100 container m-10 rounded-md shadow-md ">
        <h1 className="text-3xl  pl-5 pt-6 mb-4">Shopping Cart</h1>
        <div className="mx-5">
          <hr class="border-t border-gray-300" />
        </div>
      
          
          <div className=" m-20">
            {cartItems.map((item) => (
              <div
                key={item.id}
                className="flex items-center justify-between  border-b py-4"
              >
                <div className="flex items-center gap-4">
                  <div className="w-16 h-16 bg-gray-200 rounded-md"></div>
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
                    className="px-2 py-1 bg-gray-200 rounded-md "
                  >
                    -
                  </button>
                </div>
                <button className="text-blue-600 text-sm bg-gray-200 p-2 rounded-md m-2">Buy it now</button>
                <button
                  onClick={() => removeItem(item.id)}
                  className="text-red-600 text-sm  bg-gray-200 p-2 rounded-md m-2"
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
              <button className="p-5 bg-purple-500 text-white py-2 rounded-md text-center">
                Go to checkout
              </button>
              </div>
            </div>
          </div>
  
      </div>
    </div>
    </>
  );
};

export default ShoppingCartPage;
