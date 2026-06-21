import { useEffect, useState } from "react";

export default function AdminDashboard() {

const [listings, setListings] = useState([]);
const [error, setError] = useState("");

const token = localStorage.getItem("token");

const fetchPending = async () => {


try {

  const response = await fetch(
    "http://localhost:8080/admin/pending-listings",
    {
      headers: {
        Authorization: "Bearer " + token
      }
    }
  );

  if (!response.ok) {
    throw new Error();
  }

  const data = await response.json();
  setListings(data);

} catch {
  setError("Failed to load pending listings.");
}


};

const approve = async (id) => {

const response = await fetch(
  `http://localhost:8080/admin/approve/${id}`,
  {
    method: "POST",
    headers: {
      Authorization: "Bearer " + token
    }
  }
);

const message = await response.text();

alert(message);

fetchPending();


};

const reject = async (id) => {


const response = await fetch(
  `http://localhost:8080/admin/reject/${id}`,
  {
    method: "POST",
    headers: {
      Authorization: "Bearer " + token
    }
  }
);

const message = await response.text();

alert(message);

fetchPending();


};

useEffect(() => {
fetchPending();
}, []);

return (
<div style={{ maxWidth: "800px", margin: "auto" }}>


  <h2>Admin Dashboard</h2>

  {error && <p style={{ color: "red" }}>{error}</p>}

  {listings.length === 0 && !error && (
    <p>No pending listings.</p>
  )}

  {listings.map((l) => (

    <div
      key={l.id}
      style={{
        border: "1px solid #ddd",
        padding: "15px",
        marginBottom: "10px",
        borderRadius: "8px"
      }}
    >

      <h3>{l.name}</h3>
      <p>{l.description}</p>

      <p>Status: {l.status}</p>

      <p>Phone: {l.phoneNumber}</p>

      <p>
        Address: {l.locality}, {l.city}
      </p>

      <p>
        Aadhaar: {l.aadharNumber}
      </p>

      <p>
        PAN: {l.panNumber}
      </p>

      <button
        onClick={() => approve(l.id)}
        style={{
          marginRight: "10px",
          padding: "8px",
          backgroundColor: "green",
          color: "white",
          border: "none",
          borderRadius: "5px"
        }}
      >
        Approve
      </button>

      <button
        onClick={() => reject(l.id)}
        style={{
          padding: "8px",
          backgroundColor: "red",
          color: "white",
          border: "none",
          borderRadius: "5px"
        }}
      >
        Reject
      </button>

    </div>

  ))}

</div>


);
}
