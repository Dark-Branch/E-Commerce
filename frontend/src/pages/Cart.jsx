import React, { useEffect, useState } from "react";
import Topbar from "../components/Topbar";

import Navbar from "../components/Navbar";
import delimg from "../assets/delete-svgrepo-com.svg";
import LoadingImg from "../assets/Flowing gradient.gif"
import { useNavigate} from "react-router-dom";



const ShoppingCartPage = () => {
  // const [cartItems, setCartItems] = useState([
  //   { id: 1, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
  //   { id: 2, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
  //   { id: 3, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
  //   { id: 4, name: "Lenovo ThinkBook 16", price: 54.89, quantity: 2 },
  // ]);

  const [checkedItems, setCheckedItems] = useState([]);
  const [cartItems, setCartItems] = useState([]);
  const [isFetched,setIsFetched] = useState(false);

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

  const handleCheckbox = (e) => {
    setCheckedItems((pre) => {
      return e.target.checked? [...pre,e.target.value] : pre.filter((item)=> item !== e.target.value); 
    });
  } 

  const navigate = useNavigate();
  const handleSubmit = () =>{
    navigate("/Checkout",{state:{checkedItems}});
  }

  useEffect(() => {
    fetch('http://localhost:8000/cart')
    .then(res =>{
      return res.json();
    })
    .then(data =>{
      setCartItems(data);
    })
    .then(() => {
      setIsFetched(true);
    });
  },[]);

  return (
    <>
      <header className="sticky top-0 z-50">
        <Topbar />
      </header>

      <div className="min-h-screen bg-white flex justify-center items-center pt-[5px]">
      <div className="bg-gray-100 container m-10 rounded-md shadow-md ">
        
        <h1 className="text-3xl  pl-5 pt-6 mb-4">Shopping Cart</h1>

        <div className="mx-5">
          <hr className="border-t border-gray-300" />
        </div>
      
          
        {isFetched? <div><div className=" m-20">
            {cartItems.map((item) => (
              <div
                key={item.id}
                className="flex items-center justify-between  border-b py-4"
              >
                
                <div className="flex items-center gap-4">
                <input type="checkbox" className="m-2" value={item.id} onChange={handleCheckbox}/>
                  <div className="w-16 h-16 bg-gray-200 p-1 rounded-sm">
                    <img
                      src="https://via.placeholder.com/150"
                      alt="Product"
                      className="w-full h-full object-cover"
                    />
                  </div>
                  <span className="text-sm font-medium">{item.title}</span>
                </div>
                <span className="text-sm">${item.price.toFixed(2)}</span>
                <div className="flex items-center gap-2">
                    <button
                      onClick={() => updateQuantity(item.id, 1)}
                      className="px-2 py-1 bg-gray-200 rounded-md"
                    >
                      +
                    </button>
                  <span className="text-sm font-medium">{item.count}</span>
                    <button
                      onClick={() => updateQuantity(item.id, -1)}
                      className="px-2 py-1 bg-gray-200 rounded-md "
                    >
                      -
                    </button>
                </div>
                  <button
                    onClick={() => removeItem(item.id)}
                    className=" bg-gray-200 p-1 rounded-sm m-2"
                  >
                    <img src={delimg} className="w-5"/>
                  </button>
              </div>
            ))}
          </div>

          <div className="flex justify-center">
            <button onClick={handleSubmit} className="p-5 bg-purple-400 text-white py-2 m-5 rounded-md text-center hover:bg-purple-500">
              Go to checkout
            </button>
          </div></div> : <div className="flex justify-center p-20"><img src={LoadingImg} className="w-10 "/></div>}
    
  
      </div>
    </div>
    </>
  );
};

export default ShoppingCartPage;
