import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

const CreateOrderPage = () => {
  const { id } = useParams();
  const [products, setProducts] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  
  useEffect(() => {
    fetch('http://localhost:8180/Shop/api/products')
      .then((res) => res.json())
      .then((data) => setProducts(data))
      .catch((err) => console.error('Failed to fetch products:', err));
  }, []);

  const handleCheckboxChange = (productId) => {
    setSelectedProducts((prev) =>
      prev.includes(productId)
        ? prev.filter((id) => id !== productId)
        : [...prev, productId]
    );
  };

  const handleSubmitOrder = () => {
    const order = {
      customerId: id,
      productIds: selectedProducts,
    };

    fetch(`http://localhost:8180/Shop/api/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(order),
    })
      .then((res) => res.json())
      .then(() => alert('Order created!'))
      .catch((err) => console.error('Failed to create order:', err));
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Create Order</h1>
      <h2>Select Products</h2>
      <ul>
        {products.map((product) => (
          <li key={product.id}>
            <input
              type="checkbox"
              value={product.id}
              onChange={() => handleCheckboxChange(product.id)}
            />
            {product.name} — {product.price} USD
          </li>
        ))}
      </ul>
      <button onClick={handleSubmitOrder}>Submit Order</button>
    </div>
  );
};

export default CreateOrderPage;
