import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

const CreateOrderPage = () => {
  const { id } = useParams();
  const [products, setProducts] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  
  useEffect(() => {
    fetch('http://localhost:8180/Shop/api/myBatis/products')
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

    fetch(`http://localhost:8180/Shop/api/myBatis/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(order),
    })
      .then((res) => {
        console.log("Response from server: ", res);
        res.json()
      })
      .then(() => alert('Order created!'))
      .catch((err) => console.error('Failed to create order:', err));
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Create Order</h1>
      <h2>Select Products</h2>
  
      {/* Table for products with borders */}
      <table style={{ width: '100%', border: '1px solid black', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid black', padding: '10px', textAlign: 'center' }}>Select</th>
            <th style={{ border: '1px solid black', padding: '10px', textAlign: 'center' }}>Product Name</th>
            <th style={{ border: '1px solid black', padding: '10px', textAlign: 'center' }}>Price (EUR)</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>
                <input
                  type="checkbox"
                  value={product.id}
                  onChange={() => handleCheckboxChange(product.id)}
                />
              </td>
              <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>{product.name}</td>
              <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>{product.price} EUR</td>
            </tr>
          ))}
        </tbody>
      </table>
  
      {/* Submit Button */}
      <div style={{ marginTop: '1rem', textAlign: 'center' }}>
        <button onClick={handleSubmitOrder} style={{ padding: '10px 20px', cursor: 'pointer' }}>
          Submit Order
        </button>
      </div>
    </div>
  );
};

export default CreateOrderPage;
