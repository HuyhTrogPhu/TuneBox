import React from "react";
import OderTable from "../../components/Odertable/OderList";
import "./ManagerOder.css";
import Sidebar from "../../components/SideBar/Sidebar";
import Header from "../../components/Header/Header";

const MangerBrand = () => {
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
        {/* date picker */}
        <div className="row">
          <div className="col-6 daypickCon">
            <label htmlFor="startDay">From Date:</label>
            <input type="date" id="startDay" name="startDay" />
            <label htmlFor="endDay">To Date:</label>
            <input type="date" id="endDay" name="endDay" />
          </div>
          <div className="col-6 daypickCon">
            <label htmlFor="birthday">On day</label>
            <input type="date" id="birthday" name="birthday" />
          </div>
        </div>
      </div>
      {/* table */}
      <OderTable />
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
                Chi tiết hóa đơn
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
                  <div className="mb-3">
                    <label htmlFor="orderId" className="form-label">
                      Mã đơn hàng:
                    </label>
                    <label>2000</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="userId" className="form-label">
                      Tên người dùng:
                    </label>
                    <label>NGB</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="userId" className="form-label">
                      Số điện thoại:
                    </label>
                    <label>12344567</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="orderDate" className="form-label">
                      Ngày đặt hàng:
                    </label>
                    <label>99/09/1999</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="orderDate" className="form-label">
                      Ngày vận chuyển:
                    </label>
                    <label>99/09/1999</label>
                  </div>
                </div>
                <div className="col-6">
                  <div className="mb-3">
                    <label htmlFor="totalItems" className="form-label">
                      Tổng số lượng:
                    </label>
                    <label>8</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="totalPrice" className="form-label">
                      Tổng tiền:
                    </label>
                    <label>9999999</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">
                      Phương thức thanh toán:
                    </label>
                    <label>Momo</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">
                      Trạng thái đơn hàng:
                    </label>
                    <select
                      className="form-select"
                      aria-label="Default select example"
                    >
                      <option selected="" disabled="">
                        Chọn trạng thái đơn hàng
                      </option>
                      <option value={1}>Chưa thanh toán</option>
                      <option value={2}>Đã thanh toán</option>
                      <option value={3}>Đang vận chuyển</option>
                      <option value={3}>Đã giao</option>
                    </select>
                  </div>
                </div>
              </div>
              <div className="text-center">
                <label className="fs-3">CHI TIẾT HÓA ĐƠN</label>
              </div>
              <table className="table table-striped table-hover">
                <thead>
                  <tr>
                    <th scope="col">#</th>
                    <th scope="col">ID sản phẩm</th>
                    <th scope="col">Tên sản phẩm</th>
                    <th scope="col">số lượng</th>
                    <th scope="col">Giá</th>
                    <th scope="col">Tổng</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <th scope="row">1</th>
                    <td>2</td>
                    <td>guitar</td>
                    <td>3</td>
                    <td>200</td>
                    <td>600</td>
                  </tr>
                </tbody>
              </table>
              <div className="mb-3 text-center">
                <label
                  htmlFor="paymentMethod"
                  className="form-label fs-3"
                >
                  Oder Summary
                </label>
              </div>
              <div className="row">
                <div className="col-6">
                  <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">
                      Tổng tiền
                    </label>
                    <label>9999999999</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">
                      Thuế
                    </label>
                    <label>9999999999</label>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">
                      Tổng thu
                    </label>
                    <label>9999999999</label>
                  </div>
                </div>
                <div className="col-6">
                  <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">
                      Ghi chú
                    </label>
                    <label className="text-danger">
                      Không hành không rau không cay, nhiều pate ít chả
                      nhiều thịt, trứng opla rắc miếng muối tiêu chanh
                    </label>
                  </div>
                </div>
              </div>
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                data-bs-dismiss="modal"
              >
                Đóng
              </button>
              <button
                type="button"
                className="btn btn-success"
                data-bs-dismiss="modal"
              >
                Cập nhật
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MangerBrand;
