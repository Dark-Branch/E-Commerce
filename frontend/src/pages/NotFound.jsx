// pages/NotFound.js
import React from 'react';
import { Link } from 'react-router-dom';

function NotFound() {
  return (
    <div style={{ textAlign: 'center', marginTop: '50px' }}>
      <h1>404</h1>
      <p>Oops! The page you are looking for does not exist.</p>
      <Link to="/" style={{ textDecoration: 'none', color: 'blue' }}>
        Go back to Homepage
      </Link>
    </div>
  );
}

export default NotFound;
