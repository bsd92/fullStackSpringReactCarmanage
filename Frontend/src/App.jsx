import React from "react";
import "./App.css";
import ListCarComponent from "./components/ListCarComponent";
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import { Routes, Route, Navigate } from "react-router-dom";
import {} from "react-router-dom";
import { BrowserRouter } from "react-router-dom";
import CreateCarComponent from "./components/CreateCarComponent";
import UpdateCarComponent from "./components/UpdateCarComponent";
import PageNotFound from "./components/pageNotFound";
import Login from "./components/Login";
import Register from "./components/Register";
import PrivateRoute from "./components/PrivateRoute";

export default function App() {
  const isAuthenticated = !!localStorage.getItem("accessToken");

  return (
    <div className="app-layout">
      <BrowserRouter>
        <HeaderComponent />
        <div className="main-content">
          <Routes>
            <Route
              path="/login"
              element={!isAuthenticated ? <Login /> : <Navigate to="/" />}
            />
            <Route path="/register" element={<Register />} />
            <Route path="/" element={<Navigate to="/login" replace />} />

            {/* Routes protégées */}

            <Route
              path="/cars"
              element={
                <PrivateRoute>
                  <ListCarComponent />
                </PrivateRoute>
              }
            />
            <Route
              path="/add-car"
              element={
                <PrivateRoute>
                  {" "}
                  <CreateCarComponent />{" "}
                </PrivateRoute>
              }
            />
            <Route
              path="/update-car/:immatriculation"
              element={
                <PrivateRoute>
                  {" "}
                  <UpdateCarComponent />{" "}
                </PrivateRoute>
              }
            />
            <Route path="*" element={<PageNotFound />} />
          </Routes>
        </div>
        <FooterComponent />
      </BrowserRouter>
    </div>
  );
}
