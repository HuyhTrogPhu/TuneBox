import React,{useEffect, useState} from "react";
import { ListBrand } from "../../../service/BrandsService";

const BrandList = () => {


  const[Brand,setBrand] =   useState([])

  useEffect(()=>{
   ListBrand().then((response)=>{
    setBrand(response.data);
   }).catch(error =>{
    console.error(error);
   });
  
  },[])



  return (
    <div>
      
      <table className="table table-striped table-hover">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Brands Name</th>
              <th scope="col">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">1</th>
              <td>1</td>
              <td>guitar</td>
              <td>
                <button className="btn btn-warning ms-4">Edit</button>
                <button className="btn btn-success">Avaiable</button>
              </td>
            </tr>

          </tbody>
        </table>
    </div>
  )
}

export default BrandList
