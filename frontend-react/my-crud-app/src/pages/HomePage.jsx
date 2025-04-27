import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const [rawData, setRawData] = useState([]);
  const [groupedCustomers, setGroupedCustomers] = useState([]);
  const [activeTab, setActiveTab] = useState('jpa');
  const navigate = useNavigate();

  useEffect(() => {
    setRawData([]);

    const prefix = activeTab === 'myBatis' ? '/api/myBatis' : '/api';

    fetch(`http://localhost:8180/Shop${prefix}/customers/withOrdersAndProducts`)
      .then((res) => res.json())
      .then((data) => setRawData(data))
      .catch((err) => console.error('Failed to fetch customers:', err));
  }, [activeTab]);

  useEffect(() => {
    const customerMap = {};

    rawData.forEach(item => {
      if (!customerMap[item.customerId]) {
        customerMap[item.customerId] = {
          customerId: item.customerId,
          firstName: item.firstName,
          lastName: item.lastName,
          email: item.email,
          orders: {}
        };
      }

      if (item.orderId) {
        if (!customerMap[item.customerId].orders[item.orderId]) {
          customerMap[item.customerId].orders[item.orderId] = {
            orderId: item.orderId,
            orderDate: item.orderDate,
            products: []
          };
        }

        if (item.productId) {
          customerMap[item.customerId].orders[item.orderId].products.push({
            productId: item.productId,
            productName: item.productName,
            productPrice: item.productPrice
          });
        }
      }
    });

    // Convert maps to arrays
    const customersArray = Object.values(customerMap).map(customer => ({
      ...customer,
      orders: Object.values(customer.orders)
    }));

    setGroupedCustomers(customersArray);
  }, [rawData]);

  const formatDate = (dateString) => {
    if (!dateString) return '';
  
    const parts = dateString.split(' '); 
    if (parts.length < 6) return '';
  
    const day = parts[2];
    const monthStr = parts[1];
    const year = parts[5];
  
    const monthMap = {
      Jan: '01', Feb: '02', Mar: '03', Apr: '04', May: '05', Jun: '06',
      Jul: '07', Aug: '08', Sep: '09', Oct: '10', Nov: '11', Dec: '12'
    };
  
    const month = monthMap[monthStr];
  
    if (!month) return '';
  
    return `${year}-${month}-${day}`;
  };
  
  const formatPrice = (price) => {
    if (price == null) return '';
    return `${parseFloat(price).toFixed(2)} Eur`;
  };

  const handleAddNewCustomer = () => {
    navigate(`/${activeTab}/add-customer`);
  };

  const handleAddNewOrder = (customerId) => {
    console.log('Add order to customer', customerId);

    const prefix = activeTab === 'myBatis' ? '/myBatis' : '/jpa';

    navigate(`${prefix}/customers/${customerId}/order`);
  };

  const handleAddNewProduct = (orderId) => {
    console.log('Add products to order', orderId);

    const prefix = activeTab === 'myBatis' ? '/myBatis' : '/jpa';

    navigate(`${prefix}/add-products-to-order/${orderId}`);
  };

  return (
    <div style={{ padding: '1.5rem', maxWidth: '1600px', margin: '0 auto' }}>
      <div style={{ marginBottom: '1rem', textAlign: 'center' }}>
        <button onClick={() => setActiveTab('jpa')} disabled={activeTab === 'jpa'} style={{ marginRight: '0.5rem' }}>
          JPA
        </button>
        <button onClick={() => setActiveTab('myBatis')} disabled={activeTab === 'myBatis'}>
          MyBatis
        </button>
      </div>
  
      <h1 style={{ textAlign: 'center', marginBottom: '1.5rem', fontSize: '1.8rem' }}>
        Customers, Orders and Products
      </h1>
  
      <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
        <button onClick={handleAddNewCustomer}>➕ Add New Customer</button>
      </div>
  
      <div
        style={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: '1.5rem',
          alignItems: 'flex-start',
          justifyContent: 'flex-start',
        }}
      >
        {groupedCustomers.map((customer) => (
          <div
            key={customer.customerId}
            style={{
              flex: '1 1 48%',
              minWidth: '500px',
              border: '1px solid #0077cc',
              borderRadius: '6px',
              padding: '0.8rem',
              backgroundColor: '#f9f9f9',
            }}
          >
            <div style={{ marginBottom: '0.8rem', textAlign: 'left' }}>
              <h2 style={{ margin: '0.3rem 0' }}>{customer.firstName} {customer.lastName}</h2>
              <p style={{ margin: '0.2rem 0', fontSize: '0.9rem' }}>Email: {customer.email}</p>
              <button onClick={() => handleAddNewOrder(customer.customerId)} style={{ marginTop: '0.5rem' }}>➕ Add Order</button>
            </div>
  
            {customer.orders.length > 0 ? (
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '1rem' }}>
                {customer.orders.map(order => (
                  <div
                    key={order.orderId}
                    style={{
                      flex: '1 1 48%',
                      minWidth: '250px',
                      border: '1px solid #ccc',
                      borderRadius: '5px',
                      padding: '0.8rem',
                      backgroundColor: '#ffffff',
                    }}
                  >
                    <div style={{ textAlign: 'left', marginBottom: '0.5rem' }}>
                      <h3 style={{ margin: '0.2rem 0' }}>Order #{order.orderId}</h3>
                      <p style={{ margin: '0.2rem 0', fontSize: '0.85rem' }}>Date: {formatDate(order.orderDate)}</p>
                      <button onClick={() => handleAddNewProduct(order.orderId)} style={{ marginTop: '0.3rem' }}>➕ Add Product</button>
                    </div>
  
                    <div
                      style={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        gap: '0.5rem',
                        marginTop: '0.8rem',
                        justifyContent: 'flex-start',
                      }}
                    >
                      {order.products.map(product => (
                        <div
                          key={product.productId}
                          style={{
                            width: '120px',
                            height: '90px',
                            padding: '0.4rem',
                            border: '1px solid #ddd',
                            borderRadius: '5px',
                            backgroundColor: '#f0f0f0',
                            fontSize: '0.8rem',
                            textAlign: 'center',
                            display: 'flex',
                            flexDirection: 'column',
                            justifyContent: 'center',
                          }}
                        >
                          <strong>{product.productName}</strong>
                          <div style={{ marginTop: '0.2rem' }}>
                            Price: {formatPrice(product.productPrice)}
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p style={{ fontSize: '0.9rem', color: '#666' }}>No orders yet.</p>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default HomePage;
