export default function ProviderCard({ provider }) {

  const mapsLink = `https://www.google.com/maps/search/?api=1&query=${provider.latitude},${provider.longitude}`;

  return (
    <div
      style={{
        border: "1px solid #ddd",
        padding: "15px",
        marginBottom: "10px",
        borderRadius: "8px"
      }}
    >
      <h3>{provider.name}</h3>

      <p>{provider.description}</p>

      <p><b>Distance:</b> {provider.distanceKm?.toFixed(2)} km</p>

      <p><b>Phone:</b> {provider.phoneNumber}</p>

      <a href={mapsLink} target="_blank">
        View on Google Maps
      </a>
    </div>
  );
}