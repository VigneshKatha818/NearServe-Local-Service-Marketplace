import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import ProviderCard from "../components/ProviderCard";

export default function ProviderResults() {

  const [providers, setProviders] = useState([]);

  const location = useLocation();
  const params = new URLSearchParams(location.search);

  const service = params.get("service");

  useEffect(() => {

    fetch(
      `http://localhost:8080/public/providers/nearby?lat=17.4375&lng=78.4483&radius=10&service=${service}`
    )
      .then((res) => res.json())
      .then((data) => setProviders(data));

  }, [service]);

  return (
    <div style={{ maxWidth: "700px", margin: "auto" }}>
      <h2>Available {service}s</h2>

      {providers.length === 0 && <p>No providers found</p>}

      {providers.map((p) => (
        <ProviderCard key={p.id} provider={p} />
      ))}
    </div>
  );
}