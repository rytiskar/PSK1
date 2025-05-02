import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function ProductManagementPage() {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8180/Shop/api/products')
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch products');
        return res.json();
      })
      .then(setProducts)
      .catch((err) => setError(err.message));
  }, []);

  return (
    <div>
      <h2>Product Management</h2>

      {error && <div style={{ color: 'red' }}>Error: {error}</div>}

      {!products.length ? (
        <div>Loading products...</div>
      ) : (
        <table
          border="1"
          cellPadding="8"
          style={{ minWidth: '800px', marginTop: '16px' }}
        >
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Price</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>${product.price.toFixed(2)}</td>
                <td>
                  <button onClick={() => navigate(`/edit/${product.id}`)}>
                    Edit
                  </button>{' '}
                  <button>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default ProductManagementPage;
