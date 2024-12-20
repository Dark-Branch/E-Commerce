import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'; 
import NotFound from './pages/NotFound'; // For 404 handling
import Navbar from './components/Navbar';
import Layout from './components/Layout';

import Homepage from './pages/Homepage';
import Cart from './pages/Cart';
import Signup from './pages/Signup';
import Categories from './pages/Categories';
import Category from './pages/Category';
import Login from './pages/Login';
import ProductDetails from './pages/ProductDetails';
import Profile from './pages/Profile';
import Seller from './pages/Seller';
import SubCategory from './pages/SubCategory';
import Checkout from './pages/Checkout';


function App() {
  return (
    <Router>
      <div className="app">
        {/* Main Content: Define Routes */}
        <Routes>
        
          <Route path="/" element={<Homepage />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/categories" element={<Categories />} />
          <Route path="/category" element={<Category />} />
          {/* <Route path="/category/:id" element={<Category />} /> */}
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/login" element={<Login />} />
          <Route path="/productDetails" element={<ProductDetails />} />
          <Route path="/seller" element={<Seller />} />
          <Route path="/subCategory" element={<SubCategory />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="*" element={<NotFound />} />
         
        </Routes>

        {/* Footer */}
        {/* <Footer /> */}
      </div>
    </Router>
  );
}

export default App;
