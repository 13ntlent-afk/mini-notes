import { createBrowserRouter, Navigate } from 'react-router-dom';
import { AdminInboxPage } from './pages/AdminInboxPage';
import { ContactPage } from './pages/ContactPage';
import { LoginPage } from './pages/LoginPage';
import { NoteEditorPage } from './pages/NoteEditorPage';
import { NotesPage } from './pages/NotesPage';
import { RegisterPage } from './pages/RegisterPage';
import { App } from './App';
import { ProtectedRoute } from './components/ProtectedRoute';

export const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  {
    path: '/',
    element: <ProtectedRoute><App /></ProtectedRoute>,
    children: [
      { index: true, element: <Navigate to="/notes" replace /> },
      { path: 'notes', element: <NotesPage /> },
      { path: 'notes/new', element: <NoteEditorPage /> },
      { path: 'notes/:id', element: <NoteEditorPage /> },
      { path: 'contact', element: <ContactPage /> },
      { path: 'admin', element: <AdminInboxPage /> },
    ],
  },
], { basename: import.meta.env.BASE_URL.replace(/\/$/, '') });