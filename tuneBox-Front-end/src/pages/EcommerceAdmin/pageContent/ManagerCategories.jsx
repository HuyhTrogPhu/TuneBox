import React from "react";
import CategoriesTable from "../../../components/CategoriesTable/CatogoriesList";
import "../css/ManagerCategories.css";


const ManagerCategories = () => {
  return (
    <div>
      <div className="container-fluid">
        <div className="row m-2">

          {/* Search  */}
          <div className="d-flex mt-5">
            <div className="input-group me-2">
              <input
                className="form-control"
                type="search"
                placeholder="Search"
                aria-label="Search"
              />
              <span className="input-group-text">
                <i className="fa-solid fa-magnifying-glass" />
              </span>
            </div>
          </div>

        </div>


        {/* Modal add */}
        <button
          data-bs-toggle="modal"
          data-bs-target="#ViewsModal"
          className="btn btn-primary mb-5 mt-3"
          style={{ marginLeft: 17 }}
        >
          Add Category
        </button>
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
                  Add new Category
                </h1>
                <button
                  type="button"
                  className="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                />
              </div>
              <div className="modal-body">
                <label className="mb-3">Category name</label>
                <input className="form-control" id="name" placeholder="Enter category name" />
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

        {/* Category table */}
        <CategoriesTable />


        {/* Pagination */}
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


      </div>
    </div>
  );
};

export default ManagerCategories;
