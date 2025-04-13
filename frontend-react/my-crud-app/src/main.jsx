import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {
  BrowserRouter,
  Routes,
  Route,
} from 'react-router-dom'
import App from './App.jsx'
import HomePage from './pages/HomePage.jsx';
import CustomerPage from './pages/CustomerPage.jsx'
import ProductPage from './pages/ProductPage.jsx'
import CreateOrderPage from './pages/CreateOrderPage.jsx'
import AddCustomerPage from './pages/AddCustomerPage.jsx'


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/customers/:id" element={<CustomerPage />} />
        <Route path="/products" element={<ProductPage />} />
        <Route path="/customers/:id/order" element={<CreateOrderPage />} />
        <Route path="/add-customer" element={<AddCustomerPage />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
