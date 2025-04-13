import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

const ProductPage = () => {
  const { id } = useParams();
  const [products, setProducts] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8180/Shop/api/orders/${id}/products`)
      .then((res) => res.json())
      .then((productIds) => {
        // Fetch all product details in parallel
        return Promise.all(
          productIds.map((productId) =>
            fetch(`http://localhost:8180/Shop/api/products/${productId}`)
              .then((res) => res.json())
              .catch((err) => {
                console.error(`Failed to fetch product ${productId}:`, err);
                return null; // Fallback if a fetch fails
              })
          )
        );
      })
      .then((productDetails) => {
        // Filter out any failed fetches (nulls)
        setProducts(productDetails.filter(Boolean));
      })
      .catch((err) => console.error('Failed to fetch product IDs:', err));
  }, [id]);

  return (
    <div style={{ padding: '2rem', textAlign: 'center' }}>
      <h1>Product List</h1>
  
      {products.length > 0 ? (
        <table
          border="1"
          cellPadding="10"
          cellSpacing="0"
          style={{ margin: '0 auto' }}
        >
          <thead>
            <tr>
              <th>Product Name</th>
              <th>Price (EUR)</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id}>
                <td>{product.name}</td>
                <td>{product.price}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No products found.</p>
      )}
  
      <br />
      {/* <button onClick={() => alert('Redirect to add new product page')}>
        Add New Product
      </button> */}
    </div>
  );
};

export default ProductPage;
