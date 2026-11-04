import { Outlet } from 'react-router-dom';
import { Navbar } from './components/Navbar';

export function App() {
  return (
    <div className="app-shell">
      <Navbar />
      <main className="page-content"><Outlet /></main>
    </div>
  );
}