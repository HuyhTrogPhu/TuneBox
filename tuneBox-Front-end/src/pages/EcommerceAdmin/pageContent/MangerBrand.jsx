import React,{useEffect, useState} from "react";
import BrandList from "../../../components/BrandTable/BrandList";
import "../css/ManagerBrand.css";





const MangerBrand = () => {


  return (
    <div>
      {/* Main Content */}
      <div className="container-fluid">
        <div className="input-group mb-1 p-2">
          <input
            type="text"
            className="form-control"
            placeholder="Search"
            aria-label="Recipient's username"
            aria-describedby="basic-addon2"
          />
          <button className="input-group-text" id="basic-addon2">
            <i className="fa-solid fa-magnifying-glass" />
          </button>
        </div>
        <button
          data-bs-toggle="modal"
          data-bs-target="#ViewsModal"
          className="btn btn-primary"
          style={{ marginLeft: 17 }}
        >
          ADD Brands
        </button>
        {/* table */}
        <BrandList />
        {/* pagination */}
        <div className="">
          <nav aria-label="Page navigation example">
            <ul className="pagination justify-content-center text-center">
              <li className="page-item">
                <a className="page-link" href="#" aria-label="Previous">
                  <span aria-hidden="true">«</span>
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#">
                  1
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#">
                  2
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#">
                  3
                </a>
              </li>
              <li className="page-item">
                <a className="page-link" href="#" aria-label="Next">
                  <span aria-hidden="true">»</span>
                </a>
              </li>
            </ul>
          </nav>
        </div>
        {/*Modal*/}
        <div
          className="modal fade"
          id="ViewsModal"
          tabIndex={-1}
          aria-labelledby="exampleModalLabel"
          aria-hidden="true"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h1 className="modal-title fs-5" id="exampleModalLabel">
                  ADD Brands
                </h1>
                <button
                  type="button"
                  className="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                />
              </div>
              <div className="modal-body">
                <label className="mb-3">Brands name</label>
                <input />
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  data-bs-dismiss="modal"
                >
                  Close
                </button>
                <button type="button" className="btn btn-primary">
                  Save
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MangerBrand;
