import React, { useEffect, useState } from "react";
import { deleteCar, listCars } from "../services/carServices";
import { useNavigate } from "react-router-dom";
import { parseJwt } from "../services/authService";

const ListCarComponent = () => {
  const [cars, setCars] = useState([]);

  const [roles, setRoles] = useState([]);

  const navigator = useNavigate();

  useEffect(() => {
    getAllCars();
  }, []);

  function getAllCars() {
    listCars()
      .then((response) => {
        setCars(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }
  //Gestion de role des utilisateur en fonction de la connexion 
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const decoded = parseJwt(token);
    if (decoded && decoded.roles) {
      setRoles(decoded.roles);
    }
  }, []);

  const hasRole = (role) => roles.includes(role);

  function addNewCar() {
    navigator("/add-car");
  }

  function updateCar(immatriculation) {
    navigator(`/update-car/${immatriculation}`);
  }

  function removeCar(immatriculation) {
    deleteCar(immatriculation)
      .then((response) => {
        getAllCars();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  return (
    <div className="container">
      <h2 className="text-center">Listes des voitures</h2>
      <button className="btn btn-primary" disabled={!hasRole('ADMIN') && !hasRole('MANAGER') } onClick={addNewCar}>
        Add Car{" "}
      </button>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>Immatriculation</th>
            <th>Marque</th>
            <th>Modèle</th>
            <th>Statut</th>
            <th>Options</th>
          </tr>
        </thead>
        <tbody>
          {cars.map((car) => (
            <tr key={car.immatriculation}>
              <td>{car.immatriculation}</td>
              <td>{car.marque}</td>
              <td>{car.modele}</td>
              <td>{car.statut}</td>
              <td>
                <button
                  className="btn btn-info"
                  disabled={!hasRole('ADMIN') && !hasRole('MANAGER') }
                  onClick={() => updateCar(car.immatriculation)}
                >
                  Update
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => removeCar(car.immatriculation)}
                  style={{ marginLeft: "10px" }}
                  disabled={!hasRole('ADMIN')}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListCarComponent;
