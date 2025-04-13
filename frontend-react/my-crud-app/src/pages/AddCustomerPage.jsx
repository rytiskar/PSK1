import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AddCustomerPage = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        const newCustomer = {
            firstName,
            lastName,
            email,
          };

        fetch('http://localhost:8180/Shop/api/customers', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(newCustomer),
          })
            .then((res) => res.json())
            .then((data) => {
              // Redirect to the home page or customer list after successful creation
              navigate('/');
            })
            .catch((err) => console.error('Failed to create customer:', err));
    };

    return (
        <div style={{ padding: '2rem' }}>
          <h1>Add New Customer</h1>
          <form onSubmit={handleSubmit} style={{ maxWidth: '600px', margin: '0 auto' }}>
            <div style={{ marginBottom: '1rem' }}>
              <label htmlFor="firstName" style={{ display: 'block', fontWeight: 'bold' }}>First Name:</label>
              <input
                type="text"
                id="firstName"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                style={{ width: '100%', padding: '0.5rem', border: '1px solid #ccc', borderRadius: '4px' }}
              />
            </div>
            
            <div style={{ marginBottom: '1rem' }}>
              <label htmlFor="lastName" style={{ display: 'block', fontWeight: 'bold' }}>Last Name:</label>
              <input
                type="text"
                id="lastName"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                style={{ width: '100%', padding: '0.5rem', border: '1px solid #ccc', borderRadius: '4px' }}
              />
            </div>
            
            <div style={{ marginBottom: '1rem' }}>
              <label htmlFor="email" style={{ display: 'block', fontWeight: 'bold' }}>Email:</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                style={{ width: '100%', padding: '0.5rem', border: '1px solid #ccc', borderRadius: '4px' }}
              />
            </div>
            
            <div style={{ marginTop: '1rem', textAlign: 'center' }}>
              <button type="submit">
                Submit
              </button>
            </div>
          </form>
        </div>
      );
      
}

export default AddCustomerPage;