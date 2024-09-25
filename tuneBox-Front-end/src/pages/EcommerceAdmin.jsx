import React from 'react'
import Header from '../components/Header/Header'
import { Link,Route, Routes } from 'react-router-dom'
import ManagerBrand from './EcommerceAdmin/pageContent/MangerBrand'
import "./EcommerceAdmin/css/Sidebar.css"
import ManagerCategories from './EcommerceAdmin/pageContent/ManagerCategories'
import ManagerInstrument from './EcommerceAdmin/pageContent/ManagerInstrument'



const EcommerceAdmin = () => {
  return (
    <div>
        <div className="row">
        <div className="sidebar col-lg-2 col-md-3 vh-100">
      {/* Logo */}
      <div className="logo p-3">
        <a href="#">
          <img src="admin.png" alt="" width="100%" />
        </a>
      </div>
      {/* Menu */}
      <div className="menu">
        <ul className="list-unstyled">
          <li className="p-3">
            <i className="fa-solid fa-house" />
            <a href="#" className="text-white">
              Dashboard
            </a>
          </li>
          <li className="p-3">
            <i className="fa-solid fa-user" />
            <a href='' className="text-white">
              Customer
            </a>
          </li>
          <li className="p-3">
            <i className="fa-solid fa-music" />
            <Link to={'/Categories'} className="text-white">
              Categories
            </Link>
          </li>
          <li className="p-3">
            <i className="fa-solid fa-newspaper" />
            <Link to={'/Brand'} className="text-white">
              Brand
            </Link>
          </li>
          <li className="p-3">
            <i className="fa-solid fa-headphones" />
            <Link to={'/Instrument'} className="text-white">
              Instrument
            </Link>
          </li>
          <li className="p-3">
            <i className="fa-solid fa-circle-play" />
            <a href="#" className="text-white">
              Bill
            </a>
          </li>
          <li className="p-3">
            <i className="fa-solid fa-chart-simple" />
            <a href="#" className="text-white">
              Statistical
            </a>
          </li>
        </ul>
      </div>
    </div>
    <div className="col-10">
        <Header />
        <Routes>
      <Route path='Brand' element={<ManagerBrand />} />
      <Route path='Categories' element={<ManagerCategories />} />
      <Route path='Instrument' element={<ManagerInstrument/>} />
    </Routes>
    </div>
        </div>
        


    </div>
  )
}

export default EcommerceAdmin
