import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import AdminDashboard from "./pages/AdminDashboard";
import ProviderDashboard from "./pages/ProviderDashboard";
import AddProvider from "./pages/AddProvider";

function App() {
  return (
    <Router>

      <Navbar />

      <div style={{ padding: "20px", fontFamily: "Arial" }}>

        <Routes>

          <Route path="/" element={<Home />} />

          <Route path="/login" element={<Login />} />

          <Route path="/register" element={<Register />} />

          <Route path="/admin" element={<AdminDashboard />} />

          <Route path="/provider" element={<ProviderDashboard />} />

          <Route path="/add-provider" element={<AddProvider />} />

        </Routes>

      </div>

    </Router>
  );
}

export default App;