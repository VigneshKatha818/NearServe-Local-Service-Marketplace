import { useState } from "react";

export default function Home() {

const [service, setService] = useState("");
const [lat, setLat] = useState("");
const [lng, setLng] = useState("");
const [providers, setProviders] = useState([]);

const [reviews, setReviews] = useState({});
const [newReview, setNewReview] = useState({});

const token = localStorage.getItem("token");

const userData = localStorage.getItem("user");
const user = userData ? JSON.parse(userData) : null;
const role = user?.roles?.[0]?.name;

// 🔍 Search Providers
const searchProviders = async () => {
try {
const response = await fetch(
`http://localhost:8080/public/providers/nearby?lat=${lat}&lng=${lng}&radius=10&service=${service}`
);


  if (!response.ok) {
    alert("Failed to fetch providers");
    return;
  }

  const data = await response.json();
  setProviders(data);

} catch (error) {
  console.error(error);
  alert("Server error while searching providers");
}


};

// 📥 Fetch Reviews (FIXED)
const fetchReviews = async (listingId) => {
try {
const response = await fetch(
`http://localhost:8080/public/reviews/${listingId}`
);


  if (!response.ok) {
    console.error("Failed to fetch reviews");
    return;
  }

  const data = await response.json();

  setReviews(prev => ({
    ...prev,
    [listingId]: data
  }));

} catch (error) {
  console.error(error);
}


};

// ✍ Submit Review
const submitReview = async (listingId) => {


const reviewData = newReview[listingId];

if (!reviewData || !reviewData.rating || !reviewData.comment) {
  alert("Please enter rating and comment");
  return;
}

try {
  const response = await fetch("http://localhost:8080/customer/add-review", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token
    },
    body: JSON.stringify({
      listingId: listingId,
      rating: parseInt(reviewData.rating),
      comment: reviewData.comment
    })
  });

  if (!response.ok) {
    alert("Failed to submit review");
    return;
  }

  alert("Review added!");

  // Refresh reviews
  fetchReviews(listingId);

} catch (error) {
  console.error(error);
  alert("Server error while submitting review");
}


};

return (
<div style={{ maxWidth: "900px", margin: "auto" }}>

  <h1>Find Local Services</h1>

  {/* 🔍 Search Section */}
  <div style={{ marginBottom: "20px" }}>

    <input
      placeholder="Latitude"
      value={lat}
      onChange={(e) => setLat(e.target.value)}
      style={{ marginRight: "10px" }}
    />

    <input
      placeholder="Longitude"
      value={lng}
      onChange={(e) => setLng(e.target.value)}
      style={{ marginRight: "10px" }}
    />

    <select
      value={service}
      onChange={(e) => setService(e.target.value)}
      style={{ marginRight: "10px" }}
    >
      <option value="">Select Service</option>
      <option value="PLUMBER">Plumber</option>
      <option value="ELECTRICIAN">Electrician</option>
      <option value="AC_REPAIR">AC Repair</option>
      <option value="TUTOR">Tutor</option>
    </select>

    <button onClick={searchProviders}>
      Search
    </button>

  </div>

  {/* 📦 Providers */}
  {providers.map((p) => (

    <div
      key={p.id}
      style={{
        border: "1px solid #ddd",
        borderRadius: "10px",
        padding: "20px",
        marginBottom: "15px",
        boxShadow: "0 2px 6px rgba(0,0,0,0.1)"
      }}
    >

      <h3>{p.name}</h3>

      <p>{p.description}</p>

      <p>📍 {p.locality}, {p.city}</p>

      {/* ⭐ Rating */}
      <p>
        ⭐ {p.averageRating || 0} ({p.totalReviews || 0} reviews)
      </p>

      <p>📏 Distance: {p.distanceKm} km</p>

      <div style={{ marginTop: "10px" }}>

        <a
          href={p.googleMapsLink}
          target="_blank"
          rel="noreferrer"
          style={{ marginRight: "15px" }}
        >
          🗺 View on Map
        </a>

        {/* 📞 Phone */}
        {token ? (
          <span style={{ fontWeight: "bold" }}>
            📞 {p.phoneNumber}
          </span>
        ) : (
          <span style={{ color: "gray" }}>
            🔒 Login to view phone
          </span>
        )}

      </div>

      <hr />

      {/* 📥 Load Reviews */}
      <button onClick={() => fetchReviews(p.id)}>
        View Reviews
      </button>

      {/* Show Reviews */}
      {reviews[p.id]?.length > 0 ? (
        reviews[p.id].map((r, index) => (
          <div key={index}>
            ⭐ {r.rating} — {r.comment}
          </div>
        ))
      ) : (
        reviews[p.id] && <p>No reviews yet</p>
      )}

      {/* ✍ Add Review */}
      {token && role === "CUSTOMER" && (
        <div style={{ marginTop: "10px" }}>

          <h4>Add Review</h4>

          <input
            type="number"
            placeholder="Rating (1-5)"
            min="1"
            max="5"
            onChange={(e) =>
              setNewReview({
                ...newReview,
                [p.id]: {
                  ...newReview[p.id],
                  rating: e.target.value
                }
              })
            }
          />

          <input
            placeholder="Comment"
            onChange={(e) =>
              setNewReview({
                ...newReview,
                [p.id]: {
                  ...newReview[p.id],
                  comment: e.target.value
                }
              })
            }
          />

          <button onClick={() => submitReview(p.id)}>
            Submit Review
          </button>

        </div>
      )}

    </div>
  ))}

</div>


);
}
