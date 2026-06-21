import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {

const navigate = useNavigate();

const token = localStorage.getItem("token");

const userData = localStorage.getItem("user");
const user = userData ? JSON.parse(userData) : null;

const role = user?.roles?.[0]?.name;

const logout = () => {
localStorage.removeItem("token");
localStorage.removeItem("user");


navigate("/login");

// force re-render so navbar updates
window.location.reload();


};

return (
<nav
style={{
padding: "15px",
borderBottom: "1px solid #ddd",
marginBottom: "20px"
}}
>


  <Link to="/" style={{ marginRight: "15px" }}>Home</Link>
  <Link to="/" style={{ marginRight: "15px" }}>Find Services</Link>

  {/* Guest */}
  {!token && (
    <>
      <Link to="/login" style={{ marginRight: "15px" }}>Login</Link>
      <Link to="/register" style={{ marginRight: "15px" }}>Register</Link>
    </>
  )}

  {/* Provider */}
  {token && role === "SERVICE_PROVIDER" && (
    <Link to="/provider" style={{ marginRight: "15px" }}>
      Provider Dashboard
    </Link>
  )}

  {/* Admin */}
  {token && role === "ADMIN" && (
    <Link to="/admin" style={{ marginRight: "15px" }}>
      Admin Dashboard
    </Link>
  )}

  {/* Logout */}
  {token && (
    <button
      onClick={logout}
      style={{ marginLeft: "10px" }}
    >
      Logout
    </button>
  )}

  {token && role === "CUSTOMER" && (
  <Link to="/add-provider" style={{ marginRight: "15px" }}>
    Add Provider
  </Link>
)}

</nav>


);
}
