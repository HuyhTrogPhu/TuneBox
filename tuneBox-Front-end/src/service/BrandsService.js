import axios from "axios";  

const BASE_API = 'http://localhost:8081//e-comAdmin/brand';

export const ListBrand =() =>{
    return axios.get(BASE_API+'/brands');
}