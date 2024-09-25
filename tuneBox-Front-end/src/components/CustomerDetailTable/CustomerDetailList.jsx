import React from "react";

const BrandList = () => {
  return (
    <table className="table table-striped table-hover">
    <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">Oder date</th>
        <th scope="col">Total-item</th>
        <th scope="col">Total-price</th>
        <th scope="col">Payment-method</th>
        <th scope="col">Status</th>
        <th scope="col">Action</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <th scope="row">1</th>
        <td>12/03/2024</td>
        <td>200</td>
        <td>20000000</td>
        <td>momo</td>
        <td>Unpaid</td>
        <td>
          <button
            className="btn"
            style={{ backgroundColor: "#e94f37" }}
            data-bs-toggle="modal"
            data-bs-target="#ViewsModal"
          >
            Views
          </button>
        </td>
      </tr>
      <tr>
        <th scope="row">2</th>
        <td>12/03/2024</td>
        <td>580</td>
        <td>256168000</td>
        <td>momo</td>
        <td>paid</td>
        <td>
          <button
            className="btn"
            style={{ backgroundColor: "#e94f37" }}
            data-bs-toggle="modal"
            data-bs-target="#ViewsModal"
          >
            Views
          </button>
        </td>
      </tr>
      <tr>
        <th scope="row">3</th>
        <td>12/03/2024</td>
        <td>580</td>
        <td>256168000</td>
        <td>momo</td>
        <td>Delivered</td>
        <td>
          <button
            className="btn"
            style={{ backgroundColor: "#e94f37" }}
            data-bs-toggle="modal"
            data-bs-target="#ViewsModal"
          >
            Views
          </button>
        </td>
      </tr>
    </tbody>
  </table>
  );
};

export default BrandList;
