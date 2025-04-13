import './App.css'
import { useState, useEffect } from 'react';

function App() {
  const [customers, setCustomers] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8180/Shop/api/customers')
      .then(res => res.json())
      .then(data => setCustomers(data))
      .catch(err => console.error('Failed to fetch:', err));
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Customers</h1>
      <ul>
        {customers.map((c) => (
          <li key={c.id}>
            {c.firstName} {c.lastName} — {c.email}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App
