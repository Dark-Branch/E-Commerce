import Topbar from "../components/Topbar";
import Footer from "../components/Footer";
import master from "../assets/master.png";
import visa from "../assets/visa.png";
import Amex from "../assets/Amex.png";
import cod from "../assets/cod.png";
import closeImg from "../assets/close-svgrepo-com.svg"
import LoadingImg from "../assets/Flowing gradient.gif"
import { useLocation } from "react-router-dom";
import { useEffect,useState } from "react";
import Model, { setAppElement } from "react-modal";


const CheckoutPage = () => {
  const location = useLocation();
  const dataReceived = location.state;


  const [orderDetails,setOrderDetails] = useState(null);
  const [isFetched,setIsFetched] = useState(false);
  const [popup,setPopup] = useState(false);

  useEffect(() => {
      fetch('http://localhost:8000/checkout')
      .then(res =>{
        return res.json();
      })
      .then(data =>{
        setOrderDetails(data);
      })
      .then(() => {
        setIsFetched(true);
      });
    },[]);

    const handleSubmit =(e) => {
        e.preventDefault();
        console.log("sub");
        setPopup(false);
      }
    
      const handleReceiverChange =(e) => {
        const {name,value} = e.target;
        setOrderDetails((pre) => {
          return {
            ...pre,
            accDetails:{
              ...pre.accDetails,[name] : value, 
            }
          };
        });

      }
      
      const handleAddressChange =(e) => {
        const {name,value} = e.target;
        setOrderDetails((pre) => {
          return {
            ...pre,
            accDetails:{
              ...pre.accDetails,
              address :{
                ...pre.accDetails.address,
                [name] : value,
              } 
            }
          };
        });
      }


  return (
    <>

      
      <header >
          <Topbar/>
      </header>

      <Model isOpen={popup} onRequestClose={() => setPopup(false) } appElement={document.getElementById("root")} style ={{content:{width : "50%",hight : "50%" ,margin :"auto" }}}>
        <div className="flex justify-end items-start">
          <button onClick={() => setPopup(false)}><img src ={closeImg} className="w-7 "/></button>
        </div>
        <form onSubmit={handleSubmit}>
          <div>
            <label className="block mb-1">Name</label>
            <input
              type="text"
              name="name"
              value={isFetched && orderDetails.accDetails.name}
              onChange={handleReceiverChange}
              placeholder="Enter name"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">Address No</label>
            <input
              type="text"
              name="no"
              value={isFetched && orderDetails.accDetails.address.no}
              onChange={handleAddressChange}
              placeholder="Enter address no"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">Street</label>
            <input
              type="text"
              name="street"
              value={isFetched? orderDetails.accDetails.address.street : "Loading ..."}
              onChange={handleAddressChange}
              placeholder="Enter street"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">City</label>
            <input
              type="text"
              name="city"
              value={isFetched? orderDetails.accDetails.address.city : "Loading ..."}
              onChange={handleAddressChange}
              placeholder="Enter city"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">District</label>
            <input
              type="text"
              name="district"
              value={isFetched? orderDetails.accDetails.address.district : "Loading ..."}
              onChange={handleAddressChange}
              placeholder="Enter district"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">Province</label>
            <input
              type="text"
              name="province"
              value={isFetched? orderDetails.accDetails.address.province : "Loading ..."}
              onChange={handleAddressChange}
              placeholder="Enter province"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">Country</label>
            <input
              type="text"
              name="country"
              value={isFetched? orderDetails.accDetails.address.country : "Loading ..."}
              onChange={handleAddressChange}
              placeholder="Enter country"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">Contact</label>
            <input
              type="text"
              name="contact"
              value={isFetched? orderDetails.accDetails.contact : "Loading ..."}
              onChange={handleReceiverChange}
              placeholder="Enter contact number"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <div>
            <label className="block mb-1">Email</label>
            <input
              type="email"
              name="email"
              value={isFetched? orderDetails.accDetails.email: "Loading ..."}
              onChange={handleReceiverChange}
              placeholder="Enter email"
              className="border p-2 rounded w-full mb-3"
            />
          </div>
          <button
            type="submit"
            className="mt-2 bg-blue-500 text-white p-2 rounded w-full"
          >
            Submit
          </button>
        </form>
      </Model>



       <header className="sticky top-0 z-50">
        <Topbar />
      </header>
      

      <div className="min-h-screen bg-gray-100 flex  items-center">
      
        

        {/* Main Content */}
        <main className="container mx-auto p-4 flex flex-col md:flex-row gap-6 w-full max-w-5xl">
          {/* Left Section */}
          <section className="bg-white p-4 rounded-md shadow-md flex-1">
            {/* Shipping Address */}
            <div className="mb-6">
              <h2 className="text-lg font-medium border-b pb-2 mb-4">Shipping details</h2>
              
              {isFetched? <div><p className="text-sm">
                {orderDetails.accDetails.name}<br />
                No : {orderDetails.accDetails.address.no}<br />
                {orderDetails.accDetails.address.street}, {" "}
                {orderDetails.accDetails.address.city},{" "}
                {orderDetails.accDetails.address.district},{" "}
                {orderDetails.accDetails.address.province}.<br />
                {orderDetails.accDetails.address.country}.<br /><br />

                {orderDetails.accDetails.email}<br />
                {orderDetails.accDetails.contact}<br />
                
              </p></div> : <div className="flex justify-center p-20"><img src={LoadingImg} className="w-10 "/></div>}
             
              <button className="mt-4 text-sm bg-purple-300 p-2 rounded-md hover:bg-purple-500" onClick={() => setPopup(true)}>change shiping details</button>
            </div>

            {/* Payment Method */}
            <div>
              <h2 className="text-lg font-medium border-b pb-2 mb-4">Choose a payment method</h2>
              
              <form className="space-y-4 text-sm">
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

           <section className="bg-white p-4 rounded-md shadow-md w-full md:w-1/3">
            <h2 className="text-lg font-medium border-b pb-2 mb-4">Order Summary</h2>
            <div className="space-y-2">
            {isFetched?
            <div>
              <div className="flex justify-between text-sm">
                <span>Number of Items  </span>
                <span>{orderDetails.itemCount}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Total discount </span>
                <span>Rs : {orderDetails.totalDiscount}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>Shipping</span>
                <span>Rs : {orderDetails.shippingCost}</span>
              </div>
      
            <div className="border-t mt-4 pt-2">
              <div className="flex justify-between text-lg font-medium">
                <span>Order total</span>
                <span>Rs : {orderDetails.total}</span>
              </div>
          </div>
          </div> : <div className="flex justify-center items-center p-20"><img src={LoadingImg} className="w-10 h-10"/></div>}
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
