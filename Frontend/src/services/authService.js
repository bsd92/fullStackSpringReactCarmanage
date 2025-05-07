import axiosInstance from "./interceptors";


const API_URL = "http://localhost:8080/garage";

export const login = async (username, password) => {
  const response = await axiosInstance.post(`${API_URL}/login`, { username, password });
  if (response.data.accessToken) {
    localStorage.setItem("accessToken", response.data.accessToken);
    localStorage.setItem("refreshToken", response.data.refreshToken);
  }
  return response.data;
};

export const register = async (user) => {
  return axiosInstance.post(`${API_URL}/register`, user);
};

export const logout = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
};

export function parseJwt(token) {
  if (!token) return null;
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(atob(base64));
    return payload;
  } catch (e) {
    return null;
  }
}


