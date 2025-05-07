import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';

function Login() {
  const [credentials, setCredentials] = useState({ username: '', password: '' });
  const [touched, setTouched] = useState({ username: false, password: false });
  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials({ ...credentials, [name]: value });
  };

  const handleBlur = (e) => {
    const { name } = e.target;
    setTouched({ ...touched, [name]: true });
  };

  const isInvalid = (field) => touched[field] && !credentials[field];
  const isFormValid = credentials.username && credentials.password;

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isFormValid) {
      setTouched({ username: true, password: true });
      return;
    }

    try {
      await login(credentials.username, credentials.password);
      navigate("/cars");
    } catch (error) {
      setErrorMessage("Identifiants incorrects.");
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="text-center">Connexion</h2>

      <form onSubmit={handleSubmit} noValidate>
        <div className="mb-3">
          <label htmlFor="username" className="form-label">Nom d'utilisateur *</label>
          <input
            type="text"
            id="username"
            name="username"
            className={`form-control ${isInvalid('username') ? 'is-invalid' : ''}`}
            value={credentials.username}
            onChange={handleChange}
            onBlur={handleBlur}
            required
          />
          {isInvalid('username') && (
            <div className="invalid-feedback">Le nom d'utilisateur est requis.</div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="password" className="form-label">Mot de passe *</label>
          <input
            type="password"
            id="password"
            name="password"
            className={`form-control ${isInvalid('password') ? 'is-invalid' : ''}`}
            value={credentials.password}
            onChange={handleChange}
            onBlur={handleBlur}
            required
          />
          {isInvalid('password') && (
            <div className="invalid-feedback">Le mot de passe est requis.</div>
          )}
        </div>

        <button type="submit" className="btn btn-primary w-100" disabled={!isFormValid}>
          Se connecter
        </button>
      </form>

      {errorMessage && (
        <div style={{ color: 'red', marginTop: '10px' }}>{errorMessage}</div>
      )}

      <br />
      <button
        type="button"
        className="btn btn-primary w-100"
        onClick={() => navigate('/register')}
      >
        Créer un compte
      </button>
    </div>
  );
}

export default Login;
