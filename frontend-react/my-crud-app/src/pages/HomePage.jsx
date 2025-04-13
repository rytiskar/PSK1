import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const HomePage = () => {
  const [customers, setCustomers] = useState([]);
  const [activeTab, setActiveTab] = useState('jpa');
  const navigate = useNavigate();

  useEffect(() => {
    const prefix = activeTab === 'myBatis' ? '/api/myBatis' : '/api';

    fetch(`http://localhost:8180/Shop${prefix}/customers`)
      .then((res) => res.json())
      .then((data) => setCustomers(data))
      .catch((err) => console.error('Failed to fetch customers:', err));
  }, [activeTab]); // refetch when tab changes

  const handleAddNewCustomer = () => {
    navigate(`/${activeTab}/add-customer`);
  };

  return (
    <div style={{ padding: '2rem' }}>
      <div style={{ marginBottom: '1rem' }}>
        <button onClick={() => setActiveTab('jpa')} disabled={activeTab === 'jpa'}>
          JPA
        </button>
        <button onClick={() => setActiveTab('myBatis')} disabled={activeTab === 'myBatis'}>
          MyBatis
        </button>
      </div>

      <h1>Customer List</h1>
      <button onClick={handleAddNewCustomer}>Add New Customer</button>

      <table border="1" cellPadding="10" cellSpacing="0">
        <thead>
          <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Details</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((customer) => (
            <tr key={customer.id}>
              <td>{customer.id}</td>
              <td>{customer.firstName}</td>
              <td>{customer.lastName}</td>
              <td>{customer.email}</td>
              <td>
                <Link to={`/${activeTab}/customers/${customer.id}`}>View</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default HomePage;
