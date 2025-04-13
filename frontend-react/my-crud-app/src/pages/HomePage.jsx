import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const HomePage = () => {
  const [customers, setCustomers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8180/Shop/api/customers')
      .then((res) => res.json())
      .then((data) => setCustomers(data))
      .catch((err) => console.error('Failed to fetch customers:', err));
  }, []);

  const handleAddNewCustomer = () => {

    navigate('/add-customer');
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Customer List</h1>
      <button onClick={handleAddNewCustomer}>Add New Customer</button>
      <ul>
        {customers.map((customer) => (
          <li key={customer.id}>
            <Link to={`/customers/${customer.id}`}>
              {customer.firstName} {customer.lastName}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default HomePage;
