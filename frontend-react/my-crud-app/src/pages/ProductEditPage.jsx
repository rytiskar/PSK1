import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function ProductEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8180/Shop/api/products/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch product');
        return res.json();
      })
      .then(setProduct)
      .catch((err) => setError(err.message));
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduct((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    fetch(`http://localhost:8180/Shop/api/products`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to update product');
        return res.json();
      })
      .then(() => navigate('/'))
      .catch((err) => setError(err.message));
  };

  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>;
  if (!product) return <div>Loading...</div>;

  return (
    <div>
      <h2>Edit Product</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name: </label>
          <input
            name="name"
            value={product.name}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Price: </label>
          <input
            name="price"
            type="number"
            step="0.01"
            value={product.price}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Save</button>{' '}
        <button type="button" onClick={() => navigate(-1)}>Cancel</button>
      </form>
    </div>
  );
}

export default ProductEditPage;
