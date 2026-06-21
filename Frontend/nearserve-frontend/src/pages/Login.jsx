import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {

  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const handleLogin = async () => {

    setError("");

    try {

      // Step 1 — Login and get JWT token
      const loginResponse = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          phoneNumber: phone,
          password: password
        })
      });

      if (!loginResponse.ok) {
        throw new Error("Invalid credentials");
      }

      const token = await loginResponse.text();

      // Store JWT
      localStorage.setItem("token", token);

      // Step 2 — Get user info using token
      const userResponse = await fetch("http://localhost:8080/auth/me", {
        headers: {
          Authorization: "Bearer " + token
        }
      });

      if (!userResponse.ok) {
        throw new Error("Failed to fetch user info");
      }

      const user = await userResponse.json();

      localStorage.setItem("user", JSON.stringify(user));

      // Step 3 — Redirect based on role
      const role = user.roles[0].name;

      if (role === "ADMIN") {
        navigate("/admin");
      }
      else if (role === "SERVICE_PROVIDER") {
        navigate("/provider");
      }
      else {
        navigate("/");
      }

    } catch (err) {
      setError("Login failed. Check phone and password.");
    }
  };

  return (
    <div style={{ maxWidth: "400px", margin: "auto" }}>

      <h2>Login</h2>

      <input
        placeholder="Phone Number"
        value={phone}
        onChange={(e) => setPhone(e.target.value)}
        style={{ width: "100%", padding: "10px", marginBottom: "10px" }}
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        style={{ width: "100%", padding: "10px", marginBottom: "10px" }}
      />

      <button
        onClick={handleLogin}
        style={{ width: "100%", padding: "10px" }}
      >
        Login
      </button>

      {error && (
        <p style={{ color: "red", marginTop: "10px" }}>
          {error}
        </p>
      )}

    </div>
  );
}