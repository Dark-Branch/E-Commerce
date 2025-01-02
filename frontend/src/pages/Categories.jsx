import Navbar from "../components/Navbar";
import Topbar from "../components/Topbar";
import Footer from "../components/Footer";

const products = [
  { title: "Wireless Noise-Cancelling Over-Ear Headphones", rate: 4.8 },
  { title: "Portable Bluetooth Speaker with Deep Bass", rate: 4.7 },
  { title: "Smartphone with 128GB Storage and Triple Camera", rate: 4.5 },
  { title: "Lightweight Laptop with 16GB RAM and SSD", rate: 4.6 },
  { title: "Ergonomic Office Chair with Adjustable Height", rate: 4.4 },
  { title: "High-Speed Blender with Stainless Steel Blades", rate: 4.3 },
  { title: "4K Ultra HD Smart TV with Voice Control", rate: 4.9 },
  { title: "Electric Kettle with Auto Shut-Off and Fast Boil", rate: 4.2 },
  { title: "Multi-Functional Air Fryer with Digital Display", rate: 4.6 },
  { title: "Smart Watch with Fitness Tracker and GPS", rate: 4.5 },
  { title: "Professional DSLR Camera with 18-55mm Lens", rate: 4.8 },
  { title: "Gaming Keyboard with RGB Backlight and Macro Keys", rate: 4.7 },
  { title: "Memory Foam Mattress with Cooling Gel Technology", rate: 4.6 },
  { title: "Cordless Vacuum Cleaner with Long Battery Life", rate: 4.4 },
  { title: "Stainless Steel Cookware Set with Non-Stick Coating", rate: 4.3 },
  { title: "Modern Ceiling Fan with LED Light and Remote Control", rate: 4.5 },
  { title: "Rechargeable Electric Toothbrush with Multiple Modes", rate: 4.7 },
  { title: "Compact DSLR Tripod with Adjustable Height", rate: 4.3 },
  { title: "Premium Leather Wallet with RFID Blocking", rate: 4.8 },
  
];




const ProductListingPage = () => {
  return (
    <>
      <header >
          <Topbar/>
          <Navbar/>
      </header>
   
      <div className="min-h-screen bg-gray-100  p-6 ">
        {/* Main Content */}
        <main className="container mx-auto flex flex-col md:flex-row gap-6 mt-6">
          {/* Sidebar */}
          <aside className="w-full md:w-1/4 bg-white p-4 rounded-md shadow-md">
            <h2 className="text-lg font-medium border-b pb-2 mb-4">Category</h2>
            <ul className="space-y-2 text-sm">
              <li><a href="#" className="hover:text-blue-600">All</a></li>
              <li><a href="#" className="hover:text-blue-600">Wristwatches</a></li>
              <li><a href="#" className="hover:text-blue-600">Brand</a></li>
              <li><a href="#" className="hover:text-blue-600">Movement</a></li>
              <li><a href="#" className="hover:text-blue-600">Department</a></li>
            </ul>
          </aside>

          {/* Product Grid */}
          <section className="flex-1">
            <h1 className="text-2xl font-semibold mb-4">Toys for all</h1>
            <p className="text-sm text-gray-600 mb-6">
              From children to adults, discover the best toys and activities
            </p>

            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 ">
              {products.map((item, index) => (
                <div
                  key={index}
                  className="bg-white p-4 rounded-md hover:shadow-lg transition-shadow cursor-pointer">
                  <div className="bg-gray-200 aspect-square rounded-md mb-4"></div>
                  <h3 className="text-sm font-medium mb-2">
                    {item.title}
                  </h3>
                  <div className="text-sm text-gray-600">{item.rate} ★</div>
                </div>
              ))}
            </div>
          </section>
        </main>
      </div>
      <Footer/>
    </>
  );
};

export default ProductListingPage;
