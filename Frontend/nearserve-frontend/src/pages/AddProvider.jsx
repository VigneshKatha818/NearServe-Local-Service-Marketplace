import { useState } from "react";

export default function AddProvider() {

const token = localStorage.getItem("token");

const [name, setName] = useState("");
const [phoneNumber, setPhoneNumber] = useState("");
const [serviceType, setServiceType] = useState("");
const [description, setDescription] = useState("");

const submitProvider = async () => {


try {

  const response = await fetch(
    "http://localhost:8080/customer/add-provider",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token
      },
      body: JSON.stringify({
        name,
        phoneNumber,
        serviceType,
        description
      })
    }
  );

  const message = await response.text();

  alert(message);

  setName("");
  setPhoneNumber("");
  setServiceType("");
  setDescription("");

} catch (error) {
  console.error(error);
  alert("Failed to add provider");
}


};

return (
<div style={{ maxWidth: "600px", margin: "auto" }}>


  <h2>Add Service Provider</h2>

  <p>
    Know a good service provider? Add them to the platform.
  </p>

  <input
    placeholder="Provider Name"
    value={name}
    onChange={(e) => setName(e.target.value)}
    style={{ width: "100%", marginBottom: "10px" }}
  />

  <input
    placeholder="Phone Number"
    value={phoneNumber}
    onChange={(e) => setPhoneNumber(e.target.value)}
    style={{ width: "100%", marginBottom: "10px" }}
  />

  <select
    value={serviceType}
    onChange={(e) => setServiceType(e.target.value)}
    style={{ width: "100%", marginBottom: "10px" }}
  >
    <option value="">Select Service</option>
    <option value="PLUMBER">Plumber</option>
    <option value="ELECTRICIAN">Electrician</option>
    <option value="AC_REPAIR">AC Repair</option>
    <option value="TUTOR">Tutor</option>
  </select>

  <textarea
    placeholder="Description"
    value={description}
    onChange={(e) => setDescription(e.target.value)}
    style={{ width: "100%", marginBottom: "10px" }}
  />

  <button onClick={submitProvider}>
    Add Provider
  </button>

</div>


);
}
