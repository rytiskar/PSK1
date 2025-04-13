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
          <form onSubmit={handleSubmit}>
            <div>
              <label htmlFor="firstName">First Name:</label>
              <input
                type="text"
                id="firstName"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="lastName">Last Name:</label>
              <input
                type="text"
                id="lastName"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="email">Email:</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <button type="submit">Submit</button>
          </form>
        </div>
      );
}

export default AddCustomerPage;