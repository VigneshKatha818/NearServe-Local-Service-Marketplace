import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function CustomerHome() {

  const [service, setService] = useState("");
  const navigate = useNavigate();

  const search = () => {
    if (!service) {
      alert("Enter service type");
      return;
    }

    navigate(`/providers?service=${service}`);
  };

  return (
    <div style={{ textAlign: "center", marginTop: "50px" }}>
      <h1>Find Local Services</h1>

      <input
        type="text"
        placeholder="Enter service (Plumber, Electrician...)"
        value={service}
        onChange={(e) => setService(e.target.value)}
        style={{ padding: "10px", width: "250px" }}
      />

      <br /><br />

      <button
        onClick={search}
        style={{
          padding: "10px 20px",
          backgroundColor: "blue",
          color: "white",
          border: "none",
          borderRadius: "5px"
        }}
      >
        Search
      </button>
    </div>
  );
}