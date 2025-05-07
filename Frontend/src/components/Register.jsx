// src/components/RegisterPage.jsx
import React, { useState } from "react";
import { register } from "../services/authService";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const [user, setUser] = useState({ username: "" , email: "", password: ""});

  const navigator=useNavigate();

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await register(user);
      alert("Inscription réussie, vous pouvez vous connecter.");
      navigator('/cars')
    } catch (err) {
      alert("Erreur lors de l'inscription");
    }
  };

  return (
    <div>
      <h2>Inscription</h2>
      <form onSubmit={handleSubmit}>
        <input
          name="username"
          placeholder="Username"
          value={user.username}
          onChange={handleChange}
          required
        /><br />
        <input
          name="email"
          type="email"
          placeholder="Email"
          value={user.email}
          onChange={handleChange}
          required
        /><br />
        <input
          name="password"
          type="password"
          placeholder="Mot de passe"
          value={user.password}
          onChange={handleChange}
          required
        /><br />
        <button type="submit">S'inscrire</button>
      </form>
    </div>
  );
};

export default Register;
