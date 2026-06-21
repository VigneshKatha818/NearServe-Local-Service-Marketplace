import { useState } from "react";

export default function Register() {

  const [name, setName] = useState("");
  const [phoneNumber, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("customer");
  const [serviceType, setServiceType] = useState("PLUMBER");

  const register = async () => {

    const endpoint =
      role === "customer"
        ? "http://localhost:8080/auth/register/customer"
        : "http://localhost:8080/auth/register/provider";

    const body = {
      name,
      phoneNumber,
      password,
      serviceType
    };

    const response = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    });

    const message = await response.text();

    alert(message);
  };

  return (
    <div style={{ maxWidth: "400px", margin: "auto" }}>

      <h2>Register</h2>

      <input
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        style={{ display: "block", marginBottom: "10px", width: "100%" }}
      />

      <input
        placeholder="Phone Number"
        value={phoneNumber}
        onChange={(e) => setPhone(e.target.value)}
        style={{ display: "block", marginBottom: "10px", width: "100%" }}
      />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        style={{ display: "block", marginBottom: "10px", width: "100%" }}
      />

      <select
        value={role}
        onChange={(e) => setRole(e.target.value)}
        style={{ display: "block", marginBottom: "10px", width: "100%" }}
      >
        <option value="customer">Customer</option>
        <option value="provider">Service Provider</option>
      </select>

      {role === "provider" && (
        <select
          value={serviceType}
          onChange={(e) => setServiceType(e.target.value)}
          style={{ display: "block", marginBottom: "10px", width: "100%" }}
        >
          <option value="PLUMBER">Plumber</option>
          <option value="ELECTRICIAN">Electrician</option>
          <option value="CARPENTER">Carpenter</option>
          <option value="AC_REPAIR">AC Repair</option>
        </select>
      )}

      <button
        onClick={register}
        style={{
          padding: "10px",
          backgroundColor: "blue",
          color: "white",
          border: "none",
          width: "100%"
        }}
      >
        Register
      </button>

    </div>
  );
}