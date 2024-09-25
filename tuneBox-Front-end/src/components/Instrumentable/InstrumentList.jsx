import React from "react";

const BrandList = () => {
  return (
    <table className="table table-striped table-hover">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Name</th>
              <th scope="col">Categories</th>
              <th scope="col">Price</th>
              <th scope="col">Color</th>
              <th scope="col">Quantity</th>
              <th scope="col">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">1</th>
              <td scope="col">Guitar1</td>
              <td scope="col">Guitar</td>
              <td scope="col">999999999</td>
              <td scope="col">Red rose</td>
              <td scope="col">200</td>
              <td scope="col">
                <button className="btn btn-warning">Edit</button>
                <button className="btn btn-success">Avaiable</button>
              </td>
            </tr>
          </tbody>
        </table>
  );
};

export default BrandList;
