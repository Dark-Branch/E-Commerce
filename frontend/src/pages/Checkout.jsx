import Topbar from "../components/Topbar";
import Footer from "../components/Footer";
import master from "../assets/master.png";
import visa from "../assets/visa.png";
import Amex from "../assets/Amex.png";
import cod from "../assets/cod.png";
import { useLocation } from "react-router-dom";


const address = {
  name: 'Sithum Bimsara Wickramanayake',
  lane: 'Ranorawa Road',
  city: 'Walgasma',
  district: 'Kurunegala',
  zip: '50212',
};

const order ={
  item : 1,
  price : 5950.00,
  shipping : 80.00,
  total : 6030.00
  
}


const CheckoutPage = () => {
  const location = useLocation();
  const cartItems = location.state;


  return (
    
    <>
          <Topbar/>
       
      <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      
        

        {/* Main Content */}
        <main className="container mx-auto p-4 flex flex-col md:flex-row gap-6 w-full max-w-5xl">
          {/* Left Section */}
          <section className="bg-white p-4 rounded-md shadow-md flex-1">
            {/* Shipping Address */}
            <div className="mb-6">
              <h2 className="text-lg font-medium border-b pb-2 mb-4">Shipping address</h2>
              <p className="text-sm">
                {address.name}<br />
                {address.lane}<br />
                {address.district}<br />
                {address.zip}<br />
              </p>
              <button className="mt-4 text-sm bg-purple-300 p-2 rounded-md hover:bg-purple-500">Change address</button>
            </div>

            {/* Payment Method */}
            <div>
              <h2 className="text-lg font-medium border-b pb-2 mb-4">Choose a payment method</h2>
              <form className="space-y-4">
                <div>
                  
                  <label className="flex items-center space-x-2">
                    <input type="radio" name="payment" className="form-radio" />
                    <span className="flex items-center space-x-2">
                      <span>Master Card</span>
                      <img src =  {master} alt="Cards" className="w-10" />
                    </span>
                  </label>
                </div>
                <div>
                  <label className="flex items-center space-x-2">
                    <input type="radio" name="payment" className="form-radio" />
                    <span>Visa</span>
                    <img src =  {visa} alt="Cards" className="w-10" />
                  </label>
                </div>
                <div>
                  <label className="flex items-center space-x-2">
                    <input type="radio" name="payment" className="form-radio" />
                    <span>American Express</span>
                    <img src =  {Amex} alt="Cards" className="w-10" />
                  </label>
                </div>
                <div>
                  <label className="flex items-center space-x-2">
                    <input type="radio" name="payment" className="form-radio" />
                    <span>Cash On Delivery</span>
                    <img src =  {cod} alt="Cards" className="w-10" />
                  </label>
                </div>
              </form>
            </div>
          </section>

          {/* Right Section */}
          <section className="bg-white p-4 rounded-md shadow-md w-full md:w-1/3">
            <h2 className="text-lg font-medium border-b pb-2 mb-4">Order Summary</h2>
            <div className="space-y-2">
              <div className="flex justify-between text-sm">
                <span>Items {order.item}</span>
                <span>Rs : {order.price}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Shipping</span>
                <span>Rs : {order.shipping}</span>
              </div>
            </div>
            <div className="border-t mt-4 pt-2">
              <div className="flex justify-between text-lg font-medium">
                <span>Order total</span>
                <span>Rs : {order.total}</span>
              </div>
            </div>
            <button className="mt-6 w-full bg-purple-300 text-white py-2 rounded-md text-center cursor-pointer hover:bg-red-900" onClick={() => alert('Order confirmed!')}>
              Confirm and pay
            </button>
          </section>
        </main>
      </div>
      <Footer/>
      </>
  );
};

export default CheckoutPage;