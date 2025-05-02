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

import ProductPage from './pages/ProductPage.jsx'
import MyBatisProductPage from './pages/MyBatisProductPage.jsx'

import CreateOrderPage from './pages/CreateOrderPage.jsx'
import MyBatisOrderPage from './pages/MyBatisCreateOrder.jsx'

import AddCustomerPage from './pages/AddCustomerPage.jsx'
import MyBatisAddCustomerPage from './pages/MyBatisAddCustomerPage.jsx'

import AddProductsToOrderPage from './pages/AddProductsToOrderPage.jsx'
import MyBatisAddProductsToOrderPage from './pages/MyBatisAddProductsToOrderPage.jsx'

import ProductManagementPage from './pages/ProductManagementPage.jsx'
import ProductEditPage from './pages/ProductEditPage.jsx';


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />

        <Route path="/jpa/orders/:id" element={<ProductPage />} />
        <Route path="/myBatis/orders/:id" element={<MyBatisProductPage />} />

        <Route path="/jpa/customers/:id/order" element={<CreateOrderPage />} />
        <Route path="/myBatis/customers/:id/order" element={<MyBatisOrderPage />} />

        <Route path="/jpa/add-customer" element={<AddCustomerPage />} />
        <Route path="/myBatis/add-customer" element={<MyBatisAddCustomerPage />} />

        <Route path="/jpa/add-products-to-order/:id" element={<AddProductsToOrderPage />} />
        <Route path="/myBatis/add-products-to-order/:id" element={<MyBatisAddProductsToOrderPage />} />

        <Route path="/manage-products" element={<ProductManagementPage />} />
        <Route path="/edit/:id" element={<ProductEditPage />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
