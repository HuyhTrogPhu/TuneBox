import React from "react";
import CustomerDetailTable from "../../../components/CustomerDetailTable/CustomerDetailList";
import "./ManagerCustomerDetail.css";



const ManagerCustomerDetail = () => {
  return (
    <div>
      <div className="container-fluid">
        <div className="maincontain">
          <div className="row">
            <div className="col-3 text-center">
              <div
                className="infoCon p-5"
                style={{
                  marginTop: 90,
                  backgroundColor: "#D9D9D9",
                  height: 500,
                  borderRadius: 10
                }}
              >
                <img
                  src="user1.png"
                  alt="Avatar"
                  className="avatar2"
                  style={{ marginTop: "-150px" }}
                />
                <div className="cusInfo text-start">
                  <label>Name:NGB</label>
                  <label>Age:test</label>
                  <label>MORE INFO....</label>
                  <label>MORE INFO....</label>
                  <label>MORE INFO....</label>
                  <label>MORE INFO....</label>
                </div>
              </div>
            </div>
            <div className="col-9">
              <div className="text-center">
                <label className="fs-1">List Oders</label>
                <div className="row">
                  <div className="d-flex">
                    {" "}
                    <input
                      className="form-control me-2"
                      type="search"
                      placeholder="Search"
                      aria-label="Search"
                    />
                    <button className="btn btn-outline-success" type="submit">
                      Search
                    </button>
                  </div>
                </div>
                <CustomerDetailTable />
              </div>
            </div>
          </div>
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
                    </div>
                    <div className="mb-3">
                      <label htmlFor="userId" className="form-label">
                        Tên người dùng:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="userId" className="form-label">
                        Số điện thoại:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="orderDate" className="form-label">
                        Ngày đặt hàng:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="orderDate" className="form-label">
                        Ngày vận chuyển:
                      </label>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="mb-3">
                      <label htmlFor="totalItems" className="form-label">
                        Tổng số lượng:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="totalPrice" className="form-label">
                        Tổng tiền:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Phương thức thanh toán:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Trạng thái thanh toán:
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Trạng thái đơn hàng:
                      </label>
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
                  <label htmlFor="paymentMethod" className="form-label fs-3">
                    Oder Summary
                  </label>
                </div>
                <div className="row">
                  <div className="col-6">
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Tổng tiền
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Thuế
                      </label>
                    </div>
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Tổng thu
                      </label>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="mb-3">
                      <label htmlFor="paymentMethod" className="form-label">
                        Ghi chú
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
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerCustomerDetail;
