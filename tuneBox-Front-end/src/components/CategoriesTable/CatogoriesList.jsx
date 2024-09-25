import React from "react";

const CatogoriesList = () => {
  return (
    <table className="table table-striped table-hover">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Categories Name</th>
          <th scope="col">Action</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <th scope="row">1</th>
          <td>1</td>
          <td>guitar</td>
          <td>
            <button className="btn btn-warning">Edit</button>
            <button className="btn btn-success ms-4">Avaiable</button>
          </td>
        </tr>
      </tbody>
    </table>
  );
};

export default CatogoriesList;
