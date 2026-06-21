import { useEffect, useState } from "react";

export default function ProviderDashboard() {

const token = localStorage.getItem("token");

const [profile, setProfile] = useState(null);

const [locality, setLocality] = useState("");
const [city, setCity] = useState("");
const [state, setState] = useState("");
const [pincode, setPincode] = useState("");

const [lat, setLat] = useState("");
const [lng, setLng] = useState("");

const [aadhar, setAadhar] = useState("");
const [pan, setPan] = useState("");

// 📥 Fetch Profile
const fetchProfile = async () => {
try {
const response = await fetch(
"http://localhost:8080/provider/my-listing",
{
headers: {
Authorization: "Bearer " + token
}
}
);


  if (!response.ok) {
    console.error("Failed to load provider profile");
    return;
  }

  const data = await response.json();
  setProfile(data);

} catch (error) {
  console.error("Error loading provider profile", error);
}


};

useEffect(() => {
fetchProfile();
}, []);

// 📍 Update Address
const updateAddress = async () => {

if (!locality || !city || !state || !pincode) {
  alert("Please fill all address fields");
  return;
}

if (!lat || !lng) {
  alert("Latitude and Longitude are required");
  return;
}

try {
  const response = await fetch(
    "http://localhost:8080/provider/update-address",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token
      },
      body: JSON.stringify({
        locality,
        city,
        state,
        pincode,
        latitude: parseFloat(lat),
        longitude: parseFloat(lng)
      })
    }
  );

  const message = await response.text();
  alert(message);

} catch (error) {
  console.error(error);
  alert("Failed to update address");
}


};

// 🪪 Submit KYC
const submitKYC = async () => {

try {
  const response = await fetch(
    "http://localhost:8080/provider/submit-kyc",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token
      },
      body: JSON.stringify({
        aadharNumber: aadhar,
        panNumber: pan
      })
    }
  );

  const message = await response.text();
  alert(message);

} catch (error) {
  console.error(error);
  alert("Failed to submit KYC");
}


};

if (!profile) return <p>Loading...</p>;

return (
<div style={{ maxWidth: "600px", margin: "auto" }}>


  <h2>Provider Dashboard</h2>

  <p><b>Name:</b> {profile.name}</p>
  <p><b>Service:</b> {profile.serviceType}</p>
  <p><b>Status:</b> {profile.status}</p>

  {/* 📝 Pending Verification */}
  {profile.status === "PENDING_VERIFICATION" && (
    <div>

      <h3>Complete Your Profile</h3>

      <h4>Update Address</h4>

      <input
        placeholder="Locality"
        value={locality}
        onChange={(e) => setLocality(e.target.value)}
      />

      <input
        placeholder="City"
        value={city}
        onChange={(e) => setCity(e.target.value)}
      />

      <input
        placeholder="State"
        value={state}
        onChange={(e) => setState(e.target.value)}
      />

      <input
        placeholder="Pincode"
        value={pincode}
        onChange={(e) => setPincode(e.target.value)}
      />

      {/* 📍 Latitude & Longitude */}
      <input
        placeholder="Latitude (e.g., 17.4375)"
        value={lat}
        onChange={(e) => setLat(e.target.value)}
      />

      <input
        placeholder="Longitude (e.g., 78.4483)"
        value={lng}
        onChange={(e) => setLng(e.target.value)}
      />

      <p style={{ fontSize: "12px", color: "gray" }}>
        👉 Open Google Maps → Right click → Copy latitude & longitude
      </p>

      <button onClick={updateAddress}>
        Update Address
      </button>

      <hr />

      <h4>Submit KYC</h4>

      <input
        placeholder="Aadhaar Number"
        value={aadhar}
        onChange={(e) => setAadhar(e.target.value)}
      />

      <input
        placeholder="PAN Number"
        value={pan}
        onChange={(e) => setPan(e.target.value)}
      />

      <button onClick={submitKYC}>
        Submit KYC
      </button>

    </div>
  )}

  {/* ✅ Verified */}
  {profile.status === "VERIFIED" && (
    <div>

      <h3>Your profile is live 🎉</h3>

      <p>Customers can now find you in the marketplace.</p>

      <p>
        Location: {profile.locality}, {profile.city}
      </p>

    </div>
  )}

  {/* ❌ Rejected */}
  {profile.status === "REJECTED" && (
    <div>

      <h3>Your verification was rejected ❌</h3>

      <p>Please contact admin or update your details.</p>

    </div>
  )}

</div>


);
}
