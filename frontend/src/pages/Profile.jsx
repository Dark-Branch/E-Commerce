import React, { useState } from "react";
import profilePic from "../assets/user.png";
import Topbar from "../components/TopbarWithProfilePic";
import EditProfileModal from "../components/EditProfileModal";
import ChangePasswordModal from "../components/ChangePasswordModal";

const Profile = () => {
  const [isEditProfileOpen, setEditProfileOpen] = useState(false);
  const [isChangePasswordOpen, setChangePasswordOpen] = useState(false);

  const handleEditProfile = () => setEditProfileOpen(true);
  const handleChangePassword = () => setChangePasswordOpen(true);

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Topbar */}
      <Topbar />

      {/* Main Content */}
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 mt-8">
        <div className="bg-white shadow-lg rounded-lg p-6">
          {/* User Info Section */}
          <div className="flex items-center space-x-6">
            <img
              src={profilePic}
              alt="User Avatar"
              className="w-24 h-24 rounded-full border-2 border-gray-200"
            />
            <div>
              <h1 className="text-2xl font-bold text-gray-800">John Doe</h1>
              <p className="text-gray-600">johndoe@example.com</p>
              <p className="text-sm text-gray-500 mt-1">Member since Jan 2023</p>
            </div>
          </div>

          {/* Sections */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
            {/* Order History */}
            <div className="p-4 border rounded-lg shadow-sm bg-gray-50">
              <h2 className="text-lg font-semibold text-gray-700">
                Order History
              </h2>
              <ul className="mt-4 space-y-2 text-gray-600">
                <li>Order #12345 - $45.00 - Delivered</li>
                <li>Order #12346 - $30.00 - Processing</li>
                <li>Order #12347 - $25.00 - Cancelled</li>
                <li>
                  <a href="/orders" className="text-blue-500 hover:underline">
                    View all orders
                  </a>
                </li>
              </ul>
            </div>

            {/* Settings */}
            <div className="p-4 border rounded-lg shadow-sm bg-gray-50">
              <h2 className="text-lg font-semibold text-gray-700">Settings</h2>
              <ul className="mt-4 space-y-2 text-gray-600">
                <li>
                  <button
                    className="hover:text-blue-500"
                    onClick={handleEditProfile}
                  >
                    Edit Profile
                  </button>
                </li>
                <li>
                  <button
                    className="hover:text-blue-500"
                    onClick={handleChangePassword}
                  >
                    Change Password
                  </button>
                </li>
                <li>
                  <a href="/logout" className="hover:text-blue-500">
                    Log Out
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      {/* Modals */}
      {isEditProfileOpen && (
        <EditProfileModal onClose={() => setEditProfileOpen(false)} />
      )}
      {isChangePasswordOpen && (
        <ChangePasswordModal onClose={() => setChangePasswordOpen(false)} />
      )}
    </div>
  );
};

export default Profile;
