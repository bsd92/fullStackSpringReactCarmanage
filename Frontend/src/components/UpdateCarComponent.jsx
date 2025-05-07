import React, { useEffect, useState } from "react";
import { getCar, updateCar } from "../services/carServices";
import { useNavigate, useParams } from "react-router-dom";

const UpdateCarComponent = () => {
  const [immatriculation, setImmatriculation] = useState("");
  const [marque, setMarque] = useState("");
  const [modele, setModele] = useState("");
  const [etat, setEtat] = useState("");
  const [statut, setStatut] = useState("");

  const [errors, setErrors] = useState({
    immatriculation: "",
    marque: "",
    modele: "",
    etat: "",
    statut: "",
  });

  const handleImmatriculation = (e) => {
    setImmatriculation(e.target.value);
  };
  const navigator = useNavigate();

  const handleMarque = (e) => {
    setMarque(e.target.value);
  };
  const handleModele = (e) => {
    setModele(e.target.value);
  };
  const handleEtat = (e) => {
    setEtat(e.target.value);
  };

  const handleStatut = (e) => {
    setStatut(e.target.value);
  };

  function validateForm() {
    let valid = true;

    const copyErrors = { ...errors };

    if (immatriculation.trim()) {
      copyErrors.immatriculation = "";
    } else {
      copyErrors.immatriculation = "Immatriculation is required!";
      valid = false;
    }
    if (marque.trim()) {
      copyErrors.marque = "";
    } else {
      copyErrors.marque = "Marque is required!";
      valid = false;
    }
    if (modele.trim()) {
      copyErrors.modele = "";
    } else {
      copyErrors.modele = "Modele is required!";
      valid = false;
    }

    setErrors(copyErrors);
    return valid;
  }

  const { immatriculation: immatriculationParam } = useParams();

  useEffect(() => {
    if (immatriculationParam) {
      getCar(immatriculationParam).then((response) => {
        setImmatriculation(response.data.immatriculation);
        setMarque(response.data.marque);
        setModele(response.data.modele);
        setEtat(response.data.etat);
        setStatut(response.data.statut);
      });
    }
  }, [immatriculationParam]);

  function saveOrUpdateCar(e) {
    e.preventDefault();

    if (validateForm()) {
      const car = { immatriculation, marque, modele, etat, statut };

      updateCar(immatriculationParam, car)
        .then((response) => {
          console.log(response.data);
          navigator("/cars");
        })
        .catch((error) => {
          console.error(error);
        });
    }
  }

  //for later
  function pageTitle() {
    if (immatriculationParam) {
      return <h2 className="text-center">Update Car</h2>;
    } else {
      return <h2 className="text-center">Update Car</h2>;
    }
  }

  return (
    <div className="container">
      <br />
      <div className="row">
        <div className="card col-md-6 offset-md-3 offset-md-3">
          {pageTitle()}
          <div className="card-body">
            <form action="">
              <div className="form-group mb-2">
                <label className="form-label">Immatriculation:</label>
                <input
                  type="text"
                  placeholder="Entrer l'immatriculation"
                  name="immatriculation"
                  value={immatriculation}
                  className={`form-control && ${
                    errors.immatriculation ? "is-invalid" : ""
                  }`}
                  onChange={handleImmatriculation}
                />
                {errors.immatriculation && (
                  <div className="invalid-feedback">
                    {errors.immatriculation}
                  </div>
                )}
              </div>
              <div className="form-group mb-2">
                <label className="form-label">Marque:</label>
                <input
                  type="text"
                  placeholder="Entrer la marque"
                  name="marque"
                  value={marque}
                  className={`form-control && ${
                    errors.marque ? "is-invalid" : ""
                  }`}
                  onChange={handleMarque}
                />
                {errors.marque && (
                  <div className="invalid-feedback">{errors.marque}</div>
                )}
              </div>
              <div className="form-group mb-2">
                <label className="form-label">Modele:</label>
                <input
                  type="text"
                  placeholder="Entrer le modèle"
                  name="modele"
                  value={modele}
                  className={`form-control && ${
                    errors.modele ? "is-invalid" : ""
                  }`}
                  onChange={handleModele}
                />
                {errors.modele && (
                  <div className="invalid-feedback">{errors.modele}</div>
                )}
              </div>
              <div className="form-group mb-2">
                <label className="form-label">Etat:</label>
                <input
                  type="text"
                  placeholder="Entrer l'état"
                  name="etat"
                  value={etat}
                  className="form-control"
                  onChange={handleEtat}
                />
              </div>
              <div className="form-group mb-2">
                <label className="form-label">Statut:</label>
                <input
                  type="text"
                  placeholder="Entrer le statut"
                  name="statut"
                  value={statut}
                  className="form-control"
                  onChange={handleStatut}
                />
              </div>

              <button className="btn btn-success" onClick={saveOrUpdateCar}>
                SaveCar
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateCarComponent;
