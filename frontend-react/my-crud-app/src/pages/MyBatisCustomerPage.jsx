import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

const CustomerPage = () => {
  const { id } = useParams();
  const [customer, setCustomer] = useState(null);
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    // Fetch customer info
    fetch(`http://localhost:8180/Shop/api/myBatis/customers/${id}`)
      .then((res) => res.json())
      .then((data) => setCustomer(data))
      .catch((err) => console.error('Failed to fetch customer:', err));

    // Fetch customer orders
    fetch(`http://localhost:8180/Shop/api/myBatis/customers/${id}/orders`)
      .then((res) => res.json())
      .then((data) => setOrders(data))
      .catch((err) => console.error('Failed to fetch orders:', err));
  }, [id]);

  if (!customer) return <div>Loading...</div>;

  return (
    <div style={{ padding: '2rem' }}>
      <h1>{customer.firstName} {customer.lastName}</h1>
  
      <h2>Orders</h2>
      {orders.length === 0 ? (
        <p>No orders yet.</p>
      ) : (
        <table border="1" cellPadding="10" cellSpacing="0">
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Date</th>
              <th>Details</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id}>
                <td>{order.id}</td> {/* Order ID */}
                <td>{order.date}</td> {/* Order Date */}
                <td>
                  <Link to={`/myBatis/orders/${order.id}`}>View</Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
  
      <br />
      <Link to={`/myBatis/customers/${id}/order`}>
        <button>Create Order</button>
      </Link>
    </div>
  );
};

export default CustomerPage;
