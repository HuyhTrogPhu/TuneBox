import React from "react";
import InstrumentTable from "../../../components/Instrumentable/InstrumentList";
import "../css/ManagerInstrument.css";

const ManagerInstrument = () => {
  return (
    <div>
      {/* Main Content */}
      <div className="container-fluid">
        <div className="row m-2">
          <div className="d-flex">
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
            <button className="btn btn-outline-success" type="submit">
              Search
            </button>
          </div>
        </div>
        <button
          className="btn m-3 btn-primary"
          data-bs-toggle="modal"
          data-bs-target="#ViewsModal"
        >
          Add instrument
        </button>
        {/* table */}
        <InstrumentTable />
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
          <div className="modal-dialog modal-xl">
            <div className="modal-content">
              <div className="modal-header">
                <h1 className="modal-title fs-5" id="exampleModalLabel">
                  ADD instrument
                </h1>
                <button
                  type="button"
                  className="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                />
              </div>
              <div className="modal-body">
                <div className="row">
                  <div className="col-6">
                    <label className="mb-1">Instrument name</label>
                    <input className="form-control" />
                    <label className="mb-1">Price</label>
                    <input className="form-control" />
                    <label className="mb-1">Color</label>
                    <input className="form-control" />
                    <label className="mb-1">Quantity</label>
                    <input className="form-control" />
                    <div className="row">
                      <label className="mb-1">Picture</label>
                      <button className="btn btn-primary">
                        Change picture
                      </button>
                    </div>
                  </div>
                  <div className="col-6">
                    <label className="mb-1">Categories</label>
                    <select
                      className="form-select"
                      aria-label="Default select example"
                    >
                      <option selected="" disabled="">
                        Pick a Categories
                      </option>
                      <option value={1}>...</option>
                      <option value={0}>...</option>
                    </select>
                    <label className="mb-1">Feature</label>
                    <select
                      className="form-select"
                      aria-label="Default select example"
                    >
                      <option selected="" disabled="">
                        Pick a Feature
                      </option>
                      <option value={1}>...</option>
                      <option value={0}>...</option>
                    </select>
                    <img src="guitar1.jpg" className="instrucmentIMG" />
                  </div>
                </div>
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

export default ManagerInstrument;
