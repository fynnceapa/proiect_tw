import { Link } from "react-router-dom";

export default function NotFound() {
  return (
    <main className="page">
      <section className="panel">
        <h1>404</h1>
        <p className="helper">We lost that tape. Head back to safety.</p>
        <Link className="button" to="/">
          Return home
        </Link>
      </section>
    </main>
  );
}
