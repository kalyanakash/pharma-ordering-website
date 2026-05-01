import axios from 'axios';

// In development: Vite proxy forwards /api → localhost:8081
// In production:  VITE_API_URL must be set to your deployed backend URL
//                 e.g. https://pharmacare-backend.onrender.com
const API_URL = import.meta.env.VITE_API_URL
  ? `${import.meta.env.VITE_API_URL}/api`
  : '/api';

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
