import React from 'react'
import './Header.css'

const Header = () => {
  return (
    <div className="topbar d-flex justify-content-end align-items-center p-3 bg-light border-bottom">
    <div className="dropdown">
      <a
        href="#"
        className="dropdown-toggle"
        id="userDropdown"
        data-bs-toggle="dropdown"
        aria-expanded="false"
      >
        User name
        <img
          src=""
          className="rounded-pill ms-2"
          alt="User Image"
          style={{ width: 30, height: 30 }}
        />
      </a>
      <ul
        className="dropdown-menu dropdown-menu-end"
        aria-labelledby="userDropdown"
      >
        <li>
          <a className="dropdown-item" href="#">
            Profile
          </a>
        </li>
        <li>
          <a className="dropdown-item" href="#">
            Log Out
          </a>
        </li>
      </ul>
    </div>
  </div>
  )
}

export default Header
    