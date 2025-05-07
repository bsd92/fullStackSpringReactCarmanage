//import axios from "axios";
import axiosInstance from "./interceptors";

const REST_API_BASE_URL='http://localhost:8080/garage';



export const listCars=()=>axiosInstance.get(REST_API_BASE_URL+'/read');

export const createCar=(car)=>axiosInstance.post(REST_API_BASE_URL+'/create', car)

export const getCar=(carImmatriculation)=>axiosInstance.get(REST_API_BASE_URL+'/read/'+carImmatriculation);

export const updateCar=(carImmatriculation, car)=>axiosInstance.put(REST_API_BASE_URL+'/update/'+carImmatriculation, car);

export const deleteCar=(carImmatriculation)=>axiosInstance.delete(REST_API_BASE_URL+'/delete/'+carImmatriculation);