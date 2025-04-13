import { useState, useEffect } from 'react';

const ProductPage = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8180/Shop/api/products') // Assuming this endpoint exists
      .then((res) => res.json())
      .then((data) => setProducts(data))
      .catch((err) => console.error('Failed to fetch products:', err));
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Product List</h1>
      <ul>
        {products.map((product) => (
          <li key={product.id}>{product.name} — {product.price} USD</li>
        ))}
      </ul>
      <button onClick={() => alert('Redirect to add new product page')}>Add New Product</button>
    </div>
  );
};

export default ProductPage;
