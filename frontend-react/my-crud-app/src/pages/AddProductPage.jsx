import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AddProductPage = () => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    const parsedPrice = parseFloat(price);
    if (!name || isNaN(parsedPrice)) {
      setError('Please enter a valid name and price.');
      return;
    }

    const newProduct = {
      name,
      price: parsedPrice,
    };

    fetch('http://localhost:8180/Shop/api/products', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newProduct),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to create product');
        navigate('/manage-products');
      })
      .catch((err) => {
        console.error('Failed to create product:', err);
        setError('Failed to create product');
      });
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Add New Product</h1>
      <form onSubmit={handleSubmit} style={{ maxWidth: '500px', margin: '0 auto' }}>
        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="name" style={{ display: 'block', fontWeight: 'bold' }}>Product Name:</label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            style={{ width: '100%', padding: '0.5rem' }}
          />
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label htmlFor="price" style={{ display: 'block', fontWeight: 'bold' }}>Price (EUR):</label>
          <input
            type="number"
            id="price"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            style={{ width: '100%', padding: '0.5rem' }}
            step="0.01"
          />
        </div>

        {error && <p style={{ color: 'red' }}>{error}</p>}

        <div style={{ textAlign: 'center' }}>
          <button type="submit">Submit</button>
        </div>
      </form>
    </div>
  );
};

export default AddProductPage;
