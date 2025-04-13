import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

const CustomerPage = () => {
  const { id } = useParams();
  const [customer, setCustomer] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8180/Shop/api/customers/${id}`)
      .then((res) => res.json())
      .then((data) => setCustomer(data))
      .catch((err) => console.error('Failed to fetch customer:', err));
  }, [id]);

  if (!customer) return <div>Loading...</div>;

  return (
    <div style={{ padding: '2rem' }}>
      <h1>{customer.firstName} {customer.lastName}</h1>
      <p>Email: {customer.email}</p>
      <p>Orders:</p>
      <ul>
        {/* Display a list of the customer's orders */}
        <li>No orders yet</li>
      </ul>
      <Link to={`/customers/${id}/order`}>
        <button>Create Order</button>
      </Link>
    </div>
  );
};

export default CustomerPage;
