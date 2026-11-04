import React from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { SessionProvider } from './auth/SessionContext';
import { router } from './router';
import './styles.css';

createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <SessionProvider>
      <RouterProvider router={router} />
    </SessionProvider>
  </React.StrictMode>,
);