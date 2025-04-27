import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const MyBatisAddProductToOrderPage = () => {
  const { id } = useParams(); // order id
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [orderProductIds, setOrderProductIds] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [selectedProductId, setSelectedProductId] = useState(null);

  useEffect(() => {
    const fetchProductsAndOrderProducts = async () => {
      try {
        const [productsRes, orderProductsRes] = await Promise.all([
          fetch('http://localhost:8180/Shop/api/myBatis/products'),
          fetch(`http://localhost:8180/Shop/api/myBatis/orders/${id}/products`),
        ]);

        const productsData = await productsRes.json();
        const orderProductIdsData = await orderProductsRes.json();

        setProducts(productsData);
        setOrderProductIds(orderProductIdsData);

        // Show only products that don't yet exist in order
        const filtered = productsData.filter(
          (product) => !orderProductIdsData.includes(product.id)
        );
        setFilteredProducts(filtered);
      } catch (err) {
        console.error('Failed to fetch products or order products:', err);
      }
    };

    fetchProductsAndOrderProducts();
  }, [id]);

  const handleProductChange = (productId) => {
    setSelectedProductId(productId);
  };

  const handleAddProduct = () => {
    if (!selectedProductId) {
      alert('Please select a product.');
      return;
    }

    const body = { id: selectedProductId }; // ProductDto expects an id

    fetch(`http://localhost:8180/Shop/api/myBatis/orders/${id}/addProduct`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error('Failed to add product');
        }
      })
      .then(() => navigate('/')) // redirect after success
      .catch((err) => console.error('Failed to add product to order:', err));
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Add product to order</h1>
      <h2>Select a Product</h2>

      <table style={{ width: '100%', border: '1px solid black', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid black', padding: '10px', textAlign: 'center' }}>Select</th>
            <th style={{ border: '1px solid black', padding: '10px', textAlign: 'center' }}>Product Name</th>
            <th style={{ border: '1px solid black', padding: '10px', textAlign: 'center' }}>Price (EUR)</th>
          </tr>
        </thead>
        <tbody>
          {filteredProducts.map((product) => (
            <tr key={product.id}>
              <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>
                <input
                  type="radio"
                  name="selectedProduct"
                  value={product.id}
                  onChange={() => handleProductChange(product.id)}
                />
              </td>
              <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>{product.name}</td>
              <td style={{ border: '1px solid black', padding: '8px', textAlign: 'center' }}>{product.price} EUR</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div style={{ marginTop: '1rem', textAlign: 'center' }}>
        <button onClick={handleAddProduct} style={{ padding: '10px 20px', cursor: 'pointer' }}>
          Add product
        </button>
      </div>
    </div>
  );
};

export default MyBatisAddProductToOrderPage;
