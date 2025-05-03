import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function ProductEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [error, setError] = useState(null);
  const [showConflictPrompt, setShowConflictPrompt] = useState(false);
  
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

  const submitUpdate = (updatedProduct) => {
    fetch(`http://localhost:8180/Shop/api/products`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedProduct),
    })
      .then((res) => {
        if (res.status === 409) {
          setShowConflictPrompt(true);
          return null;
        }
        if (!res.ok) throw new Error('Failed to update product');
        navigate('/manage-products');
      })
      .catch((err) => setError(err.message));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    submitUpdate(product);
  };

  const handleForceOverwrite = async () => {
    try {
      // Fetch the latest version of the product
      const res = await fetch(`http://localhost:8180/Shop/api/products/${id}`);
      if (!res.ok) throw new Error('Failed to fetch latest product');
  
      const latest = await res.json();
  
      // Merge user's changes into the latest product object
      const updatedProduct = {
        ...latest,
        name: product.name,
        price: product.price
      };
  
      // Submit the merged product with the latest version
      await submitUpdate(updatedProduct);
  
      setShowConflictPrompt(false);
    } catch (err) {
      setError(err.message);
    }
  };

  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>;
  if (!product) return <div>Loading...</div>;

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Edit Product</h1>

      {showConflictPrompt && (
        <div style={{ backgroundColor: '#ffe0e0', padding: '1rem', marginBottom: '1rem' }}>
          <strong>Conflict detected:</strong> This product was updated by someone else.
          <br />
          Do you still want to overwrite the changes?
          <div style={{ marginTop: '1rem' }}>
            <button onClick={handleForceOverwrite}>Yes, Overwrite</button>{' '}
            <button onClick={() => navigate('/manage-products')}>Cancel</button>
          </div>
        </div>
      )}

      <form onSubmit={handleSubmit} style={{ maxWidth: '500px', margin: '0 auto' }}>
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="name" style={{ display: 'block', fontWeight: 'bold' }}>Product Name:</label>
          <input
            id="name"
            name="name"
            type="text"
            value={product.name}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '0.5rem' }}
          />
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="price" style={{ display: 'block', fontWeight: 'bold' }}>Price (EUR):</label>
          <input
            id="price"
            name="price"
            type="number"
            step="0.01"
            value={product.price}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '0.5rem' }}
          />
        </div>

        <div style={{ textAlign: 'center' }}>
          <button type="submit">Save</button>{' '}
          <button type="button" onClick={() => navigate('/manage-products')}>Cancel</button>
        </div>
      </form>
    </div>
  );
}

export default ProductEditPage;
